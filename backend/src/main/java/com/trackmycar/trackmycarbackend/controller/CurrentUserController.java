package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.AppUserDto;
import com.trackmycar.trackmycarbackend.entity.AppUser;
import com.trackmycar.trackmycarbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import com.trackmycar.trackmycarbackend.service.TokenService;

@RestController
@RequestMapping(path = "/users/current")
@CrossOrigin("*")
public class CurrentUserController {
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public CurrentUserController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @GetMapping
    public String welcomeMessage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = tokenService.getUsernameFromToken(token);

        return "User access level\n\nHello, " + username;
    }

    @GetMapping(path="/details")
    public AppUserDto getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);

        return new AppUserDto(user);
    }
}
