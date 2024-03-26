package com.trackmycar.trackmycarbackend.dto;

import java.time.LocalDateTime;

public class VehicleLastPositionDto {
    private Integer ownerId;
    private Integer vehicleId;
    private LocalDateTime timestamp;
    private Double latitude;
    private Double longitude;

    public VehicleLastPositionDto() {
    }

    public VehicleLastPositionDto(Integer ownerId,
                                  Integer vehicleId,
                                  LocalDateTime timestamp,
                                  Double latitude,
                                  Double longitude) {
        this.ownerId = ownerId;
        this.vehicleId = vehicleId;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
