package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.model.Position;
import com.trackmycar.trackmycarbackend.model.VehicleTrackerAssignment;

public class VehicleTrackerAssignmentDto {
    private Integer assignmentId;
    private Integer ownerId;
    private Integer vehicleId;
    private Integer trackerId;
    private boolean isAssignmentActive;
    private Position lastPosition;

    public VehicleTrackerAssignmentDto() {
        super();
    }

    public VehicleTrackerAssignmentDto(Integer assignmentId,
                                       Integer ownerId,
                                       Integer vehicleId,
                                       Integer trackerId,
                                       boolean isAssignmentActive,
                                       Position lastPosition) {
        super();
        this.assignmentId = assignmentId;
        this.ownerId = ownerId;
        this.vehicleId = vehicleId;
        this.trackerId = trackerId;
        this.isAssignmentActive = isAssignmentActive;
        this.lastPosition = lastPosition;
    }

    public VehicleTrackerAssignmentDto(VehicleTrackerAssignment assignment) {
        super();
        this.assignmentId = assignment.getVehicleTrackerAssignmentId();
        this.ownerId = assignment.getOwner().getUserId();
        this.vehicleId = assignment.getVehicle().getVehicleId();
        this.trackerId = assignment.getTracker().getTrackerId();
        this.isAssignmentActive = assignment.isAssignmentActive();
        this.lastPosition = new Position(
                assignment.getLastPosTimestamp(),
                assignment.getLastPosLatitude(),
                assignment.getLastPosLongitude()
        );
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }

    public boolean isAssignmentActive() {
        return isAssignmentActive;
    }

    public void setAssignmentActive(boolean assignmentActive) {
        isAssignmentActive = assignmentActive;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
    }
}
