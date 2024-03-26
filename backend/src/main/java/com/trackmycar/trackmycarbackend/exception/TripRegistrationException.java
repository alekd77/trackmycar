package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class TripRegistrationException extends ApiException {
    public TripRegistrationException() {
        super("Trip registration failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public TripRegistrationException(String debugMessage) {
        super("Trip registration failed", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
