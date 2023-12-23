package ru.practicum.common;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;

@UtilityClass
public class PaginationUtil {

    public static PageRequest toPageRequest(int from, int size) {
        return PageRequest.of(from / size, size);
    }
}
