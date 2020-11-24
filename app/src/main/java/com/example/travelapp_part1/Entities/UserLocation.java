package com.example.travelapp_part1.Entities;

// temporary with no use and unknown details...
public class UserLocation {
    private double x;
    private double y;

    public UserLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getLat() {
        return x;
    }

    public double getLon() {
        return y;
    }

}
