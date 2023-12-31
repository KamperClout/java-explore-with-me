package ru.practicum.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String status;
    private String reason;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
