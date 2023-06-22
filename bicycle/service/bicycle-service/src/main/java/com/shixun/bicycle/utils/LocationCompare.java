package com.shixun.bicycle.utils;

import com.shixun.bicycle.pojo.LocationInfo;

import java.util.Comparator;

public class LocationCompare implements Comparator<LocationInfo> {
    @Override
    public int compare(LocationInfo o1, LocationInfo o2) {
        return (int) (o1.getDistance() - o2.getDistance());
    }
}
