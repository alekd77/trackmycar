package com.trackmycar.trackmycarbackend.dto;

public class LoginResponseDto {
    private AppUserDto appUser;
    private String jwt;

    public LoginResponseDto() {
        super();
    }

    public LoginResponseDto(AppUserDto appUser, String jwt) {
        this.appUser = appUser;
        this.jwt = jwt;
    }

    public AppUserDto getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDto appUser) {
        this.appUser = appUser;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
