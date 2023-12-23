package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.*;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.EventState;
import ru.practicum.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublished(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews(),
                event.getConfirmedRequests()
        );
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public Event toEvent(NewEventDto eventDto, Category category, User initiator) {
        return new Event(
                null,
                eventDto.getAnnotation(),
                category,
                LocalDateTime.now(),
                eventDto.getDescription(),
                eventDto.getEventDate(),
                initiator,
                eventDto.getLocation(),
                eventDto.getPaid() != null && eventDto.getPaid(),
                eventDto.getParticipantLimit() == null ? 0 : eventDto.getParticipantLimit(),
                null,
                eventDto.getRequestModeration() == null || eventDto.getRequestModeration(),
                EventState.PENDING,
                eventDto.getTitle(),
                null,
                null
        );
    }

    public UpdateEventRequest toUpdateEventRequest(UpdateEventAdminRequest request) {
        return new UpdateEventRequest(
                request.getAnnotation(),
                request.getCategory(),
                request.getDescription(),
                request.getEventDate(),
                request.getLocation(),
                request.getPaid(),
                request.getParticipantLimit(),
                request.getRequestModeration(),
                request.getTitle()
        );
    }

    public UpdateEventRequest toUpdateEventRequest(UpdateEventUserRequest request) {
        return new UpdateEventRequest(
                request.getAnnotation(),
                request.getCategory(),
                request.getDescription(),
                request.getEventDate(),
                request.getLocation(),
                request.getPaid(),
                request.getParticipantLimit(),
                request.getRequestModeration(),
                request.getTitle()
        );
    }
}
