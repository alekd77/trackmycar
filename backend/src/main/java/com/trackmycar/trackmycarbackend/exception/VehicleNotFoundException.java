package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class VehicleNotFoundException extends ApiException {
    public VehicleNotFoundException() {
        super("Vehicle not found", HttpStatus.NOT_FOUND, "");
    }

    public VehicleNotFoundException(String debugMessage) {
        super("Vehicle not found", HttpStatus.NOT_FOUND, debugMessage);
    }
}
