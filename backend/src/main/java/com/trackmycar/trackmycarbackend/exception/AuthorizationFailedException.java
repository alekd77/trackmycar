package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationFailedException extends ApiException {
    public AuthorizationFailedException() {
        super("Authorization failed", HttpStatus.FORBIDDEN, "You are trying to access a resource for which you do not have permission");
    }

    public AuthorizationFailedException(String debugMessage) {
        super("Authorization failed", HttpStatus.FORBIDDEN, debugMessage);
    }
}
