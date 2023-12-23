package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exceptions.DataConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        try {
            return UserMapper.toUserDto(repository.save(UserMapper.userRequestToUser(newUserRequest)));
        } catch (
                DataIntegrityViolationException e) {
            throw new DataConflictException("This data duplicate data of other users");
        }
    }

    @Override
    public List<UserDto> getUsers(Pageable page, List<Long> ids) {
        if (!ids.isEmpty()) {
            return repository.getUsersByIdIn(ids).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        }
        return repository.findAll(page).stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        if (repository.deleteByIdAndReturnCount(userId) != 1) {
            throw new NotFoundException(String.format("User with id = %s was not found", userId));
        }
    }
}
