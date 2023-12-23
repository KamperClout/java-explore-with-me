package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserShortDto {
    private Long id;
    private String name;
}
