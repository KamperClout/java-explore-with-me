package ru.practicum.service.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.List;

@Service
public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Integer compId);

    CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest compilationDto);

    List<CompilationDto> getCompilations(Boolean pinned, PageRequest toPageRequest);

    CompilationDto getCompilation(Integer compId);
}