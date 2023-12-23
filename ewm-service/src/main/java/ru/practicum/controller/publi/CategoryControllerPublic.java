package ru.practicum.controller.publi;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/categories")
public class CategoryControllerPublic {
    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = Constants.DEFAULT_FROM)
                                           int from,
                                           @Positive @RequestParam(defaultValue = Constants.DEFAULT_SIZE) int size) {
        log.info(String.format("Получен запрос GET /categories на получение списка категорий с параметрами " +
                "from = %s, size = %s", from, size));
        return service.getCategories(PaginationUtil.toPageRequest(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        log.info(String.format("Получен запрос GET /categories/{catId} = %s на получение категории", catId));
        return service.getCategory(catId);
    }
}
