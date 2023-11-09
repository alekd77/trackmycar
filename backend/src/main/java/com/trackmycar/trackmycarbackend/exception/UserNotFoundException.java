package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException() {
        super("User not found", HttpStatus.NOT_FOUND, "");
    }

    public UserNotFoundException(String debugMessage) {
        super("User not found", HttpStatus.NOT_FOUND, debugMessage);

    }
}
