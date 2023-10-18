package com.trackmycar.trackmycarbackend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/home")
@CrossOrigin("*")
public class HomeController {

    @GetMapping
    public String welcomeMessage() {
        return "Welcome to TrackMyCar!";
    }
}
