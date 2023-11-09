package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends ApiException {
    public InvalidInputException() {
        super("Invalid input!", HttpStatus.BAD_REQUEST, "");
    }

    public InvalidInputException(String debugMessage) {
        super("Invalid input!", HttpStatus.BAD_REQUEST, debugMessage);
    }
}
