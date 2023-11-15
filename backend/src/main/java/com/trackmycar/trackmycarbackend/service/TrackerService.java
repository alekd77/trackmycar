package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.exception.InvalidInputException;
import com.trackmycar.trackmycarbackend.exception.TrackerNotFoundException;
import com.trackmycar.trackmycarbackend.exception.TrackerRegistrationFailedException;
import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Tracker;
import com.trackmycar.trackmycarbackend.model.TrackerStatus;
import com.trackmycar.trackmycarbackend.repository.TrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class TrackerService {
    private final TrackerRepository trackerRepository;

    @Autowired
    public TrackerService(TrackerRepository trackerRepository) {
        this.trackerRepository = trackerRepository;
    }

    public Set<Tracker> getAllTrackersByOwner(AppUser owner) {
        return trackerRepository
                .findTrackersByOwner(owner)
                .orElse(new HashSet<>());
    }

    public Tracker getTrackerById(Integer trackerId) {
        return trackerRepository
                .findById(trackerId)
                .orElseThrow(() -> new TrackerNotFoundException(
                        "Tracker of given ID: " + trackerId + " not found"
                ));
    }

    public Tracker addTracker(AppUser owner,
                              String name,
                              String description,
                              String imei) {

        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Tracker name cannot be empty");
        }

        // TODO: Add IMEI validation
        if (imei == null || imei.isEmpty()) {
            throw new InvalidInputException("IMEI cannot be empty");
        }

        // checks if the user already have a tracker of the same name
        if (trackerRepository.findByOwnerAndName(owner, name).isPresent()) {
            throw new InvalidInputException("Tracker of given name: " + name + " already exists");
        }

        Tracker tracker = new Tracker();
        tracker.setOwner(owner);
        tracker.setName(name);
        tracker.setDescription(description);
        tracker.setStatus(TrackerStatus.UNASSIGNED);
        tracker.setImei(imei);

        try {
            return trackerRepository.save(tracker);
        } catch (Exception e) {
            throw new TrackerRegistrationFailedException();
        }
    }

    @Transactional
    public Tracker updateTracker(Tracker tracker,
                                 String name,
                                 String description,
                                 String imei) {
        if (name != null && !name.isEmpty()
                && !Objects.equals(name, tracker.getName())) {
            if (trackerRepository.findByOwnerAndName(tracker.getOwner(), name).isPresent()) {
                throw new InvalidInputException("Tracker of given name: " + name + " already exists");
            }

            tracker.setName(name);
        }

        if (description != null && !description.isEmpty()
                && !Objects.equals(description, tracker.getDescription())) {
            tracker.setDescription(description);
        }

        if (imei != null && !imei.isEmpty()
                && !Objects.equals(imei, tracker.getImei())) {
            tracker.setImei(imei);
        }

        try {
            return trackerRepository.save(tracker);
        } catch (Exception e) {
            throw new TrackerRegistrationFailedException();
        }
    }

    public void deleteTracker(Tracker tracker) {
        trackerRepository.delete(tracker);
    }
}
