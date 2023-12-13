package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.TripGeolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripGeolocationRepository extends JpaRepository<TripGeolocation, Integer> {
}
