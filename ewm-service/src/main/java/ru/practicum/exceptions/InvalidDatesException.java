package ru.practicum.exceptions;

public class InvalidDatesException extends RuntimeException {
    public InvalidDatesException(String message) {
        super(message);
    }
}
