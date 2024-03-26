package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class TripGeolocationNotFoundException extends ApiException {
    public TripGeolocationNotFoundException() {
        super("TripGeolocation not found", HttpStatus.NOT_FOUND, "");
    }

    public TripGeolocationNotFoundException(String debugMessage) {
        super("TripGeolocation not found", HttpStatus.NOT_FOUND, debugMessage);
    }
}
