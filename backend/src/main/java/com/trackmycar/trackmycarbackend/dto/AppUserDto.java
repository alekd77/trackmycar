package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.entity.AppUser;

public class AppUserDto {
    private Integer userId;
    private String username;
    private String email;
    private String name;

    public AppUserDto() {
    }

    public AppUserDto(Integer userId,
                      String username,
                      String email,
                      String name) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public AppUserDto(AppUser user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
