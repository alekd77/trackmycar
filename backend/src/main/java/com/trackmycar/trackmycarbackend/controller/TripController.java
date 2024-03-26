package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.*;
import com.trackmycar.trackmycarbackend.exception.FailedToDeleteTripException;
import com.trackmycar.trackmycarbackend.exception.TripNotFoundException;
import com.trackmycar.trackmycarbackend.exception.TripRegistrationException;
import com.trackmycar.trackmycarbackend.exception.FailedToDeleteTripGeolocationException;
import com.trackmycar.trackmycarbackend.exception.TripGeolocationNotFoundException;
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

    @Autowired
    public TripController(TokenService tokenService,
                          AuthorizationService authorizationService,
                          UserService userService,
                          TripService tripService) {
        this.tokenService = tokenService;
        this.authorizationService = authorizationService;
        this.userService = userService;
        this.tripService = tripService;
    }

    @GetMapping
    public Set<TripDto> getAllUserTrips(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Set<Trip> trips = tripService.getAllTripsByOwner(user);

        return trips.stream()
                .map(trip -> new TripDto(
                        trip.getTripId(),
                        trip.getVehicle(),
                        trip.getTracker(),
                        trip.getStartTimestamp(),
                        trip.getEndTimestamp(),
                        trip.getName(),
                        trip.getTotalDistance(),
                        trip.getMaxSpeed(),
                        trip.getAvgSpeed(),
                        trip.isTripEnded()))
                .collect(Collectors.toSet());
    }

    @GetMapping(path="/{tripId}")
    public TripDto getTripById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                               @PathVariable("tripId") Integer tripId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Trip trip = tripService.getTripById(tripId);

        authorizationService.checkResourceAccessAuthorization(trip, user, Trip::getOwner);

        return new TripDto(
                trip.getTripId(),
                trip.getVehicle(),
                trip.getTracker(),
                trip.getStartTimestamp(),
                trip.getEndTimestamp(),
                trip.getName(),
                trip.getTotalDistance(),
                trip.getMaxSpeed(),
                trip.getAvgSpeed(),
                trip.isTripEnded());
    }

    @DeleteMapping(path="/{tripId}")
    public ResponseEntity<String> deleteTrip(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                             @PathVariable("tripId") Integer tripId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Trip trip = tripService.getTripById(tripId);

        authorizationService.checkResourceAccessAuthorization(trip, user, Trip::getOwner);

        tripService.deleteTrip(trip);

        return new ResponseEntity<>("Trip with ID: " + trip.getTripId() +
                " has been deleted", HttpStatus.OK);
    }

    @GetMapping(path="/{tripId}/geolocations")
    public TripWithGeolocationsDto getTripGeolocations(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                       @PathVariable("tripId") Integer tripId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Trip trip = tripService.getTripById(tripId);

        authorizationService.checkResourceAccessAuthorization(trip, user, Trip::getOwner);

        TripDto tripDto = new TripDto(
                trip.getTripId(),
                trip.getVehicle(),
                trip.getTracker(),
                trip.getStartTimestamp(),
                trip.getEndTimestamp(),
                trip.getName(),
                trip.getTotalDistance(),
                trip.getMaxSpeed(),
                trip.getAvgSpeed(),
                trip.isTripEnded());

        List<TripGeolocationDto> geolocationsDto =
                trip.getGeolocations()
                .stream()
                .map(TripGeolocationDto::new)
                .toList();

        return new TripWithGeolocationsDto(tripDto, geolocationsDto);
    }

    @GetMapping(path="/{tripId}/geolocations/{geolocationId}")
    public ResponseEntity<String> deleteTripGeolocation(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         @PathVariable("tripId") Integer tripId,
                                                         @PathVariable("geolocationId") Integer geolocationId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Trip trip = tripService.getTripById(tripId);

        authorizationService.checkResourceAccessAuthorization(trip, user, Trip::getOwner);

        TripGeolocation tripGeolocation = tripService.getTripGeolocationById(geolocationId);

        tripService.deleteTripGeolocation(tripGeolocation);

        return new ResponseEntity<>("TripGeolocation with ID: " + trip.getTripId() +
                " has been deleted", HttpStatus.OK);
    }

    @ExceptionHandler({TripNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleTripNotFound(TripNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({TripRegistrationException.class})
    public ResponseEntity<ApiExceptionDto> handleTripRegistrationException(TripRegistrationException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({FailedToDeleteTripException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToDeleteTrip(FailedToDeleteTripException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({TripGeolocationNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleTripGeolocationNotFound(TripGeolocationNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({FailedToDeleteTripGeolocationException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToDeleteTripGeolocation(FailedToDeleteTripGeolocationException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }
}
