package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class FailedToDeleteTripGeolocationException extends ApiException {
    public FailedToDeleteTripGeolocationException() {
        super("Failed to delete TripGeolocation", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public FailedToDeleteTripGeolocationException(String debugMessage) {
        super("Failed to delete TripGeolocation", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
