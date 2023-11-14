package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class VehicleRegistrationFailedException extends ApiException {
    public VehicleRegistrationFailedException() {
        super("Vehicle registration failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public VehicleRegistrationFailedException(String debugMessage) {
        super("Vehicle registration failed", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
