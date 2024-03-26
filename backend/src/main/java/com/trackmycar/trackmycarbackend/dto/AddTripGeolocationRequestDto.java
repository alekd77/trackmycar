package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.exception.FailedToAddTripGeolocationException;

import java.time.LocalDateTime;

public class AddTripGeolocationRequestDto {
    private String imei;
    private Integer tripId;
    private LocalDateTime timestamp;
    private Double latitude;
    private Double longitude;
    private Double speed;

    public AddTripGeolocationRequestDto() {
    }

    public AddTripGeolocationRequestDto(String imei, Integer tripId, LocalDateTime timestamp,
                                        Double latitude, Double longitude, Double speed) {
        this.imei = imei;
        this.tripId = tripId;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        try {
            this.timestamp = LocalDateTime.parse(timestamp);
        } catch (Exception e) {
            throw new FailedToAddTripGeolocationException(
                    "Invalid timestamp: " + timestamp +
                    " (expected timestamp format is: YYYY-MM-DD'T'HH:MM:SS.XXX");
        }
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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }
}