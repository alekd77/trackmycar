package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class FailedToAddTripGeolocationException extends ApiException {
    public FailedToAddTripGeolocationException() {
        super("Failed to add trip geolocation", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public FailedToAddTripGeolocationException(String debugMessage) {
        super("Failed to add trip geolocation", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
