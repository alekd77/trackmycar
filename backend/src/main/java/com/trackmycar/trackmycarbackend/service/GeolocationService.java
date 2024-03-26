package com.trackmycar.trackmycarbackend.service;

import org.springframework.stereotype.Service;

@Service
public class GeolocationService {
    private static final double EARTH_RADIUS = 6371.0; // Earth radius in kilometers

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        // Calculate the differences
        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;

        // Haversine formula
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        double distance = EARTH_RADIUS * c;

        return distance;
    }
}
