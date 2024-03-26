package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.ApiExceptionDto;
import com.trackmycar.trackmycarbackend.dto.VehicleTrackerAssignmentDto;
import com.trackmycar.trackmycarbackend.exception.*;
import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.VehicleTrackerAssignment;
import com.trackmycar.trackmycarbackend.service.AuthorizationService;
import com.trackmycar.trackmycarbackend.service.TokenService;
import com.trackmycar.trackmycarbackend.service.UserService;
import com.trackmycar.trackmycarbackend.service.VehicleTrackerAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/assignments")
@CrossOrigin("*")
public class VehicleTrackerAssignmentController {
    private final TokenService tokenService;
    private final UserService userService;
    private final VehicleTrackerAssignmentService assignmentService;
    private final AuthorizationService authorizationService;

    @Autowired
    public VehicleTrackerAssignmentController(TokenService tokenService,
                                              UserService userService,
                                              VehicleTrackerAssignmentService assignmentService,
                                              AuthorizationService authorizationService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.assignmentService = assignmentService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public Set<VehicleTrackerAssignmentDto> getAllUserAssignments(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Set<VehicleTrackerAssignment> assignments =
                assignmentService.getAllAssignmentsByOwner(user);

        return assignments
                .stream()
                .map(VehicleTrackerAssignmentDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping(path="/{assignmentId}")
    public VehicleTrackerAssignmentDto getAssignmentById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         @PathVariable("assignmentId") Integer assignmentId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        VehicleTrackerAssignment assignment = assignmentService.getAssignmentById(assignmentId);

        authorizationService.checkResourceAccessAuthorization(assignment, user, VehicleTrackerAssignment::getOwner);

        return new VehicleTrackerAssignmentDto(assignment);
    }

    @PostMapping
    public VehicleTrackerAssignmentDto addNewAssignment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                        @RequestBody VehicleTrackerAssignmentDto body) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        VehicleTrackerAssignment assignment = assignmentService.addAssignment(
                user,
                body.getVehicleId(),
                body.getTrackerId()
        );

        return new VehicleTrackerAssignmentDto(assignment);
    }

    @DeleteMapping(path="/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @PathVariable("assignmentId") Integer assignmentId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        VehicleTrackerAssignment assignment = assignmentService.getAssignmentById(assignmentId);

        authorizationService.checkResourceAccessAuthorization(assignment, user, VehicleTrackerAssignment::getOwner);

        assignmentService.deleteAssignment(assignment);

        return new ResponseEntity<String>("Assignment has been deleted", HttpStatus.OK);
    }

    @GetMapping(path="/active")
    public Set<VehicleTrackerAssignmentDto> getAllUserActiveAssignments(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Set<VehicleTrackerAssignment> assignments = assignmentService.getAllActiveAssignmentsByOwner(user);

        return assignments
                .stream()
                .map(VehicleTrackerAssignmentDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping(path="/active/by-vehicle/{vehicleId}")
    public VehicleTrackerAssignmentDto getActiveAssignmentByVehicleId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                      @PathVariable("vehicleId") Integer vehicleId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByVehicleId(vehicleId);

        authorizationService.checkResourceAccessAuthorization(assignment, user, VehicleTrackerAssignment::getOwner);

        return new VehicleTrackerAssignmentDto(assignment);
    }

    @GetMapping(path="/active/by-tracker/{trackerId}")
    public VehicleTrackerAssignmentDto getActiveAssignmentByTrackerId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                      @PathVariable("trackerId") Integer trackerId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        VehicleTrackerAssignment assignment = assignmentService.getActiveAssignmentByTrackerId(trackerId);

        authorizationService.checkResourceAccessAuthorization(assignment, user, VehicleTrackerAssignment::getOwner);

        return new VehicleTrackerAssignmentDto(assignment);
    }

    @PutMapping(path="/deactivate/{assignmentId}")
    public VehicleTrackerAssignmentDto deactivateAssignment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                            @PathVariable("assignmentId") Integer assignmentId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        VehicleTrackerAssignment assignment = assignmentService.getAssignmentById(assignmentId);

        authorizationService.checkResourceAccessAuthorization(assignment, user, VehicleTrackerAssignment::getOwner);

        VehicleTrackerAssignment deactivatedAssignment = assignmentService.deactivateAssignment(assignmentId);

        return new VehicleTrackerAssignmentDto(deactivatedAssignment);
    }

    @ExceptionHandler({VehicleTrackerAssignmentNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleVehicleTrackerAssignmentNotFound(
            VehicleTrackerAssignmentNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({VehicleTrackerAssignmentRegistrationFailedException.class})
    public ResponseEntity<ApiExceptionDto> handleVehicleTrackerAssignmentRegistrationFailed(
            VehicleTrackerAssignmentRegistrationFailedException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }
}
