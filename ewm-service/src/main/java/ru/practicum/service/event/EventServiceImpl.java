package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.StatsUtil;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.exceptions.DataConflictException;
import ru.practicum.exceptions.InvalidDatesException;
import ru.practicum.exceptions.NotAllowException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;
    private final StatsUtil statsUtil;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new InvalidDatesException("Incorrect request: start of the event is after end of the event");
        }
        if (categories != null && categories.isEmpty()) {
            categories = null;
        }
        if (states != null && states.isEmpty()) {
            states = null;
        }
        var events = repository.getEventsAdmin(users, states, categories, rangeStart, rangeEnd,
                page);
        events.forEach(statsUtil::setEventViews);
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request) {
        var oldEvent = checkEventIsExistsAndGet(eventId);
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case REJECT_EVENT:
                    if (oldEvent.getState().equals(EventState.PUBLISHED)) {
                        throw new NotAllowException("Cannot reject the event because it's in the state: PUBLISHED");
                    }
                    oldEvent.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    if (!oldEvent.getState().equals(EventState.PENDING)) {
                        throw new NotAllowException("Cannot publish the event because " +
                                "it's not in the right state: PUBLISHED");
                    }
                    oldEvent.setState(EventState.PUBLISHED);
                    break;
            }
        }
        var updateEvent = update(oldEvent, EventMapper.toUpdateEventRequest(request));
        if (updateEvent.getState().equals(EventState.PUBLISHED) &&
                updateEvent.getEventDate().isBefore(LocalDateTime.now().minusHours(1L))) {
            throw new InvalidDatesException(String.format("Field: eventDate. Error: Нельзя опубликовать событие " +
                    "позднее, чем за час после его начала. Value: %s", updateEvent.getEventDate()));
        }
        updateEvent = repository.save(updateEvent);
        statsUtil.setEventViews(updateEvent);
        return EventMapper.toEventFullDto(updateEvent);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, PageRequest toPageRequest) {
        checkUserIsExistsAndGet(userId);
        var events = repository.findAllByInitiatorId(userId, toPageRequest);
        events.forEach(statsUtil::setEventViews);
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        checkDates(eventDto.getEventDate());
        var event = EventMapper.toEvent(eventDto, checkCategoryIsExistsAndGet(eventDto.getCategory()),
                checkUserIsExistsAndGet(userId));
        event.setLocation(locationRepository.save(event.getLocation()));
        return EventMapper.toEventFullDto(repository.save(event));
    }

    @Override
    public EventFullWithCommentsDto showMyEvent(Long userId, Long eventId) {
        checkUserIsExistsAndGet(userId);
        var event = checkEventIsExistsAndGet(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotAllowException("Information is available for initiator only");
        }
        statsUtil.setEventViews(event);
        var comments = commentRepository.findAllByEventId(eventId);
        return EventMapper.toEventFullWithCommentsDto(event, comments);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        checkUserIsExistsAndGet(userId);

        var oldEvent = checkEventIsExistsAndGet(eventId);
        if (oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException("Event must not be published");
        }
        if (request.getStateAction() != null) {
            switch (request.getStateAction()) {
                case CANCEL_REVIEW:
                    oldEvent.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    oldEvent.setState(EventState.PENDING);
            }
        }
        var updateEvent = update(oldEvent, EventMapper.toUpdateEventRequest(request));
        checkDates(oldEvent.getEventDate());
        updateEvent = repository.save(updateEvent);
        statsUtil.setEventViews(updateEvent);
        return EventMapper.toEventFullDto(updateEvent);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        checkUserIsExistsAndGet(userId);
        checkEventIsExistsAndGet(eventId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }


    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest request) {
        var user = checkUserIsExistsAndGet(userId);
        var event = checkEventIsExistsAndGet(eventId);
        List<Long> ids = request.getRequestIds();
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(ids);
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            for (ParticipationRequest req : requests) {
                req.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(RequestMapper.toParticipationRequestDto(requestRepository.save(req)));
            }
            return new EventRequestStatusUpdateResult(confirmedRequests, new ArrayList<>());
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new DataConflictException(String.format("The event has already reached its participant limit=%s",
                    event.getParticipantLimit()));
        }
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (ParticipationRequest req : requests) {
            checkRequestStatus(req);
            if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    throw new DataConflictException(String.format(
                            "The event has already reached its participant limit=%s", event.getParticipantLimit()));
                }
                req.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(RequestMapper.toParticipationRequestDto(requestRepository.save(req)));
            } else {
                req.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.toParticipationRequestDto(requestRepository.save(req)));
            }
        }
        if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            for (ParticipationRequest req : requestRepository.findAllByStatus(RequestStatus.PENDING)) {
                req.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(RequestMapper.toParticipationRequestDto(requestRepository.save(req)));
            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);

    }

    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        var event = repository.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(()
                -> new NotFoundException(String.format("Event with id=%s was not found", id)));
        statsUtil.addView(request.getRequestURI(), request.getRemoteAddr());
        statsUtil.setEventViews(event);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> findAllEventsPublic(String text, List<Long> categories, Boolean paid,
                                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   Boolean onlyAvailable, String sort, HttpServletRequest request,
                                                   PageRequest toPageRequest) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100L);
        }
        if (rangeStart.isAfter(rangeEnd)) {
            throw new InvalidDatesException("Incorrect request: start of the event is after end of the event");
        }
        if (onlyAvailable != null && !onlyAvailable) {
            onlyAvailable = null;
        }
        if (categories != null && categories.isEmpty()) {
            categories = null;
        }
        var events = repository.findAllPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                toPageRequest);
        events.forEach(statsUtil::setEventViews);
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    events = events.stream()
                            .sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
                    break;
                case "VIEWS":
                    events = events.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
                    break;
            }
        }
        statsUtil.addView(request.getRequestURI(), request.getRemoteAddr());
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    private User checkUserIsExistsAndGet(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException(String.format("User with id = %s was not found", userId)));
    }

    private Event update(Event oldEvent, UpdateEventRequest request) {
        if (request.getAnnotation() != null) {
            oldEvent.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            oldEvent.setCategory(checkCategoryIsExistsAndGet(request.getCategory()));
        }
        if (request.getDescription() != null) {
            oldEvent.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            if (request.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new InvalidDatesException("EventDate must be one hour in the future at least");
            }
            oldEvent.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            oldEvent.setLocation(locationRepository.save(request.getLocation()));
        }
        if (request.getPaid() != null) {
            oldEvent.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            oldEvent.setRequestModeration(request.getRequestModeration());
        }
        if (request.getTitle() != null) {
            oldEvent.setTitle(request.getTitle());
        }
        return oldEvent;
    }

    private Event checkEventIsExistsAndGet(Long eventId) {
        return repository.findById(eventId).orElseThrow(()
                -> new NotFoundException(String.format("Event with id = %s was not found", eventId)));
    }

    private Category checkCategoryIsExistsAndGet(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(()
                -> new NotFoundException(String.format("Category with id = %s was not found", categoryId)));
    }

    private void checkDates(LocalDateTime start) {
        if (!start.isAfter(LocalDateTime.now().plusHours(2L))) {
            throw new InvalidDatesException(String.format(
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s", start));
        }
    }

    private void checkRequestStatus(ParticipationRequest request) {
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new NotAllowException("The request is not in right status: PENDING");
        }
    }
}
