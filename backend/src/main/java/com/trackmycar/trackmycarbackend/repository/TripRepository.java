package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    boolean existsByName(String name);

    Optional<Set<Trip>> findAllByAssignmentOwner(AppUser owner);
}
