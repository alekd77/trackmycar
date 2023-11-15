package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.model.Vehicle;

public class VehicleDto {
    private Integer vehicleId;
    private AppUserDto owner;
    private String name;
    private String description;
    private String markerHexColor;

    public VehicleDto() {
    }

    public VehicleDto(Integer vehicleId,
                      AppUserDto owner,
                      String name,
                      String description,
                      String markerHexColor) {
        this.vehicleId = vehicleId;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.markerHexColor = markerHexColor;
    }

    public VehicleDto(Vehicle vehicle) {
        this.vehicleId = vehicle.getVehicleId();
        this.owner = new AppUserDto(vehicle.getOwner());
        this.name = vehicle.getName();
        this.description = vehicle.getDescription();
        this.markerHexColor = vehicle.getMarkerHexColor();
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public AppUserDto getOwner() {
        return owner;
    }

    public void setOwner(AppUserDto owner) {
        this.owner = owner;
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

    @Override
    public String toString() {
        return "VehicleDTO{" +
                "vehicleId=" + vehicleId +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", markerHexColor='" + markerHexColor + '\'' +
                '}';
    }
}
