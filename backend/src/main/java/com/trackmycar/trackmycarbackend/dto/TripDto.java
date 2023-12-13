package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.model.Trip;

import java.time.LocalDateTime;

public class TripDto {
    private Integer tripId;
    private String name;
    private Double totalDistance;
    private Double maxSpeed;
    private Double avgSpeed;

    public TripDto() {
    }

    public TripDto(Integer tripId,
                   String name,
                   LocalDateTime startTimestamp,
                   LocalDateTime endTimestamp,
                   Double totalDistance,
                   Double maxSpeed,
                   Double avgSpeed) {
        this.tripId = tripId;
        this.name = name;
        this.totalDistance = totalDistance;
        this.maxSpeed = maxSpeed;
        this.avgSpeed = avgSpeed;
    }

    public TripDto(Trip trip) {
        this.tripId = trip.getTripId();
        this.name = trip.getName();
        this.totalDistance = trip.getTotalDistance();
        this.maxSpeed = trip.getMaxSpeed();
        this.avgSpeed = trip.getAvgSpeed();
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
