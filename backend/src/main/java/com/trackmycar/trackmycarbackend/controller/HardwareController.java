package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.exception.HardwareTrackerIllegalStateException;
import com.trackmycar.trackmycarbackend.model.*;
import com.trackmycar.trackmycarbackend.service.TrackerService;
import com.trackmycar.trackmycarbackend.service.TripService;
import com.trackmycar.trackmycarbackend.service.VehicleTrackerAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/hardware")
@CrossOrigin("*")
public class HardwareController {
    private final TrackerService trackerService;
    private final VehicleTrackerAssignmentService assignmentService;
    private final TripService tripService;

    public HardwareController(TrackerService trackerService,
                              VehicleTrackerAssignmentService assignmentService,
                              TripService tripService) {
        this.trackerService = trackerService;
        this.assignmentService = assignmentService;
        this.tripService = tripService;
    }

    @PutMapping(path="/last-position")
    public void updateLastPosition(@RequestParam String imei,
                                   @RequestParam String timestamp,
                                   @RequestParam Double latitude,
                                   @RequestParam Double longitude) {
        Tracker tracker = trackerService.getTrackerByImei(imei);
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByTracker(tracker);
        LocalDateTime fixedTimestamp = LocalDateTime.now();

        assignmentService.updateLastPosition(
                assignment,
                fixedTimestamp,
                latitude,
                longitude
        );
    }

    @PostMapping(path="/trips/{}")

    @PostMapping(path="/send-current-position")
    public ResponseEntity<String> sendCurrentPosition(@RequestParam(name="trackerImei") String imei,
                                                      @RequestParam(required = false) Integer tripId,
                                                      @RequestParam(required = false) String timestamp,
                                                      @RequestParam Double latitude,
                                                      @RequestParam Double longitude,
                                                      @RequestParam(required = false) Double speed) {
        Tracker tracker = trackerService.getTrackerByImei(imei);
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByTrackerId(tracker.getTrackerId());
        LocalDateTime fixedTimestamp = timestamp != null
                ? extractTimestamp(timestamp)
                : LocalDateTime.now();

        if (tracker.getStatus() == TrackerStatus.TRACKING_PASSIVE) {
            VehicleTrackerAssignment updatedAssignment =
                    assignmentService.updateLastPosition(
                            assignment,
                            fixedTimestamp,
                            latitude,
                            longitude
            );

            return new ResponseEntity<String>(
                    "Updated last position in vehicle-tracker assignment with ID: "
                            + updatedAssignment.getVehicleTrackerAssignmentId(),
                    HttpStatus.OK
            );
        }

        if (tracker.getStatus() == TrackerStatus.TRACKING_ACTIVE) {
            TripGeolocation geolocation = new TripGeolocation();
            geolocation.setTimestamp(fixedTimestamp);
            geolocation.setLatitude(latitude);
            geolocation.setLongitude(longitude);
            geolocation.setSpeed(speed);

            Trip trip = tripService.addTripGeolocation(
                    tripService.getTripById(tripId),
                    geolocation
            );

            return new ResponseEntity<String>(
                    "Geolocation has been assigned to the trip with ID: "
                            + trip.getTripId(),
                    HttpStatus.OK
            );
        }

        throw new HardwareTrackerIllegalStateException();
    }

    private LocalDateTime extractTimestamp(String timestamp) {
        return LocalDateTime.now();
    }

    @PostMapping(path="/start-new-trip")
    public ResponseEntity<String> startNewTrip(@RequestParam(name="tracker-imei") String imei) {
        Tracker tracker = trackerService.getTrackerByImei(imei);
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByTrackerId(tracker.getTrackerId());
        Trip trip = tripService.startNewTrip(assignment);

        return new ResponseEntity<>("Started new trip with ID: " + trip.getTripId(), HttpStatus.OK);
    }



}
