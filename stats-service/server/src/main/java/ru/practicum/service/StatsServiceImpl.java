package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        PageRequest pageable = PageRequest.of(0, 10);
        if (unique) {
            return ViewStatsMapper.toDtoList(repository.findUniqueViewStats(start, end, uris, pageable));
        } else {
            return ViewStatsMapper.toDtoList(repository.findViewStats(start, end, uris, pageable));
        }
    }

    @Transactional
    @Override
    public EndpointHitDto save(EndpointHitDto dto) {
        return EndpointHitMapper.toDto(repository.save(EndpointHitMapper.toEntity(dto)));
    }
}
