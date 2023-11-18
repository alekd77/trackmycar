package com.trackmycar.trackmycarbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="vehicle_tracker_assignments")
public class VehicleTrackerAssignment {
    @Id
    @SequenceGenerator(
            name = "vehicle_tracker_assignments_sequence",
            sequenceName = "vehicle_tracker_assignments_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vehicle_tracker_assignments_sequence"
    )
    @Column(name="vehicle_tracker_assignment_id")
    private Integer vehicleTrackerAssignmentId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private AppUser owner;

    @ManyToOne
    @JoinColumn(name="vehicle_id", nullable=false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name="tracker_id", nullable=false)
    private Tracker tracker;

    @Column(name="last_pos_timestamp")
    private LocalDateTime lastPosTimestamp;

    @Column(name="last_pos_latitude")
    private Double lastPosLatitude;

    @Column(name="last_pos_longitude")
    private Double lastPosLongitude;

    @Column(name="is_assignment_active")
    private boolean isAssignmentActive;

    public VehicleTrackerAssignment() {
        super();
    }

    public VehicleTrackerAssignment(Integer vehicleTrackerAssignmentId,
                                    AppUser owner,
                                    Vehicle vehicle,
                                    Tracker tracker,
                                    LocalDateTime lastPosTimestamp,
                                    Double lastPosLatitude,
                                    Double lastPosLongitude,
                                    boolean isAssignmentActive) {
        this.vehicleTrackerAssignmentId = vehicleTrackerAssignmentId;
        this.owner = owner;
        this.vehicle = vehicle;
        this.tracker = tracker;
        this.lastPosTimestamp = lastPosTimestamp;
        this.lastPosLatitude = lastPosLatitude;
        this.lastPosLongitude = lastPosLongitude;
        this.isAssignmentActive = isAssignmentActive;
    }

    public Integer getVehicleTrackerAssignmentId() {
        return vehicleTrackerAssignmentId;
    }

    public void setVehicleTrackerAssignmentId(Integer vehicleTrackerAssignmentId) {
        this.vehicleTrackerAssignmentId = vehicleTrackerAssignmentId;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public LocalDateTime getLastPosTimestamp() {
        return lastPosTimestamp;
    }

    public void setLastPosTimestamp(LocalDateTime lastPosTimestamp) {
        this.lastPosTimestamp = lastPosTimestamp;
    }

    public Double getLastPosLatitude() {
        return lastPosLatitude;
    }

    public void setLastPosLatitude(Double lastPosLatitude) {
        this.lastPosLatitude = lastPosLatitude;
    }

    public Double getLastPosLongitude() {
        return lastPosLongitude;
    }

    public void setLastPosLongitude(Double lastPosLongitude) {
        this.lastPosLongitude = lastPosLongitude;
    }

    public boolean isAssignmentActive() {
        return isAssignmentActive;
    }

    public void setAssignmentActive(boolean assignmentActive) {
        isAssignmentActive = assignmentActive;
    }

    @Override
    public String toString() {
        return "VehicleTrackerAssignment{" +
                "vehicleTrackerAssignmentId=" + vehicleTrackerAssignmentId +
                ", owner=" + owner +
                ", vehicle=" + vehicle +
                ", tracker=" + tracker +
                ", lastPosTimestamp=" + lastPosTimestamp +
                ", lastPosLatitude=" + lastPosLatitude +
                ", lastPosLongitude=" + lastPosLongitude +
                ", isAssignmentActive=" + isAssignmentActive +
                '}';
    }
}
