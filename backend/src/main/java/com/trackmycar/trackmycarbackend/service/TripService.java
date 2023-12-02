package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.exception.FailedToDeleteTripException;
import com.trackmycar.trackmycarbackend.exception.TripNotFoundException;
import com.trackmycar.trackmycarbackend.exception.TripRegistrationException;
import com.trackmycar.trackmycarbackend.model.*;
import com.trackmycar.trackmycarbackend.repository.TripRepository;
import com.trackmycar.trackmycarbackend.repository.VehicleTrackerAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final VehicleTrackerAssignmentRepository assignmentRepository;

    @Autowired
    public TripService(TripRepository tripRepository, VehicleTrackerAssignmentRepository assignmentRepository) {
        this.tripRepository = tripRepository;
        this.assignmentRepository = assignmentRepository;
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

    // addCustomTrip()

    // startTrip -> method called by tracker

    public Trip startNewTrip(VehicleTrackerAssignment assignment) {
        if (!assignment.isAssignmentActive()) {
            throw new TripRegistrationException("Provided assignment is not active");
        }

        boolean isTempNameValid = false;
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        StringBuilder tempName = new StringBuilder("New Trip ");
        tempName.append(date);

        int tempNameCounter = 1;
        while (!isTempNameValid) {
            if (!tripRepository.existsByName(String.valueOf(tempName))) {
                isTempNameValid = true;
            } else {
                ++tempNameCounter;
            }
        }
        tempName.append("( ").append(tempNameCounter).append(" )");

        Trip trip = new Trip();
        trip.setAssignment(assignment);
        trip.setName(String.valueOf(tempName));

        return trip;
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

        tripRepository.delete(trip);
    }

    public Set<Trip> getAllTripsByVehicle(Vehicle vehicle) {
        return new HashSet<>();
    }

    public Set<Trip> getAllTripsByTracker(Tracker tracker) {
        return new HashSet<>();
    }

    public List<TripGeolocation> getTripGeolocations(Trip trip) {
        return new ArrayList<>();
    }
}
