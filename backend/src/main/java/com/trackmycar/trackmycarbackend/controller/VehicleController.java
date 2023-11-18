package com.trackmycar.trackmycarbackend.controller;

import com.trackmycar.trackmycarbackend.dto.ApiExceptionDto;
import com.trackmycar.trackmycarbackend.dto.VehicleDto;
import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Vehicle;
import com.trackmycar.trackmycarbackend.exception.*;
import com.trackmycar.trackmycarbackend.service.TokenService;
import com.trackmycar.trackmycarbackend.service.UserService;
import com.trackmycar.trackmycarbackend.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/vehicles")
@CrossOrigin("*")
public class VehicleController {
    private final TokenService tokenService;
    private final UserService userService;
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(TokenService tokenService,
                             UserService userService,
                             VehicleService vehicleService) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public Set<VehicleDto> getUserVehicles(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Set<Vehicle> vehicles = vehicleService.getAllVehiclesByOwner(user);

        return vehicles
                .stream()
                .map(VehicleDto::new)
                .collect(Collectors.toSet());
    }

    @GetMapping(path="/{vehicleId}")
    public VehicleDto getVehicleById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @PathVariable("vehicleId") Integer vehicleId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

        // TODO: Fix resource access authorization
        // Simple authorization checking based on the ownership of the vehicle
        if (!vehicle.getOwner().getUsername().equals(user.getUsername())) {
            throw new AuthorizationFailedException();
        }

        return new VehicleDto(vehicle);
    }

    @PostMapping
    public VehicleDto addNewVehicle(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                    @RequestBody VehicleDto body) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);

        Vehicle vehicle = vehicleService.addVehicle(
                user,
                body.getName(),
                body.getDescription(),
                body.getMarkerHexColor()
        );

        return new VehicleDto(vehicle);
    }

    @PutMapping(path="/{vehicleId}")
    public VehicleDto updateVehicle(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                    @PathVariable("vehicleId") Integer vehicleId,
                                    @RequestBody VehicleDto body) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

        // TODO: Fix resource access authorization
        // Simple authorization checking based on the ownership of the vehicle
        if (!vehicle.getOwner().getUsername().equals(user.getUsername())) {
            throw new AuthorizationFailedException();
        }

        Vehicle updatedVehicle = vehicleService.updateVehicle(
                vehicle,
                body.getName(),
                body.getDescription(),
                body.getMarkerHexColor()
        );

        return new VehicleDto(updatedVehicle);
    }

    @DeleteMapping(path="/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                @PathVariable("vehicleId") Integer vehicleId) {
        String username = tokenService.getUsernameFromToken(token);
        AppUser user = userService.getUserByUsername(username);
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

        // TODO: Fix resource access authorization
        // Simple authorization checking based on the ownership of the vehicle
        if (!vehicle.getOwner().getUsername().equals(user.getUsername())) {
            throw new AuthorizationFailedException();
        }

        vehicleService.deleteVehicle(vehicle);

        return new ResponseEntity<String>("Vehicle has been deleted", HttpStatus.OK);
    }

    @ExceptionHandler({VehicleNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleVehicleNotFound(VehicleNotFoundException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }

    @ExceptionHandler({VehicleRegistrationFailedException.class})
    public ResponseEntity<ApiExceptionDto> handleVehicleRegistrationFailed(VehicleRegistrationFailedException ex) {
        return new ResponseEntity<>(new ApiExceptionDto(ex), ex.getStatus());
    }
}
