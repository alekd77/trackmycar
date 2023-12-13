package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class HardwareTrackerIllegalStateException extends ApiException {
    public HardwareTrackerIllegalStateException() {
        super("Hardware tracker illegal state", HttpStatus.BAD_REQUEST, "");
    }

    public HardwareTrackerIllegalStateException(String debugMessage) {
        super("Hardware tracker illegal state", HttpStatus.BAD_REQUEST, debugMessage);
    }
}
