package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.Trip;
import com.trackmycar.trackmycarbackend.model.TripGeolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripGeolocationRepository extends JpaRepository<TripGeolocation, Integer> {
    Optional<TripGeolocation> findFirstByTripOrderByTimestampDesc(Trip trip);
}
