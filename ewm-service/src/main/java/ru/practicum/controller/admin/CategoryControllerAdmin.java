package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/admin/categories")
public class CategoryControllerAdmin {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info(String.format("Получен запрос POST /admin/categories на добавление новой категории %s",
                newCategoryDto.getName()));
        return service.createCategory(newCategoryDto);
    }

    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info(String.format("Получен запрос DELETE /admin/categories/{catId} = %s на удаление категории", catId));
        service.deleteCategory(catId);
    }

    @PatchMapping(path = "/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info(String.format("Получен запрос PATCH /admin/categories/{catId}=%s на изменение категории", catId));
        return service.updateCategory(catId, newCategoryDto);
    }
}
