package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.*;
import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.exception.AuthenticationFailedException;
import com.trackmycar.trackmycarbackend.exception.InvalidInputException;
import com.trackmycar.trackmycarbackend.exception.UserNotFoundException;
import com.trackmycar.trackmycarbackend.service.AuthenticationService;
import com.trackmycar.trackmycarbackend.service.TokenService;
import com.trackmycar.trackmycarbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/auth")
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    TokenService tokenService, UserService userService) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping(path="/register")
    public AppUserDto registerUser(@RequestBody RegistrationDto body) {
        AppUser user = authenticationService.registerUser(
                body.getUsername(),
                body.getPassword(),
                body.getEmail(),
                body.getName()
        );

        return new AppUserDto(user);
    }

    @PostMapping(path="/login")
    public LoginResponseDto loginUser(@RequestBody LoginDto body) {
        Authentication auth = authenticationService.authenticateUser(
                body.getUsername(),
                body.getPassword()
        );

        String token = tokenService.generateJwt(auth);
        AppUser user = userService.getUserByUsername(body.getUsername());

        return new LoginResponseDto(new AppUserDto(user), token);
    }

    @ExceptionHandler({AuthenticationFailedException.class})
    public ResponseEntity<ApiExceptionDto> handleAuthenticationFailed(AuthenticationFailedException ex) {
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
