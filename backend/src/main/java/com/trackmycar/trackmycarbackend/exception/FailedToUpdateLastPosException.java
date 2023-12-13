package com.trackmycar.trackmycarbackend.exception;

import org.springframework.http.HttpStatus;

public class FailedToUpdateLastPosException extends ApiException {
    public FailedToUpdateLastPosException() {
        super("Failed to update last position", HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public FailedToUpdateLastPosException(String debugMessage) {
        super("Failed to update last position", HttpStatus.INTERNAL_SERVER_ERROR, debugMessage);
    }
}
