package ru.practicum.exceptions;

public class DataConflictException extends RuntimeException {

    public DataConflictException(String message) {
        super(message);
    }
}
