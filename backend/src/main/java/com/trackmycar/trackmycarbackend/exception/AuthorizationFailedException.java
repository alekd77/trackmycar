package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationFailedException extends ApiException {
    public AuthorizationFailedException() {
        super("Authentication failed", HttpStatus.FORBIDDEN, "You are trying to access a resource for which you do not have permission");
    }

    public AuthorizationFailedException(String debugMessage) {
        super("Authentication failed", HttpStatus.FORBIDDEN, debugMessage);
    }
}
