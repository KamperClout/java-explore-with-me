package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UpdateCommentDto {
    @NotBlank
    @Size(max = 10000)
    private String updateMeaning;
}
