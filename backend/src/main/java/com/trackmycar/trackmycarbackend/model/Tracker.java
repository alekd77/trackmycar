package com.trackmycar.trackmycarbackend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "tracker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleTrackerAssignment> assignments;

    @Column(nullable=false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private TrackerStatus status;

    @Column(length=15)
    private String imei;

    public Tracker() {
        super();
        this.assignments = new ArrayList<>();
    }

    public Tracker(Integer trackerId,
                   AppUser owner,
                   List<VehicleTrackerAssignment> assignments,
                   String name,
                   String description,
                   TrackerStatus status,
                   String imei) {
        this.trackerId = trackerId;
        this.owner = owner;
        this.assignments = assignments;
        this.name = name;
        this.description = description;
        this.status = status;
        this.imei = imei;
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

    public List<VehicleTrackerAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<VehicleTrackerAssignment> assignments) {
        this.assignments = assignments;
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

    @Override
    public String toString() {
        return "Tracker{" +
                "trackerId=" + trackerId +
                ", owner=" + owner +
                ", assignments=" + assignments +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", imei='" + imei + '\'' +
                '}';
    }
}
