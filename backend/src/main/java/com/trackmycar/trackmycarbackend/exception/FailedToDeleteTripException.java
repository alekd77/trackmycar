package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class FailedToDeleteTripException extends ApiException {
    public FailedToDeleteTripException() {
        super("Failed to delete the trip", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public FailedToDeleteTripException(String debugMessage) {
        super("Failed to delete the trip", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
