package ru.practicum.dto.event;

import lombok.Getter;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.EventState;
import ru.practicum.model.Location;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EventFullWithCommentsDto extends EventFullDto {
    List<CommentDto> comments;

    public EventFullWithCommentsDto(Long id, String annotation, CategoryDto category, LocalDateTime createdOn,
                                    String description, LocalDateTime eventDate, UserShortDto initiator,
                                    Location location, Boolean paid, Integer participantLimit,
                                    LocalDateTime publishedOn, Boolean requestModeration, EventState state,
                                    String title, Long views, Integer confirmedRequests, List<CommentDto> comments) {
        super(id, annotation, category, createdOn, description, eventDate, initiator, location, paid, participantLimit,
                publishedOn, requestModeration, state, title, views, confirmedRequests);
        this.comments = comments;
    }
}
