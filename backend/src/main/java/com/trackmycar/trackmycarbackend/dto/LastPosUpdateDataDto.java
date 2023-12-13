package com.trackmycar.trackmycarbackend.dto;

import com.trackmycar.trackmycarbackend.exception.FailedToUpdateLastPosException;

import java.time.LocalDateTime;

public class LastPosUpdateDataDto {
    private String imei;
    private LocalDateTime timestamp;
    private Double latitude;
    private Double longitude;

    public LastPosUpdateDataDto() {
    }

    public LastPosUpdateDataDto(String imei, LocalDateTime timestamp, Double latitude, Double longitude) {
        this.imei = imei;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        try {
            this.timestamp = LocalDateTime.parse(timestamp);
        } catch (Exception e) {
            throw new FailedToUpdateLastPosException(
                    "Invalid timestamp: " + timestamp +
                            " (expected timestamp format is: YYYY-MM-DD'T'HH:MM:SS.XXX");
        }
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
