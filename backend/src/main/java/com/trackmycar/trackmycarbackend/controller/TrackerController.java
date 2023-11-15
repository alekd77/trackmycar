package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.ApiExceptionDto;
import com.trackmycar.trackmycarbackend.dto.TrackerDto;
import com.trackmycar.trackmycarbackend.exception.*;
import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Tracker;
import com.trackmycar.trackmycarbackend.service.TokenService;
import com.trackmycar.trackmycarbackend.service.TrackerService;
import com.trackmycar.trackmycarbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/trackers")
@CrossOrigin("*")
public class TrackerController {
    private final TokenService tokenService;
    private final UserService userService;
    private final TrackerService trackerService;

    @Autowired
    public TrackerController(TokenService tokenService,
                             UserService userService,
                             TrackerService trackerService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.trackerService = trackerService;
    }

    @GetMapping
    public Set<TrackerDto> getUserTrackers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Set<Tracker> trackers = trackerService.getAllTrackersByOwner(user);

        return trackers
                .stream()
                .map(TrackerDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping(path="/{trackerId}")
    public TrackerDto getTrackerById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @PathVariable("trackerId") Integer trackerId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Tracker tracker = trackerService.getTrackerById(trackerId);

        // TODO: Fix resource access authorization
        // Simple authorization checking based on the ownership of the vehicle
        if (!tracker.getOwner().getUsername().equals(user.getUsername())) {
            throw new AuthorizationFailedException();
        }

        return new TrackerDto(tracker);
    }

    @PostMapping
    public TrackerDto addNewTracker(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                    @RequestBody TrackerDto body) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);

        Tracker tracker = trackerService.addTracker(
                user,
                body.getName(),
                body.getDescription(),
                body.getImei()
        );

        return new TrackerDto(tracker);
    }

    @PutMapping(path="/{trackerId}")
    public TrackerDto updateTracker(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                    @PathVariable("trackerId") Integer trackerId,
                                    @RequestBody TrackerDto body) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Tracker tracker = trackerService.getTrackerById(trackerId);

        // TODO: Fix resource access authorization
        // Simple authorization checking based on the ownership of the vehicle
        if (!tracker.getOwner().getUsername().equals(user.getUsername())) {
            throw new AuthorizationFailedException();
        }

        Tracker updatedTracker = trackerService.updateTracker(
                tracker,
                body.getName(),
                body.getDescription(),
                body.getImei()
        );

        return new TrackerDto(updatedTracker);
    }

    @DeleteMapping(path="/{trackerId}")
    public ResponseEntity<String> deleteTracker(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                @PathVariable("trackerId") Integer trackerId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Tracker tracker = trackerService.getTrackerById(trackerId);

        // TODO: Fix resource access authorization
        // Simple authorization checking based on the ownership of the vehicle
        if (!tracker.getOwner().getUsername().equals(user.getUsername())) {
            throw new AuthorizationFailedException();
        }

        trackerService.deleteTracker(tracker);

        return new ResponseEntity<>("Tracker has been deleted", HttpStatus.OK);
    }

    @ExceptionHandler({AuthorizationFailedException.class})
    public ResponseEntity<ApiExceptionDto> handleAuthorizationFailed(AuthorizationFailedException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({TrackerRegistrationFailedException.class})
    public ResponseEntity<ApiExceptionDto> handleTrackerRegistrationFailed(TrackerRegistrationFailedException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({TrackerNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleTrackerNotFound(TrackerNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({InvalidInputException.class})
    public ResponseEntity<ApiExceptionDto> handleInvalidInput(InvalidInputException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }
}
