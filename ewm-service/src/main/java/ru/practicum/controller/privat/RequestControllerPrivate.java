package ru.practicum.controller.privat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.request.RequestService;

import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/users/")
public class RequestControllerPrivate {
    private final RequestService service;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findRequests(@PathVariable Long userId) {
        log.info(String.format("Получен запрос GET /users/{userId} = %s /requests", userId));
        return service.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info(String.format(
                "Получен запрос POST /users/{userId} = %s /requests на добавления заявки участия в событии с id = %s",
                userId, eventId));
        return service.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info(String.format(
                "Получен запрос PATCH /users/{userId}= %s/{requestId} = %s/cancel на отмену заявки участия в событии",
                userId, requestId));
        return service.cancelRequest(userId, requestId);
    }
}
