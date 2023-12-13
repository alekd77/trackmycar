package com.trackmycar.trackmycarbackend.dto;

import java.util.List;

public class TripWithGeolocationsDto {
    private TripDto trip;
    private List<TripGeolocationDto> geolocations;

    public TripWithGeolocationsDto() {
    }

    public TripWithGeolocationsDto(TripDto trip,
                                   List<TripGeolocationDto> geolocations) {
        this.trip = trip;
        this.geolocations = geolocations;
    }

    public TripDto getTrip() {
        return trip;
    }

    public void setTrip(TripDto trip) {
        this.trip = trip;
    }

    public List<TripGeolocationDto> getGeolocations() {
        return geolocations;
    }

    public void setGeolocations(List<TripGeolocationDto> geolocations) {
        this.geolocations = geolocations;
    }
}
