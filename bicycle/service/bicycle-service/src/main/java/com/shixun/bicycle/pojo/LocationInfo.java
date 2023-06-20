package com.shixun.bicycle.pojo;

import lombok.Data;

@Data
public class LocationInfo {
    double jd;
    double wd;
    int area;

    private double distance;

    public LocationInfo(double jd, double wd, int area) {
        this.jd = jd;
        this.wd = wd;
        this.area = area;
    }
}
