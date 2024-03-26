package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class FailedToFinishCurrentTripException extends ApiException {
    public FailedToFinishCurrentTripException() {
        super("Failed to finish current trip", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public FailedToFinishCurrentTripException(String debugMessage) {
        super("Failed to finish current trip", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
