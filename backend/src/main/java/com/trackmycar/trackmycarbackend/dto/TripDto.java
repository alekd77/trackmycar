package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.model.Trip;

import java.time.LocalDateTime;

public class TripDto {
    private Integer tripId;
    private String name;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
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
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.totalDistance = totalDistance;
        this.maxSpeed = maxSpeed;
        this.avgSpeed = avgSpeed;
    }

    public TripDto(Trip trip) {
        this.tripId = trip.getTripId();
        this.name = trip.getName();
        this.startTimestamp = trip.getStartTimestamp();
        this.endTimestamp = trip.getEndTimestamp();
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

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
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
