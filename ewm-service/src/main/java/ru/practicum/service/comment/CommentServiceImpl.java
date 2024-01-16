package ru.practicum.service.comment;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.exceptions.NotAllowException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto postComment(Long userId, Long eventId, NewCommentDto commentDto) {
        return CommentMapper.toCommentDto(repository.save(CommentMapper.toComment(
                checkUserExistsAndGet(userId), checkEventExistsAndGet(eventId), commentDto)));
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto commentDto) {
        checkUserExistsAndGet(userId);
        var oldComment = checkCommentExistsAndGet(commentId);
        if (!oldComment.getAuthor().getId().equals(userId)) {
            throw new NotAllowException("You can edit your comments only");
        }
        if (oldComment.getMeaning().equals(commentDto.getMeaning())) {
            return CommentMapper.toCommentDto(oldComment);
        }
        oldComment.setMeaning(commentDto.getMeaning());
        oldComment.setUpdated(LocalDateTime.now());
        return CommentMapper.toCommentDto(repository.save(oldComment));
    }

    @Override
    public CommentDto getComment(Long commentId) {
        return CommentMapper.toCommentDto(checkCommentExistsAndGet(commentId));
    }

    @Override
    public void deleteMyComment(Long userId, Long commentId) {
        checkUserExistsAndGet(userId);
        var comment = checkCommentExistsAndGet(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotAllowException("You can delete only your comments");
        }
        repository.delete(comment);
    }

    @Override
    public void deleteCommentAdmin(Long commentId) {
        if (repository.deleteByIdAndReturnCount(commentId) != 1) {
            throw new NotFoundException(String.format("Comment with id = %s was not found", commentId));
        }
    }

    @Override
    public List<CommentDto> getAllComments(Long userId, PageRequest pageRequest) {
        checkUserExistsAndGet(userId);
        return repository.findAllByAuthorId(userId, pageRequest).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getEventComment(Long eventId, PageRequest toPageRequest) {
        checkEventExistsAndGet(eventId);
        return repository.findAllByEventId(eventId).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    private User checkUserExistsAndGet(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException(String.format("User with id = %s was not found", userId)));
    }

    private Event checkEventExistsAndGet(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException(String.format("Event with id = %s was not found", eventId)));
    }

    private Comment checkCommentExistsAndGet(Long commentId) {
        return repository.findById(commentId).orElseThrow(()
                -> new NotFoundException(String.format("Comment with id = %s was not found", commentId)));
    }
}

