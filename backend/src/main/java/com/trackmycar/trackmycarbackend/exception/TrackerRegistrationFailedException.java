package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class TrackerRegistrationFailedException extends ApiException {
    public TrackerRegistrationFailedException() {
        super("Vehicle registration failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public TrackerRegistrationFailedException(String debugMessage) {
        super("Vehicle registration failed", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
