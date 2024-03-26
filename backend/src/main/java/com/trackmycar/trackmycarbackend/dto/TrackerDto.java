package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.model.Tracker;

public class TrackerDto {
    private Integer trackerId;
    private AppUserDto owner;
    private String name;
    private String description;
    private String status;
    private String imei;

    public TrackerDto() {
    }

    public TrackerDto(Integer trackerId,
                      AppUserDto owner,
                      String name,
                      String description,
                      String status,
                      String imei) {
        this.trackerId = trackerId;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.status = status;
        this.imei = imei;
    }

    public TrackerDto(Tracker tracker) {
        this.trackerId = tracker.getTrackerId();
        this.owner = new AppUserDto(tracker.getOwner());
        this.name = tracker.getName();
        this.description = tracker.getDescription();
        this.status = tracker.getStatus().toString();
        this.imei = tracker.getImei();
    }

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }

    public AppUserDto getOwner() {
        return owner;
    }

    public void setOwner(AppUserDto owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
