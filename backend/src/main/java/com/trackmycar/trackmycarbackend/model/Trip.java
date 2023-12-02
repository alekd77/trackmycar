package com.trackmycar.trackmycarbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="trips")
public class Trip {
    @Id
    @SequenceGenerator(
            name = "trips_sequence",
            sequenceName = "trips_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trips_sequence"
    )
    @Column(name="trip_id")
    private Integer tripId;

    @ManyToOne
    @JoinColumn(name="vehicle_tracker_assignment_id", nullable = false)
    private VehicleTrackerAssignment assignment;

    @OneToMany(mappedBy="trip")
    private List<TripGeolocation> geolocations;

    private String name;

    @Column(name="total_distance")
    private Double totalDistance;

    @Column(name="max_speed")
    private Double maxSpeed;

    @Column(name="avg_speed")
    private Double avgSpeed;

    public Trip() {
        super();
        this.geolocations = new ArrayList<>();
    }

    public Trip(Integer tripId,
                VehicleTrackerAssignment assignment,
                List<TripGeolocation> geolocations,
                String name,
                Double totalDistance,
                Double maxSpeed,
                Double avgSpeed) {
        super();
        this.tripId = tripId;
        this.assignment = assignment;
        this.geolocations = geolocations;
        this.name = name;
        this.totalDistance = totalDistance;
        this.maxSpeed = maxSpeed;
        this.avgSpeed = avgSpeed;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public VehicleTrackerAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(VehicleTrackerAssignment assignment) {
        this.assignment = assignment;
    }

    public List<TripGeolocation> getGeolocations() {
        return geolocations;
    }

    public void setGeolocations(List<TripGeolocation> geolocations) {
        this.geolocations = geolocations;
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

    public AppUser getOwner() {
        return assignment != null ? assignment.getOwner() : null;
    }
}
