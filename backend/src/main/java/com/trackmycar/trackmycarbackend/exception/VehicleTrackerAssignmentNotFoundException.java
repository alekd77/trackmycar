package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class VehicleTrackerAssignmentNotFoundException extends ApiException {
    public VehicleTrackerAssignmentNotFoundException() {
        super("Assignment not found", HttpStatus.NOT_FOUND, "");
    }

    public VehicleTrackerAssignmentNotFoundException(String debugMessage) {
        super("Assignment not found", HttpStatus.NOT_FOUND, debugMessage);
    }
}
