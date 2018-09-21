package com.example.ridowanahmed.childlocator.Model;

/**
 * Created by Ridowan Ahmed on 0012, August, 12, 2017.
 */

public class ChildInformation {

    String childName;
    double latitude;
    double longitude;
    long time;

    public ChildInformation() {
    }

    public ChildInformation(String childName, double latitude, double longitude, long time) {
        this.childName = childName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public String getChildName() {
        return childName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTime() {
        return time;
    }
}
