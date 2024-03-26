package com.trackmycar.trackmycarbackend.dto;

public class StartNewTripRequestDto {
    private String imei;

    public StartNewTripRequestDto() {
    }

    public StartNewTripRequestDto(String imei) {
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
