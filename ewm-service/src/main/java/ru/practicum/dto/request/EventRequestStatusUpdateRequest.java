package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.model.RequestStatus;

import java.util.List;

@AllArgsConstructor
@Getter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}
