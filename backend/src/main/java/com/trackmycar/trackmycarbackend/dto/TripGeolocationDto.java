package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.model.TripGeolocation;

import java.time.LocalDateTime;

public class TripGeolocationDto {
    private Integer tripGeolocationId;
    private Integer tripId;
    private LocalDateTime timestamp;
    private Double latitude;
    private Double longitude;
    private Double speed;

    public TripGeolocationDto() {
    }

    public TripGeolocationDto(Integer tripGeolocationId,
                              Integer tripId,
                              LocalDateTime timestamp,
                              Double latitude,
                              Double longitude,
                              Double speed) {
        this.tripGeolocationId = tripGeolocationId;
        this.tripId = tripId;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public TripGeolocationDto(TripGeolocation geolocation) {
        this.tripGeolocationId = geolocation.getTripGeolocationId();
        this.tripId = geolocation.getTrip().getTripId();
        this.timestamp = geolocation.getTimestamp();
        this.latitude = geolocation.getLatitude();
        this.longitude = geolocation.getLongitude();
        this.speed = geolocation.getSpeed();
    }

    public Integer getTripGeolocationId() {
        return tripGeolocationId;
    }

    public void setTripGeolocationId(Integer tripGeolocationId) {
        this.tripGeolocationId = tripGeolocationId;
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
