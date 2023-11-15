package com.trackmycar.trackmycarbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name="trackers")
public class Tracker {
    @Id
    @SequenceGenerator(
            name = "trackers_sequence",
            sequenceName = "trackers_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trackers_sequence"
    )
    @Column(name="tracker_id")
    private Integer trackerId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private AppUser owner;

    @Column(nullable=false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private TrackerStatus status;

    @Column(length=15)
    private String imei;

    @Column(name="current_battery_level")
    private Integer currentBatteryLevel;

    public Tracker() {
        super();
    }

    public Tracker(Integer trackerId,
                   AppUser owner,
                   String name,
                   String description,
                   TrackerStatus status,
                   String imei,
                   Integer currentBatteryLevel) {
        this.trackerId = trackerId;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.status = status;
        this.imei = imei;
        this.currentBatteryLevel = currentBatteryLevel;
    }

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
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

    public TrackerStatus getStatus() {
        return status;
    }

    public void setStatus(TrackerStatus status) {
        this.status = status;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getCurrentBatteryLevel() {
        return currentBatteryLevel;
    }

    public void setCurrentBatteryLevel(Integer currentBatteryLevel) {
        this.currentBatteryLevel = currentBatteryLevel;
    }

    @Override
    public String toString() {
        return "Tracker{" +
                "trackerId=" + trackerId +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", imei='" + imei + '\'' +
                ", currentBatteryLevel=" + currentBatteryLevel +
                '}';
    }
}
