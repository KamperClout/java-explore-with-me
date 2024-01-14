package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comment.CommentService;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentControllerAdmin {
    private final CommentService service;

    @DeleteMapping(path = "/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        log.info(String.format(
                "Получен запрос DELETE /admin/comments/{commentId} = %s на удаление комментария", commentId));
        service.deleteCommentAdmin(commentId);
    }
}