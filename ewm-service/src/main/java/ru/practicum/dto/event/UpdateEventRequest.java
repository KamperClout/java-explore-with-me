package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.common.Constants;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    @JsonFormat(pattern = Constants.TIME_PATTERN)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
