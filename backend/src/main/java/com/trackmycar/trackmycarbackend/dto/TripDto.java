package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.model.Tracker;
import com.trackmycar.trackmycarbackend.model.Trip;
import com.trackmycar.trackmycarbackend.model.Vehicle;

import java.time.LocalDateTime;

public class TripDto {
    private Integer tripId;
    private VehicleDto vehicle;
    private TrackerDto tracker;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private String name;
    private Double totalDistance;
    private Double maxSpeed;
    private Double avgSpeed;
    private Boolean isEnded;

    public TripDto() {
        super();
    }

    public TripDto(Integer tripId,
                   Vehicle vehicle,
                   Tracker tracker,
                   LocalDateTime startTimestamp,
                   LocalDateTime endTimestamp,
                   String name,
                   Double totalDistance,
                   Double maxSpeed,
                   Double avgSpeed,
                   Boolean isEnded) {
        super();
        this.tripId = tripId;
        this.vehicle = new VehicleDto(vehicle);
        this.tracker = new TrackerDto(tracker);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.name = name;
        this.totalDistance = totalDistance;
        this.maxSpeed = maxSpeed;
        this.avgSpeed = avgSpeed;
        this.isEnded = isEnded;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public VehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
    }

    public TrackerDto getTracker() {
        return tracker;
    }

    public void setTracker(TrackerDto tracker) {
        this.tracker = tracker;
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

    public Boolean getEnded() {
        return isEnded;
    }

    public void setEnded(Boolean ended) {
        isEnded = ended;
    }
}
