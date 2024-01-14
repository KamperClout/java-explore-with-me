package ru.practicum.service.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request);

    List<EventShortDto> getUserEvents(Long userId, PageRequest toPageRequest);

    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    EventFullWithCommentsDto showMyEvent(Long userId, Long eventId);

    EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    EventFullDto getEvent(Long id, HttpServletRequest request);

    List<EventShortDto> findAllEventsPublic(String text, List<Long> categories, Boolean paid,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                            String sort, HttpServletRequest request, PageRequest toPageRequest);
}
