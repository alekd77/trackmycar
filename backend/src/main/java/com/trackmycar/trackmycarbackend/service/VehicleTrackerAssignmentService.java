package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.exception.VehicleTrackerAssignmentNotFoundException;
import com.trackmycar.trackmycarbackend.exception.VehicleTrackerAssignmentRegistrationFailedException;
import com.trackmycar.trackmycarbackend.model.*;
import com.trackmycar.trackmycarbackend.repository.VehicleTrackerAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class VehicleTrackerAssignmentService {
    private final VehicleTrackerAssignmentRepository vehicleTrackerAssignmentRepository;
    private final VehicleService vehicleService;
    private final TrackerService trackerService;

    @Autowired
    public VehicleTrackerAssignmentService(VehicleTrackerAssignmentRepository vehicleTrackerAssignmentRepository,
                                           VehicleService vehicleService,
                                           TrackerService trackerService) {
        this.vehicleTrackerAssignmentRepository = vehicleTrackerAssignmentRepository;
        this.vehicleService = vehicleService;
        this.trackerService = trackerService;
    }

    public Set<VehicleTrackerAssignment> getAllAssignmentsByOwner(Integer ownerId) {
        return vehicleTrackerAssignmentRepository
                .findAllAssignmentsByOwner(ownerId)
                .orElse(new HashSet<>());
    }

    public Set<VehicleTrackerAssignment> getAllActiveAssignmentsByOwner(Integer ownerId) {
        return vehicleTrackerAssignmentRepository
                .findAllActiveAssignmentsByOwner(ownerId)
                .orElse(new HashSet<>());
    }

    public VehicleTrackerAssignment getAssignmentById(Integer assignmentId) {
        return vehicleTrackerAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new VehicleTrackerAssignmentNotFoundException(
                        "Assignment of given ID: " + assignmentId + "\" not found"
                ));
    }

    public VehicleTrackerAssignment getAssignmentByVehicleId(Integer vehicleId) {
        return vehicleTrackerAssignmentRepository
                .findByAssignedVehicle(vehicleId)
                .orElseThrow(() -> new VehicleTrackerAssignmentNotFoundException(
                        "No assignment found for vehicle with ID: " + vehicleId
                ));
    }

    public VehicleTrackerAssignment getAssignmentByTrackerId(Integer trackerId) {
        return vehicleTrackerAssignmentRepository
                .findByAssignedTracker(trackerId)
                .orElseThrow(() -> new VehicleTrackerAssignmentNotFoundException(
                        "No assignment found for tracker with ID: " + trackerId
                ));
    }


//    public Position getLatestPositionForVehicle(Vehicle vehicle) {
//
//    }

    public VehicleTrackerAssignment addAssignment(AppUser owner,
                                                  Integer vehicleId,
                                                  Integer trackerId) {
        VehicleTrackerAssignment assignment = new VehicleTrackerAssignment();
        assignment.setOwner(owner);

        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        assignment.setVehicle(vehicle);

        Tracker tracker = trackerService.getTrackerById(trackerId);
        assignment.setTracker(tracker);

        assignment.setAssignmentActive(true);

        try {
            return vehicleTrackerAssignmentRepository.save(assignment);
        } catch (Exception e) {
            throw new VehicleTrackerAssignmentRegistrationFailedException();
        }
    }

    public VehicleTrackerAssignment deactivateAssignment(Integer assignmentId) {
        VehicleTrackerAssignment assignment = getAssignmentById(assignmentId);
        assignment.setAssignmentActive(false);
        return vehicleTrackerAssignmentRepository.save(assignment);
    }

    public void deleteAssignment(VehicleTrackerAssignment assignment) {
        vehicleTrackerAssignmentRepository.delete(assignment);
    }
}
