package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.entity.AppUser;
import com.trackmycar.trackmycarbackend.entity.Vehicle;
import com.trackmycar.trackmycarbackend.exception.InvalidInputException;
import com.trackmycar.trackmycarbackend.exception.VehicleNotFoundException;
import com.trackmycar.trackmycarbackend.exception.VehicleRegistrationFailedException;
import com.trackmycar.trackmycarbackend.repository.VehicleRepository;
import com.trackmycar.trackmycarbackend.util.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle getVehicleById(Integer vehicleId) {
        return vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "Vehicle of given ID: " + vehicleId + " not found"));
    }

    public Set<Vehicle> getAllVehiclesByOwner(AppUser owner) {
        return vehicleRepository.findVehiclesByOwner(owner).orElse(new HashSet<>());
    }

    public Vehicle addVehicle(AppUser owner,
                              String name,
                              String description,
                              String markerHexColor) {

        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Vehicle name cannot be empty");
        }

        if (!InputValidator.isValidHexColor(markerHexColor)) {
            throw new InvalidInputException("Invalid vehicle's marker hex color");
        }

        // checks if the user already have a vehicle of the same name
        if (vehicleRepository.findByOwnerAndName(owner, name).isPresent()) {
            throw new InvalidInputException("Vehicle of given name: " + name + " already exists");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setName(name);
        vehicle.setDescription(description);
        vehicle.setMarkerHexColor(markerHexColor);

        try {
            return vehicleRepository.save(vehicle);
        } catch (Exception e) {
            throw new VehicleRegistrationFailedException();
        }
    }

    public Vehicle updateVehicle(Vehicle vehicle,
                                 String name,
                                 String description,
                                 String markerHexColor) {

        if (name != null && !name.isEmpty()
                && !Objects.equals(name, vehicle.getName())) {
            if (vehicleRepository.findByOwnerAndName(vehicle.getOwner(), name).isEmpty()) {
                throw new InvalidInputException("Vehicle of given name: " + name + " already exists");
            }

            vehicle.setName(name);
        }

        if (description != null && !description.isEmpty()
                && !Objects.equals(description, vehicle.getDescription())) {
            vehicle.setDescription(description);
        }

        if (InputValidator.isValidHexColor(markerHexColor)
                && !Objects.equals(markerHexColor, vehicle.getMarkerHexColor())) {
            vehicle.setMarkerHexColor(markerHexColor);
        }

        try {
            return vehicleRepository.save(vehicle);
        } catch (Exception e) {
            throw new VehicleRegistrationFailedException();
        }
    }

    public void deleteVehicle(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }
}
