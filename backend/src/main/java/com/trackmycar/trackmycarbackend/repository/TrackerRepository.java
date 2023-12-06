package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TrackerRepository extends JpaRepository<Tracker, Integer> {
    Optional<Set<Tracker>> findTrackersByOwner(AppUser owner);

    Optional<Tracker> findByOwnerAndName(AppUser owner, String name);
}
