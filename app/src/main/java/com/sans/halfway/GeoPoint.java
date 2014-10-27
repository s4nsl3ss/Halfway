package com.sans.halfway;

/**
 * Created by sans on 28.09.2014.
 */
public class GeoPoint {

    private double lat;
    private double lgt;

    public GeoPoint(double x, double y){
        this.lat=x;
        this.lgt=y;
    }

    protected double getLat(){
        return this.lat;
    }

    protected double getLgt(){
        return this.lgt;
    }

    public String toString(){
        return String.valueOf(lat)+","+String.valueOf(lgt);
    }
}
