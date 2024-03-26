package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.AddTripGeolocationRequestDto;
import com.trackmycar.trackmycarbackend.dto.ApiExceptionDto;
import com.trackmycar.trackmycarbackend.dto.LastPosUpdateDataDto;
import com.trackmycar.trackmycarbackend.dto.StartNewTripRequestDto;
import com.trackmycar.trackmycarbackend.exception.*;
import com.trackmycar.trackmycarbackend.model.*;
import com.trackmycar.trackmycarbackend.service.TrackerService;
import com.trackmycar.trackmycarbackend.service.TripService;
import com.trackmycar.trackmycarbackend.service.VehicleTrackerAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path="/trackers/send-alert")
    public ResponseEntity<String> sendAlert() {
        return new ResponseEntity<String>("Not implemented yet", HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping(path="/trackers/last-pos")
    public ResponseEntity<String> updateLastPosition(@RequestBody LastPosUpdateDataDto body) {
        Tracker tracker = trackerService.getTrackerByImei(body.getImei());
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByTracker(tracker);

        assignmentService.updateLastPosition(
                assignment,
                body.getTimestamp(),
                body.getLatitude(),
                body.getLongitude()
        );

        return new ResponseEntity<>("Successfully updated last pos of assignment with ID: " +
                assignment.getVehicleTrackerAssignmentId(), HttpStatus.OK);
    }

    @PostMapping(path="/trackers/start-new-trip")
    public ResponseEntity<String> startNewTrip(@RequestBody StartNewTripRequestDto body) {
        Tracker tracker = trackerService.getTrackerByImei(body.getImei());
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByTrackerId(tracker.getTrackerId());
        Trip trip = tripService.startNewTrip(assignment);

        return new ResponseEntity<>("Started new trip with ID: " + trip.getTripId(), HttpStatus.OK);
    }

    @PostMapping(path="/trackers/{trackerId}/finish-current-trip")
    public ResponseEntity<String> finishCurrentTrip(@PathVariable("trackerId") Integer trackerId) {
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByTrackerId(trackerId);
        Trip trip = tripService.finishCurrentTrip(assignment);

        return new ResponseEntity<>("Trip with ID: " + trip.getTripId() + " has been successfully finished", HttpStatus.OK);
    }

    @PostMapping(path="/trackers/add-trip-geolocation")
    public ResponseEntity<String> addTripGeolocation(@RequestBody AddTripGeolocationRequestDto body) {
        Tracker tracker = trackerService.getTrackerByImei(body.getImei());
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByTracker(tracker);
        Trip trip = tripService.getTripById(body.getTripId());

        if (!trip.getAssignment().equals(assignment)) {
            throw new FailedToAddTripGeolocationException("Trip is not assigned to the tracker");
        }

        assignmentService.updateLastPosition(
                assignment,
                body.getTimestamp(),
                body.getLatitude(),
                body.getLongitude()
        );

        tripService.addTripGeolocation(
                trip,
                body.getTimestamp(),
                body.getLatitude(),
                body.getLongitude(),
                body.getSpeed()
        );

        return new ResponseEntity<>("Successfully added geolocation to trip with ID: " +
                trip.getTripId(), HttpStatus.OK);
    }

    @ExceptionHandler({HardwareDataProcessingFailureException.class})
    public ResponseEntity<ApiExceptionDto> handleHardwareDataProcessingFailure(
            HardwareDataProcessingFailureException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({FailedToAddTripGeolocationException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToAddTripGeolocation(FailedToAddTripGeolocationException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({TrackerNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleTrackerNotFound(TrackerNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({TripNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleTripNotFound(TripNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({TripRegistrationException.class})
    public ResponseEntity<ApiExceptionDto> handleTripRegistrationException(TripRegistrationException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({FailedToFinishCurrentTripException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToFinishCurrentTripException(FailedToFinishCurrentTripException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({VehicleTrackerAssignmentNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleVehicleTrackerAssignmentNotFound(
            VehicleTrackerAssignmentNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }
}
