package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.exception.VehicleTrackerAssignmentNotFoundException;
import com.trackmycar.trackmycarbackend.exception.VehicleTrackerAssignmentRegistrationFailedException;
import com.trackmycar.trackmycarbackend.model.*;
import com.trackmycar.trackmycarbackend.repository.TrackerRepository;
import com.trackmycar.trackmycarbackend.repository.VehicleRepository;
import com.trackmycar.trackmycarbackend.repository.VehicleTrackerAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class VehicleTrackerAssignmentService {
    private final VehicleTrackerAssignmentRepository vehicleTrackerAssignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final TrackerRepository trackerRepository;
    private final VehicleService vehicleService;
    private final TrackerService trackerService;

    @Autowired
    public VehicleTrackerAssignmentService(VehicleTrackerAssignmentRepository vehicleTrackerAssignmentRepository,
                                           VehicleRepository vehicleRepository,
                                           TrackerRepository trackerRepository,
                                           VehicleService vehicleService,
                                           TrackerService trackerService) {
        this.vehicleTrackerAssignmentRepository = vehicleTrackerAssignmentRepository;
        this.vehicleRepository = vehicleRepository;
        this.trackerRepository = trackerRepository;
        this.vehicleService = vehicleService;
        this.trackerService = trackerService;
    }

    public Set<VehicleTrackerAssignment> getAllAssignmentsByOwner(AppUser owner) {
        return vehicleTrackerAssignmentRepository
                .findAllByOwner(owner)
                .orElse(new HashSet<>());
    }

    public VehicleTrackerAssignment getAssignmentById(Integer assignmentId) {
        return vehicleTrackerAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new VehicleTrackerAssignmentNotFoundException(
                        "No assignment found for provided ID: " + assignmentId
                ));
    }

    public VehicleTrackerAssignment addAssignment(AppUser owner,
                                                  Integer vehicleId,
                                                  Integer trackerId) {

        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        boolean isVehicleAlreadyAssigned = vehicleTrackerAssignmentRepository
                .findByVehicleAndIsAssignmentActiveTrue(vehicle)
                .isPresent();

        if (isVehicleAlreadyAssigned) {
            throw new VehicleTrackerAssignmentRegistrationFailedException(
                    "Vehicle with ID: " + vehicleId + " is already assigned"
            );
        }

        Tracker tracker = trackerService.getTrackerById(trackerId);
        boolean isTrackerAlreadyAssigned = vehicleTrackerAssignmentRepository
                .findByTrackerAndIsAssignmentActiveTrue(tracker)
                .isPresent();

        if (isTrackerAlreadyAssigned) {
            throw new VehicleTrackerAssignmentRegistrationFailedException(
                    "Tracker with ID: " + trackerId + " is already assigned"
            );
        }

        VehicleTrackerAssignment assignment = new VehicleTrackerAssignment();
        assignment.setOwner(owner);
        assignment.setVehicle(vehicle);
        assignment.setTracker(tracker);
        assignment.setAssignmentActive(true);

        try {
            return vehicleTrackerAssignmentRepository.save(assignment);
        } catch (Exception e) {
            throw new VehicleTrackerAssignmentRegistrationFailedException();
        }
    }

    public void deleteAssignment(VehicleTrackerAssignment assignment) {
        vehicleTrackerAssignmentRepository.delete(assignment);
    }

    public Set<VehicleTrackerAssignment> getAllActiveAssignmentsByOwner(AppUser owner) {
        return vehicleTrackerAssignmentRepository
                .findAllByOwnerAndIsAssignmentActiveTrue(owner)
                .orElse(new HashSet<>());
    }

    public VehicleTrackerAssignment getActiveAssignmentByVehicleId(Integer vehicleId) {
        return vehicleTrackerAssignmentRepository
                .findByVehicleAndIsAssignmentActiveTrue(
                        vehicleRepository.getReferenceById(vehicleId))
                .orElseThrow(() -> new VehicleTrackerAssignmentNotFoundException(
                        "No assignment found for vehicle with ID: " + vehicleId)
                );
    }

    public VehicleTrackerAssignment getActiveAssignmentByTrackerId(Integer trackerId) {
        return vehicleTrackerAssignmentRepository
                .findByTrackerAndIsAssignmentActiveTrue(
                        trackerRepository.getReferenceById(trackerId))
                .orElseThrow(() -> new VehicleTrackerAssignmentNotFoundException(
                        "No assignment found for tracker with ID: " + trackerId)
                );
    }

    public VehicleTrackerAssignment deactivateAssignment(Integer assignmentId) {
        VehicleTrackerAssignment assignment = getAssignmentById(assignmentId);
        assignment.setAssignmentActive(false);
        return vehicleTrackerAssignmentRepository.save(assignment);
    }
}
