package com.trackmycar.trackmycarbackend.exception;

import com.trackmycar.trackmycarbackend.dto.ApiExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({AuthenticationFailedException.class})
    public ResponseEntity<ApiExceptionDto> handleAuthenticationFailed(AuthenticationFailedException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({AuthorizationFailedException.class})
    public ResponseEntity<ApiExceptionDto> handleAuthorizationFailed(AuthorizationFailedException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({InvalidInputException.class})
    public ResponseEntity<ApiExceptionDto> handleInvalidInput(InvalidInputException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }
}
