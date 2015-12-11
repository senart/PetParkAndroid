package com.example.gavriltonev.petpark.models;

/**
 * Created by gavriltonev on 12/11/15.
 */
public class Pin {

    private String name;
    private double latitude;
    private double longitude;

    public Pin(String name, Double latitude, Double longitude) {
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
