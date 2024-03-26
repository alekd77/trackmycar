package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.exception.*;
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
    private final GeolocationService geolocationService;

    @Autowired
    public TripService(TripRepository tripRepository,
                       TripGeolocationRepository tripGeolocationRepository,
                       GeolocationService geolocationService) {
        this.tripRepository = tripRepository;
        this.tripGeolocationRepository = tripGeolocationRepository;
        this.geolocationService = geolocationService;
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

        boolean hasActiveTrip = tripRepository
                .existsByAssignmentAndIsTripEndedFalse(assignment);
        if (hasActiveTrip) {
            throw new TripRegistrationException("The assignment already has an active trip");
        }

        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String tempName = "New Trip " + date;

        int tempNameCounter = 0;
        int maxAttempts = 1000;

        do {
            String candidateName = tempNameCounter > 0
                    ? tempName + " (" + tempNameCounter + ")"
                    : tempName;

            if (!tripRepository.existsByName(candidateName)) {
                tempName = candidateName;
                break;
            }

            ++tempNameCounter;
        } while (tempNameCounter <= maxAttempts);

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
    public Trip finishCurrentTrip(VehicleTrackerAssignment assignment) {
        if (!assignment.isAssignmentActive()) {
            throw new FailedToFinishCurrentTripException("Provided assignment is not active");
        }

        Optional<Trip> currentTrip = tripRepository.findByAssignmentAndIsTripEndedFalse(assignment);

        if (currentTrip.isEmpty()) {
            throw new FailedToFinishCurrentTripException("Provided assignment does not have active trip");
        }

        try {
            currentTrip.get().setTripEnded(true);
            return tripRepository.save(currentTrip.get());
        } catch (Exception e) {
            throw new FailedToFinishCurrentTripException();
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
            TripGeolocation lastGeolocation = tripGeolocationRepository
                    .findFirstByTripOrderByTimestampDesc(trip).orElse(null);

            TripGeolocation geolocation = new TripGeolocation();
            geolocation.setTrip(trip);
            geolocation.setTimestamp(timestamp);
            geolocation.setLatitude(latitude);
            geolocation.setLongitude(longitude);
            geolocation.setSpeed(speed);
            tripGeolocationRepository.save(geolocation);

            trip.getGeolocations().add(geolocation);

            if (lastGeolocation != null) {
                System.out.println("\nLast geolocation ID: " + lastGeolocation.getTripGeolocationId() + "\n");

                Double newTotalDistance = calculateNewTotalDistance(
                        trip.getTotalDistance(),
                        lastGeolocation.getLatitude(),
                        lastGeolocation.getLongitude(),
                        latitude,
                        longitude);
                trip.setTotalDistance(newTotalDistance);
            }

            Double newAvgSpeed = trip.getAvgSpeed() > 0.0
                    ? (trip.getAvgSpeed() + speed) / 2
                    : speed;
            trip.setAvgSpeed(newAvgSpeed);

            Double newSpeed = speed > trip.getMaxSpeed()
                    ? speed
                    : trip.getMaxSpeed();
            trip.setMaxSpeed(newSpeed);

            tripRepository.save(trip);
        } catch (Exception e) {
            throw new FailedToAddTripGeolocationException();
        }
    }


    @Transactional
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

    private Double calculateNewTotalDistance(Double oldTotalDistance,
                                             Double oldPosLat,
                                             Double oldPosLon,
                                             Double newPosLat,
                                             Double newPosLon) {
        Double traversedDistance = geolocationService.calculateDistance(
                oldPosLat, oldPosLon, newPosLat, newPosLon);

        return oldTotalDistance + traversedDistance;
    }

    public TripGeolocation getTripGeolocationById(Integer geolocationId) {
        return tripGeolocationRepository
                .findById(geolocationId)
                .orElseThrow(() -> new TripGeolocationNotFoundException(
                        "TripGeolocation of given ID: " + geolocationId + " not found"
                ));
    }

    @Transactional
    public void deleteTripGeolocation(TripGeolocation geolocation) {
        try {
            tripGeolocationRepository.delete(geolocation);
        } catch (Exception e) {
            throw new FailedToDeleteTripException();
        }
    }
}
