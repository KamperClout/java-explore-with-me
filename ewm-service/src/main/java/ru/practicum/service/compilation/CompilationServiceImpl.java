package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.common.StatsUtil;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final StatsUtil statsUtil;

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        List<Event> events = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findAllById(compilationDto.getEvents());
        }
        events.forEach(statsUtil::setEventViews);
        return CompilationMapper.toCompilationDto(
                repository.save(CompilationMapper.toCompilation(compilationDto, events)));
    }

    @Override
    public void deleteCompilation(Integer compId) {
        if (repository.deleteByIdAndReturnCount(compId) == 0) {
            throw new NotFoundException(String.format("Compilation with id = %s was not found", compId));
        }
    }

    @Override
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest compilationDto) {
        var oldComp = repository.findById(compId).orElseThrow(()
                -> new NotFoundException(String.format("Compilation with id = %s was not found", compId)));
        if (compilationDto.getEvents() != null) {
            var events = eventRepository.findAllById(compilationDto.getEvents());
            events.forEach(statsUtil::setEventViews);
            oldComp.setEvents(events);
        }
        if (compilationDto.getPinned() != null) {
            oldComp.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getTitle() != null) {
            oldComp.setTitle(compilationDto.getTitle());
        }
        return CompilationMapper.toCompilationDto(repository.save(oldComp));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, PageRequest toPageRequest) {
        return repository.findAllByPinned(pinned, toPageRequest).stream()
                .map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Integer compId) {
        return CompilationMapper.toCompilationDto(repository.findById(compId).orElseThrow(()
                -> new NotFoundException(String.format("Compilation with id = %s was not found", compId))));
    }
}
