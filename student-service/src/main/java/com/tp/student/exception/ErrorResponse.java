package com.tp.student.exception;

import java.time.LocalDateTime;

/**
 * Structured error body returned to the client on any exception.
 * Consistent shape makes it easy to parse errors in a frontend or API gateway.
 */
public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
