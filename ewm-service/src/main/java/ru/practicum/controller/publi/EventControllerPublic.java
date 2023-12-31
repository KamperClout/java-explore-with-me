package ru.practicum.controller.publi;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/events")
public class EventControllerPublic {
    private final EventService service;

    @GetMapping("/{id}")
    public EventFullDto findEvent(@PathVariable Long id, HttpServletRequest request) {
        log.info(String.format("Получен запрос GET /events/{id} = %s на получение категории", id));
        return service.getEvent(id, request);
    }

    @GetMapping
    public List<EventShortDto> findAllEvents(@RequestParam(required = false) String text,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) Boolean paid,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = Constants.TIME_PATTERN) LocalDateTime rangeStart,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = Constants.TIME_PATTERN) LocalDateTime rangeEnd,
                                             @RequestParam(required = false) Boolean onlyAvailable,
                                             @RequestParam(required = false) String sort,
                                             @PositiveOrZero @RequestParam(defaultValue = Constants.DEFAULT_FROM)
                                             Integer from,
                                             @Positive @RequestParam(defaultValue = Constants.DEFAULT_SIZE)
                                             Integer size, HttpServletRequest request) {
        log.info(String.format("Получен GET /events запрос на получение списка событий с параметрами: text = %s, " +
                        "categories = %s, paid = %s, rangeStart = %s, rangeEnd = %s, onlyAvailable = %s, sort = %s, " +
                        "from = %s, size = %s", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size));
        return service.findAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                request, PaginationUtil.toPageRequest(from, size));
    }
}
