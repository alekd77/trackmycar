package com.trackmycar.trackmycarbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    @GetMapping
    public String welcomeMessage() {
        return "User access level";
    }
}
