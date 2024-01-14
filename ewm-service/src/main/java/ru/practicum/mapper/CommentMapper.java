package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(User author, Event event, NewCommentDto commentDto) {
        return new Comment(
                null,
                commentDto.getMeaning(),
                author,
                event,
                LocalDateTime.now(),
                null
        );
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getMeaning(),
                UserMapper.toUserShortDto(comment.getAuthor()),
                EventMapper.toEventShortDto(comment.getEvent()),
                comment.getCreated(),
                comment.getUpdated()
        );
    }
}
