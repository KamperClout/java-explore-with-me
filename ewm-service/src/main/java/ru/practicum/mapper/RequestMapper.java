package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;

@UtilityClass
public class RequestMapper {

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

    public ParticipationRequest toParticipationRequest(ParticipationRequestDto participationRequestDto,
                                                       Event event, User requestor) {
        return new ParticipationRequest(
                participationRequestDto.getId(),
                event,
                requestor,
                participationRequestDto.getCreated(),
                participationRequestDto.getStatus()
        );
    }
}
