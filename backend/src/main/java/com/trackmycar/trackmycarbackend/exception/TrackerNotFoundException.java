package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class TrackerNotFoundException extends ApiException {
    public TrackerNotFoundException() {
        super("Tracker not found", HttpStatus.NOT_FOUND, "");
    }

    public TrackerNotFoundException(String debugMessage) {
        super("Tracker not found", HttpStatus.NOT_FOUND, debugMessage);
    }
}
