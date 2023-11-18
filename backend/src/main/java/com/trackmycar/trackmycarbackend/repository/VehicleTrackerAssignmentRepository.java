package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.VehicleTrackerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface VehicleTrackerAssignmentRepository extends JpaRepository<VehicleTrackerAssignment, Integer> {
    @Query("SELECT vta FROM VehicleTrackerAssignment vta WHERE vta.owner.userId = :ownerId")
    Optional<Set<VehicleTrackerAssignment>> findAllAssignmentsByOwner(Integer ownerId);

    @Query("SELECT vta FROM VehicleTrackerAssignment vta WHERE vta.owner.userId = :ownerId AND vta.isAssignmentActive = true")
    Optional<Set<VehicleTrackerAssignment>> findAllActiveAssignmentsByOwner(Integer ownerId);

    @Query("SELECT vta FROM VehicleTrackerAssignment vta WHERE vta.vehicle.vehicleId = :vehicleId")
    Optional<VehicleTrackerAssignment> findByAssignedVehicle(Integer vehicleId);

    @Query("SELECT vta FROM VehicleTrackerAssignment vta WHERE vta.tracker.trackerId = :trackerId")
    Optional<VehicleTrackerAssignment> findByAssignedTracker(Integer trackerId);
}
