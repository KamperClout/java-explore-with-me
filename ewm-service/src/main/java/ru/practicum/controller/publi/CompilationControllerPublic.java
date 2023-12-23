package ru.practicum.controller.publi;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationControllerPublic {

    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = Constants.DEFAULT_FROM)
                                                int from,
                                                @Positive @RequestParam(defaultValue = Constants.DEFAULT_SIZE)
                                                int size) {
        log.info(String.format(
                "Получен запрос GET /compilations на получение списка подборок (pinned = %s, from = %s, size = %s)",
                pinned, from, size));
        return service.getCompilations(pinned, PaginationUtil.toPageRequest(from, size));
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Integer compId) {
        log.info(String.format(
                "Получен запрос GET /compilations/{compId} = %s на получение подборки событий", compId));
        return service.getCompilation(compId);
    }
}
