package ru.practicum.exceptions;

public class EditNotAllowException extends RuntimeException {

    public EditNotAllowException(String message) {
        super(message);
    }
}
