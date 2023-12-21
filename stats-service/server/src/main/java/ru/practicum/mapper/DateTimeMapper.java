package ru.practicum.mapper;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@UtilityClass
public class DateTimeMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime toLocalDateTime(String stringDate) {
        return LocalDateTime.parse(stringDate, FORMATTER);
    }

    public static String toStringDate(LocalDateTime date) {
        return date.format(FORMATTER);
    }


}
