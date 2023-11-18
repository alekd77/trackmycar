package com.trackmycar.trackmycarbackend.model;

import java.time.LocalDateTime;

public class Position {
    private LocalDateTime timestamp;
    private Double latitude;
    private Double longitude;

    public Position() {
        super();
    }

    public Position(LocalDateTime timestamp,
                    Double latitude,
                    Double longitude) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
