package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class VehicleTrackerAssignmentRegistrationFailedException extends ApiException {
    public VehicleTrackerAssignmentRegistrationFailedException() {
        super("Vehicle Tracker Assignment registration failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public VehicleTrackerAssignmentRegistrationFailedException(String debugMessage) {
        super("Vehicle Tracker Assignment registration failed", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
