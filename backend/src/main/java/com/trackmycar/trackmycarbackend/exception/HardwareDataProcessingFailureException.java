package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class HardwareDataProcessingFailureException extends ApiException {
    public HardwareDataProcessingFailureException() {
        super("Failed to process data from hardware", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public HardwareDataProcessingFailureException(String debugMessage) {
        super("Failed to process data from hardware", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
