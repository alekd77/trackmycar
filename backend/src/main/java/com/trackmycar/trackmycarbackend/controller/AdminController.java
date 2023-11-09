package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.entity.AppUser;
import com.trackmycar.trackmycarbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admins")
@CrossOrigin("*")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String welcomeMessage() {
        return "Admin access level";
    }

    @GetMapping(path="/users")
    public List<AppUser> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path="/users/{userId}")
    public AppUser getUser(@PathVariable("userId") Integer userId) {
        return userService.getUserById(userId);
    }
}