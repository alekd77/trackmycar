package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiException extends RuntimeException {
    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String debugMessage;

    public ApiException(String message, HttpStatus status, String debugMessage) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.debugMessage = debugMessage;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
