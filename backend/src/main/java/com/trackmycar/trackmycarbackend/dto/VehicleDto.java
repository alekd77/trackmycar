package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.model.Vehicle;

public class VehicleDto {
    private Integer vehicleId;
    private Integer ownerId;
    private String name;
    private String description;
    private String markerHexColor;

    public VehicleDto() {
    }

    public VehicleDto(Integer vehicleId,
                      Integer ownerId,
                      String name,
                      String description,
                      String markerHexColor) {
        this.vehicleId = vehicleId;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.markerHexColor = markerHexColor;
    }

    public VehicleDto(Vehicle vehicle) {
        this.vehicleId = vehicle.getVehicleId();
        this.ownerId = vehicle.getOwner().getUserId();
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

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
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
                ", ownerId=" + ownerId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", markerHexColor='" + markerHexColor + '\'' +
                '}';
    }
}
