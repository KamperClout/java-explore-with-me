package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.exceptions.InvalidDatesException;
import ru.practicum.model.EventState;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventControllerAdmin {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<EventState> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = Constants.TIME_PATTERN) LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = Constants.TIME_PATTERN) LocalDateTime rangeEnd,
                                        @PositiveOrZero @RequestParam(defaultValue = Constants.DEFAULT_FROM) int from,
                                        @Positive @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size) {
        log.info(String.format("Получен запрос GET /admin/events с параметрами users=%s, states=%s, categories=%s, " +
                        "rangeStart=%s, rangeEnd=%s, from=%s, size=%s",
                users, states, categories, rangeStart, rangeEnd, from, size));
        if (rangeStart != null && rangeEnd != null && !rangeStart.isBefore(rangeEnd)) {
            throw new InvalidDatesException("Неверные даты");
        }
        return service.getEvents(users, states, categories, rangeStart, rangeEnd,
                PaginationUtil.toPageRequest(from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@PathVariable Long eventId, @Valid @RequestBody UpdateEventAdminRequest request) {
        log.info(String.format("Получен запрос PATCH /admin/events/{eventId} = %s", eventId));
        return service.updateEvent(eventId, request);
    }

}
