package ru.practicum.common;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class StatsUtil {
    private final StatsClient statsClient;

    public void setEventViews(Event event) {
        List<ViewStatsDto> views = statsClient.getStats(event.getCreatedOn(), LocalDateTime.now(), true,
                List.of("/events/" + event.getId()));
        if (views.size() == 0) {
            event.setViews(0L);
        } else {
            event.setViews(views.get(0).getHits());
        }
    }

    public void addView(String uri, String ip) {
        statsClient.createHit(new EndpointHitDto(null, "ewm-main-service", uri, ip, LocalDateTime.now()));
    }
}

