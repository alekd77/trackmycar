package com.trackmycar.trackmycarbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="trip_geolocations")
public class TripGeolocation {
    @Id
    @SequenceGenerator(
            name = "trip_geolocations_sequence",
            sequenceName = "trip_geolocations_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trip_geolocations_sequence"
    )
    @Column(name="trip_geolocation_id")
    private Integer tripGeolocationId;

    @ManyToOne
    @JoinColumn(name="trip_id", nullable = false)
    private Trip trip;

    private LocalDateTime timestamp;

    private Double latitude;

    private Double longitude;

    private Double speed;

    public TripGeolocation() {
    }

    public TripGeolocation(Integer tripGeolocationId,
                           Trip trip,
                           LocalDateTime timestamp,
                           Double latitude,
                           Double longitude,
                           Double speed) {
        this.tripGeolocationId = tripGeolocationId;
        this.trip = trip;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public Integer getTripGeolocationId() {
        return tripGeolocationId;
    }

    public void setTripGeolocationId(Integer tripGeolocationId) {
        this.tripGeolocationId = tripGeolocationId;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
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
