package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.common.Constants;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String meaning;
    private UserShortDto author;
    private EventShortDto event;
    @JsonFormat(pattern = Constants.TIME_PATTERN)
    private LocalDateTime created;
    @JsonFormat(pattern = Constants.TIME_PATTERN)
    private LocalDateTime updated;
}
