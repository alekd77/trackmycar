package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.LoginDTO;
import com.trackmycar.trackmycarbackend.dto.LoginResponseDTO;
import com.trackmycar.trackmycarbackend.dto.RegistrationDTO;
import com.trackmycar.trackmycarbackend.entity.AppUser;
import com.trackmycar.trackmycarbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/auth")
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path="/register")
    public AppUser registerUser(@RequestBody RegistrationDTO body) {
        return authenticationService.registerUser(
                body.getUsername(),
                body.getPassword(),
                body.getEmail(),
                body.getName()
        );
    }

    @PostMapping(path="/login")
    public LoginResponseDTO loginUser(@RequestBody LoginDTO body) {
        return authenticationService.loginUser(
                body.getUsername(),
                body.getPassword()
        );
    }
}
