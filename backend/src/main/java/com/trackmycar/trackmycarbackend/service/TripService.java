package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.exception.FailedToAddTripGeolocationException;
import com.trackmycar.trackmycarbackend.exception.FailedToDeleteTripException;
import com.trackmycar.trackmycarbackend.exception.TripNotFoundException;
import com.trackmycar.trackmycarbackend.exception.TripRegistrationException;
import com.trackmycar.trackmycarbackend.model.*;
import com.trackmycar.trackmycarbackend.repository.TripGeolocationRepository;
import com.trackmycar.trackmycarbackend.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final TripGeolocationRepository tripGeolocationRepository;

    @Autowired
    public TripService(TripRepository tripRepository, TripGeolocationRepository tripGeolocationRepository) {
        this.tripRepository = tripRepository;
        this.tripGeolocationRepository = tripGeolocationRepository;
    }

    public Set<Trip> getAllTripsByOwner(AppUser owner) {
        return tripRepository
                .findAllByAssignmentOwner(owner)
                .orElse(new HashSet<>());
    }

    public Trip getTripById(Integer tripId) {
        return tripRepository
                .findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(
                        "Trip of given ID: " + tripId + " not found"
                ));
    }

    @Transactional
    public Trip startNewTrip(VehicleTrackerAssignment assignment) {
        if (!assignment.isAssignmentActive()) {
            throw new TripRegistrationException("Provided assignment is not active");
        }

        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        StringBuilder tempName = new StringBuilder("New Trip ").append(date);

        int tempNameCounter = 1;
        int maxAttempts = 1000;

        while (tempNameCounter <= maxAttempts) {
            String candidateName = (tempNameCounter > 1)
                    ? tempName.append(" (").append(tempNameCounter).append(")").toString()
                    : tempName.toString();

            if (!tripRepository.existsByName(candidateName)) {
                tempName = new StringBuilder(candidateName);
                break;
            }

            ++tempNameCounter;
        }

        if (tempNameCounter > maxAttempts) {
            throw new TripRegistrationException("Failed to generate a valid trip name");
        }

        Trip trip = new Trip();
        trip.setAssignment(assignment);
        trip.setName(tempName.toString());

        try {
            return tripRepository.save(trip);
        } catch (Exception e) {
            throw new TripRegistrationException("Failed to start new trip");
        }
    }

    @Transactional
    public void addTripGeolocation(Trip trip, LocalDateTime timestamp, Double latitude,
                                   Double longitude, Double speed) {
        // Validate the geolocation data
        if (timestamp == null || latitude == null || longitude == null || speed == null || speed < 0) {
            throw new FailedToAddTripGeolocationException("Invalid TripGeolocation data");
        }

        // Validate latitude
        if (latitude < -90 || latitude > 90) {
            throw new FailedToAddTripGeolocationException("Latitude must be between -90 and 90 degrees");
        }

        // Validate longitude
        if (longitude < -180 || longitude > 180) {
            throw new FailedToAddTripGeolocationException("Longitude must be between -180 and 180 degrees");
        }

        try {
            TripGeolocation geolocation = new TripGeolocation();
            geolocation.setTrip(trip);
            geolocation.setTimestamp(timestamp);
            geolocation.setLatitude(latitude);
            geolocation.setLongitude(longitude);
            geolocation.setSpeed(speed);
            tripGeolocationRepository.save(geolocation);

            trip.getGeolocations().add(geolocation);
            tripRepository.save(trip);
        } catch (Exception e) {
            throw new FailedToAddTripGeolocationException();
        }
    }


    public void deleteTrip(Trip trip) {
        VehicleTrackerAssignment assignment = trip.getAssignment();
        Tracker tracker = assignment.getTracker();

        if (tracker.getStatus() == TrackerStatus.TRACKING_ACTIVE
                && assignment.isAssignmentActive()) {
            throw new FailedToDeleteTripException(
                    "The trip cannot be deleted as it is still in progress." +
                    " Stop active tracking before attempting to delete the trip."
            );
        }

        try {
            tripRepository.delete(trip);
        } catch (Exception e) {
            throw new FailedToDeleteTripException();
        }
    }
}
