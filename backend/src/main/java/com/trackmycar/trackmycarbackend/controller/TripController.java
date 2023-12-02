package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.TripDto;
import com.trackmycar.trackmycarbackend.dto.TripGeolocationDto;
import com.trackmycar.trackmycarbackend.dto.TripWithGeolocationsDto;
import com.trackmycar.trackmycarbackend.model.*;
import com.trackmycar.trackmycarbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/trips")
@CrossOrigin("*")
public class TripController {
    private final TokenService tokenService;
    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final TripService tripService;
    private final VehicleTrackerAssignmentService assignmentService;
    private final VehicleService vehicleService;
    private final TrackerService trackerService;

    @Autowired
    public TripController(TokenService tokenService,
                          AuthorizationService authorizationService,
                          UserService userService,
                          TripService tripService,
                          VehicleTrackerAssignmentService assignmentService,
                          VehicleService vehicleService,
                          TrackerService trackerService) {
        this.tokenService = tokenService;
        this.authorizationService = authorizationService;
        this.userService = userService;
        this.tripService = tripService;
        this.assignmentService = assignmentService;
        this.vehicleService = vehicleService;
        this.trackerService = trackerService;
    }

    @GetMapping
    public Set<TripDto> getAllUserTrips(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Set<Trip> trips = tripService.getAllTripsByOwner(user);

        return trips
                .stream()
                .map(TripDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping(path="/{tripId}")
    public TripDto getTripById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                               @PathVariable("tripId") Integer tripId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Trip trip = tripService.getTripById(tripId);

        authorizationService.checkResourceAccessAuthorization(trip, user, Trip::getOwner);

        return new TripDto(trip);
    }

    @DeleteMapping(path="/{tripId}")
    public ResponseEntity<String> deleteTrip(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @PathVariable("tripId") Integer tripId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Trip trip = tripService.getTripById(tripId);

        authorizationService.checkResourceAccessAuthorization(trip, user, Trip::getOwner);

        tripService.deleteTrip(trip);

        return new ResponseEntity<String>("Trip has been deleted", HttpStatus.OK);
    }

    @GetMapping(path="/by-vehicle/{vehicleId}")
    public Set<TripDto> getAllUserTripsByVehicle(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @PathVariable("vehicleId") Integer vehicleId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

        authorizationService.checkResourceAccessAuthorization(vehicle, user, Vehicle::getOwner);

        Set<Trip> trips = tripService.getAllTripsByVehicle(vehicle);

        return trips
                .stream()
                .map(TripDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping(path="/by-tracker/{trackerId}")
    public Set<TripDto> getAllUserTripsByTracker(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                 @PathVariable("trackerId") Integer trackerId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Tracker tracker = trackerService.getTrackerById(trackerId);

        authorizationService.checkResourceAccessAuthorization(tracker, user, Tracker::getOwner);

        Set<Trip> trips = tripService.getAllTripsByTracker(tracker);

        return trips
                .stream()
                .map(TripDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping(path="/{tripId}/geolocations")
    public TripWithGeolocationsDto getTripGeolocations(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                       @PathVariable("tripId") Integer tripId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Trip trip = tripService.getTripById(tripId);

        authorizationService.checkResourceAccessAuthorization(trip, user, Trip::getOwner);

        TripDto tripDto = new TripDto(trip);

        List<TripGeolocationDto> geolocationsDtoList =
                tripService.getTripGeolocations(trip)
                .stream()
                .map(TripGeolocationDto::new)
                .toList();

        return new TripWithGeolocationsDto(tripDto, geolocationsDtoList);
    }
}
