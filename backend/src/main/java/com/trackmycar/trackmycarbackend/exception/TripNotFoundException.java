package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class TripNotFoundException extends ApiException {
    public TripNotFoundException() {
        super("Trip not found", HttpStatus.NOT_FOUND, "");
    }

    public TripNotFoundException(String debugMessage) {
        super("Trip not found", HttpStatus.NOT_FOUND, debugMessage);
    }
}
