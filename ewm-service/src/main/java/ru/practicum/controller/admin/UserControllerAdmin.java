package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Constants;
import ru.practicum.common.PaginationUtil;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserControllerAdmin {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Получен запрос POST /admin/users на добавление нового пользователя " + newUserRequest.toString());
        return service.createUser(newUserRequest);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(defaultValue = "") List<Long> ids,
                                  @RequestParam(required = false, defaultValue = Constants.DEFAULT_FROM)
                                  int from,
                                  @RequestParam(required = false, defaultValue = Constants.DEFAULT_SIZE)
                                  int size) {
        log.info(String.format("Получен запрос GET /admin/users на получение списка пользователей с id = %s, " +
                "начиная с %s, по %s на странице", ids, from, size));
        return service.getUsers(PaginationUtil.toPageRequest(from, size), ids);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PositiveOrZero @PathVariable Long userId) {
        log.info(String.format("Получен запрос DELETE /admin/users/{userId} = %s на удаление пользователя", userId));
        service.deleteUser(userId);
    }
}
