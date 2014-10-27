package com.sans.halfway;


public class JStep {

   final private int distance;
   final private GeoPoint start;
   final private GeoPoint end;



    public JStep(int dis, double stlat, double stltd, double elat, double eltd){
        this.distance = dis;
        this.start = new GeoPoint(stlat,stltd);
        this.end = new GeoPoint(elat,eltd);
    }


    public GeoPoint getEnd() {
        return end;
    }

    public GeoPoint getStart() {
        return start;
    }

    public int getDistance() {
        return distance;
    }

    public String toString(){
        return "d: "+ String.valueOf(distance) + " s: " + String.valueOf(start.getLat()) + "," + String.valueOf(start.getLgt()) + " e: "  + String.valueOf(end.getLat()) + "," + String.valueOf(end.getLgt());
    }

}
