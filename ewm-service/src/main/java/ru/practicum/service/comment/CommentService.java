package ru.practicum.service.comment;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto postComment(Long userId, Long eventId, NewCommentDto commentDto);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto commentDto);

    CommentDto getComment(Long commentId);

    void deleteMyComment(Long userId, Long commentId);

    void deleteCommentAdmin(Long commentId);

    List<CommentDto> getAllComments(Long userId, PageRequest pageRequest);

    List<CommentDto> getEventComment(Long eventId, PageRequest toPageRequest);
}