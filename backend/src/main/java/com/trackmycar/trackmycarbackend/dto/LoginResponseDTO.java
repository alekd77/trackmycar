package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.entity.AppUser;

public class LoginResponseDTO {
    private AppUser appUser;
    private String jwt;

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(AppUser appUser, String jwt) {
        this.appUser = appUser;
        this.jwt = jwt;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
