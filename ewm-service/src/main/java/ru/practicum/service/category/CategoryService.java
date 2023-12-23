package ru.practicum.service.category;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories(PageRequest toPageRequest);

    CategoryDto getCategory(Long catId);
}
