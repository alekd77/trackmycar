package com.trackmycar.trackmycarbackend.dto;

public class VehicleRegistrationDto {
    private String name;
    private String description;
    private String markerHexColor;

    public VehicleRegistrationDto() {
    }

    public VehicleRegistrationDto(String name,
                                  String description,
                                  String markerHexColor) {
        this.name = name;
        this.description = description;
        this.markerHexColor = markerHexColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMarkerHexColor() {
        return markerHexColor;
    }

    public void setMarkerHexColor(String markerHexColor) {
        this.markerHexColor = markerHexColor;
    }
}
