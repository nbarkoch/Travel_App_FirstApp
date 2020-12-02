package com.example.travelapp_part1.Entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

//import java.io.Serializable;
import java.text.NumberFormat;

public class UserLocation implements Parcelable {
    private Double lat;
    private Double lon;

    public static final Creator<UserLocation> CREATOR = new Creator<UserLocation>() {
        @Override
        public UserLocation createFromParcel(Parcel in) {
            return new UserLocation(in);
        }

        @Override
        public UserLocation[] newArray(int size) {
            return new UserLocation[size];
        }
    };

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public UserLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public UserLocation() {
    }

    public UserLocation convertFromLocation(Location location){
        if (location==null)
            return null;
        return new UserLocation(location.getLatitude(),location.getLongitude());
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getInstance(); // get instance
        nf.setMaximumFractionDigits(2); // set decimal places
        return  nf.format(lat) + ", " + nf.format(lon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
    }

    // Parcelling part
    public UserLocation(Parcel in){
        this.lat = in.readDouble();
        this.lon = in.readDouble();
    }
}
