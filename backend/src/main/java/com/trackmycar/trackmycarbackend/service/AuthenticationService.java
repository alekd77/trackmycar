package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.model.*;
import com.trackmycar.trackmycarbackend.exception.AuthenticationFailedException;
import com.trackmycar.trackmycarbackend.exception.InvalidInputException;
import com.trackmycar.trackmycarbackend.repository.RoleRepository;
import com.trackmycar.trackmycarbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final VehicleService vehicleService;
    private final TrackerService trackerService;
    private final VehicleTrackerAssignmentService assignmentService;
    private final TripService tripService;
    private final GeolocationService geolocationService;

    @Autowired
    public AuthenticationService(RoleRepository roleRepository,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authManager,
                                 TokenService tokenService,
                                 VehicleService vehicleService,
                                 TrackerService trackerService,
                                 VehicleTrackerAssignmentService assignmentService,
                                 TripService tripService,
                                 GeolocationService geolocationService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.vehicleService = vehicleService;
        this.trackerService = trackerService;
        this.assignmentService = assignmentService;
        this.tripService = tripService;
        this.geolocationService = geolocationService;
    }

    @Transactional
    public AppUser registerUser(String username, String password, String email, String name) {
        if (username == null || username.isEmpty()) {
            throw new InvalidInputException("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            throw new InvalidInputException("Password cannot be empty");
        }

        if (password.length() < 8) {
            throw new InvalidInputException("Password must be at least 8 characters");
        }

        if (email == null || email.isEmpty()) {
            throw new InvalidInputException("Email cannot be empty");
        }

        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new InvalidInputException("Username is already taken");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidInputException("Email is already taken");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").orElseThrow();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        // Convert the first character of the name to uppercase
        String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1);

        try {
            AppUser user = new AppUser();
            user.setAuthorities(authorities);
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setEmail(email);
            user.setName(formattedName);
            userRepository.save(user);

            // populate db with user-related sample data for demo purpose

            Vehicle vehicle1 = vehicleService.addVehicle(
                    user,
                    "BMW",
                    "personal",
                    "#3452eb"
            );

            Vehicle vehicle2 = vehicleService.addVehicle(
                    user,
                    "Toyota",
                    "business",
                    "#fcba03"
            );

            Tracker tracker1 = trackerService.addTracker(
                    user,
                    "tracker1",
                    "",
                    "123123123123123"
            );

            VehicleTrackerAssignment assignment = assignmentService.addAssignment(
                    user,
                    vehicle1.getVehicleId(),
                    tracker1.getTrackerId()
            );

            assignmentService.updateLastPosition(
                    assignment,
                    LocalDateTime.parse("2024-03-13T14:44:09.123000"),
                    52.198941,
                    21.024080
            );

            Trip trip1 = tripService.startNewTrip(assignment);

            tripService.addTripGeolocation(
                    trip1,
                    LocalDateTime.parse("2024-01-07T20:13:11.879000"),
                    51.084614,
                    17.047092,
                    4.6
            );

            tripService.addTripGeolocation(
                    trip1,
                    LocalDateTime.parse("2024-01-07T20:18:11.879000"),
                    51.083327,
                    17.037597,
                    12.5456
            );

            tripService.addTripGeolocation(
                    trip1,
                    LocalDateTime.parse("2024-01-07T20:23:11.879000"),
                    51.0888933,
                    17.0343995,
                    18.5456
            );

            tripService.addTripGeolocation(
                    trip1,
                    LocalDateTime.parse("2024-01-07T20:28:11.879000"),
                    51.089555,
                    17.03695,
                    15.5612
            );

            tripService.addTripGeolocation(
                    trip1,
                    LocalDateTime.parse("2024-01-07T20:33:11.879000"),
                    51.091176,
                    17.04018,
                    19.1222
            );

            tripService.addTripGeolocation(
                    trip1,
                    LocalDateTime.parse("2024-01-07T20:38:11.879000"),
                    51.08749,
                    17.040872,
                    24.2355
            );

            tripService.addTripGeolocation(
                    trip1,
                    LocalDateTime.parse("2024-01-07T20:43:11.879000"),
                    51.085824,
                    17.045869,
                    29.2733
            );

            tripService.finishCurrentTrip(assignment);

            return user;

        } catch(Exception e) {
            throw new AuthenticationFailedException("Failed to register user");
        }
    }

    public Authentication authenticateUser(String username, String password) {
        try {
            Authentication auth =  authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            System.out.println("User " + username + " successfully authenticated");

            return auth;
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException();
        }
    }
}
