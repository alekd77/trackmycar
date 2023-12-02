package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class FailedToDeleteTripException extends ApiException {
    public FailedToDeleteTripException() {
        super("Invalid request to delete the trip", HttpStatus.BAD_REQUEST, "");
    }

    public FailedToDeleteTripException(String debugMessage) {
        super("Invalid request to delete the trip", HttpStatus.BAD_REQUEST, debugMessage);
    }
}
