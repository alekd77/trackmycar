package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Tracker;
import com.trackmycar.trackmycarbackend.model.Vehicle;
import com.trackmycar.trackmycarbackend.model.VehicleTrackerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleTrackerAssignmentRepository extends JpaRepository<VehicleTrackerAssignment, Integer> {
    Optional<Set<VehicleTrackerAssignment>> findAllByOwner(AppUser owner);

    Optional<Set<VehicleTrackerAssignment>> findAllByOwnerAndIsAssignmentActiveTrue(AppUser owner);

    Optional<VehicleTrackerAssignment> findByVehicleAndIsAssignmentActiveTrue(Vehicle vehicle);

    Optional<VehicleTrackerAssignment> findByTrackerAndIsAssignmentActiveTrue(Tracker tracker);

}
