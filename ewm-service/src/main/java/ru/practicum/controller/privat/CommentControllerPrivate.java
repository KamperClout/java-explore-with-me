package ru.practicum.controller.privat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentControllerPrivate {
    private final CommentService service;

    @PostMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable Long userId, @PathVariable Long eventId,
                                  @Valid @RequestBody NewCommentDto commentDto) {
        log.info(String.format(
                "Получен запрос POST /users/{userId} = %s/comments/{eventId} = %s на добавление комментария",
                userId, eventId));
        return service.postComment(userId, eventId, commentDto);
    }

    @PatchMapping(path = "/{commentId}")
    public CommentDto patchComment(@PathVariable Long userId, @PathVariable Long commentId,
                                   @Valid @RequestBody NewCommentDto commentDto) {
        log.info(String.format(
                "Получен запрос PATCH /users/{userId} = %s/comments/{commentId} = %s на изменение комментария",
                userId, commentId));
        return service.updateComment(userId, commentId, commentDto);
    }

    @DeleteMapping(path = "/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        log.info(String.format(
                "Получен запрос DELETE /users/{userId} = %s/comments/{commentId} = %s на удаление комментария",
                userId, commentId));
        service.deleteMyComment(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getAllUserComments(@PathVariable Long userId,
                                               @RequestParam(defaultValue = Constants.DEFAULT_FROM)
                                               @PositiveOrZero int from,
                                               @RequestParam(defaultValue = Constants.DEFAULT_SIZE)
                                               @Positive int size) {
        log.info(String.format(
                "Получен запрос GET /users/{userId} = %s/comments на просмотр своих комментариев", userId));
        return service.getAllComments(userId, PaginationUtil.toPageRequest(from, size));
    }
}
