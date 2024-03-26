package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Trip;
import com.trackmycar.trackmycarbackend.model.VehicleTrackerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    boolean existsByName(String name);
    boolean existsByAssignmentAndIsTripEndedFalse(VehicleTrackerAssignment assignment);

    Optional<Set<Trip>> findAllByAssignmentOwner(AppUser owner);

    Optional<Trip> findByAssignmentAndIsTripEndedFalse(VehicleTrackerAssignment assignment);
}
