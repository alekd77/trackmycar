package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends ApiException {
    public AuthenticationFailedException() {
        super("Authentication failed", HttpStatus.UNAUTHORIZED, "");
    }

    public AuthenticationFailedException(String debugMessage) {
        super("Authentication failed", HttpStatus.UNAUTHORIZED, debugMessage);
    }
}
