package com.trackmycar.trackmycarbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name="vehicles")
public class Vehicle {
    @Id
    @SequenceGenerator(
            name = "vehicles_sequence",
            sequenceName = "vehicles_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vehicles_sequence"
    )
    @Column(name="vehicle_id")
    private Integer vehicleId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private AppUser owner;

    @Column(nullable=false)
    private String name;

    private String description;

    @Column(name="marker_hex_color", length = 7)
    private String markerHexColor;

    public Vehicle() {
        super();
    }

    public Vehicle(Integer vehicleId,
                   AppUser owner,
                   String name,
                   String description,
                   String markerHexColor) {
        this.vehicleId = vehicleId;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.markerHexColor = markerHexColor;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
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

    public String getMarkerHexColor() {
        return markerHexColor;
    }

    public void setMarkerHexColor(String markerHexColor) {
        this.markerHexColor = markerHexColor;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", markerHexColor='" + markerHexColor + '\'' +
                '}';
    }
}
