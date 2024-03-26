package com.trackmycar.trackmycarbackend.repository;

import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Optional<Set<Vehicle>> findVehiclesByOwner(AppUser owner);

    Optional<Vehicle> findByOwnerAndName(AppUser owner, String name);
}
