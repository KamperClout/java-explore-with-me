package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.common.Constants;
import ru.practicum.model.RequestStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(pattern = Constants.TIME_PATTERN)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
