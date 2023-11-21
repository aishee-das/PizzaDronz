package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.ac.ed.inf.ilp.data.LngLat;

public class DroneMove {

    @JsonProperty("orderNo")
    String orderNo;
    @JsonProperty("fromLongitude")
    private double fromLongitude;

    @JsonProperty("fromLatitude")
    private double fromLatitude;

    @JsonProperty("angle")
    private double angle;

    @JsonProperty("toLongitude")
    private double toLongitude;

    @JsonProperty("toLatitude")
    private double toLatitude;

    public DroneMove(String orderNo, double fromLongitude, double fromLatitude, double angle,
                     double toLongitude, double toLatitude) {
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
    }
//    public double getCurrentNodeFromLng() {
//        return fromLongitude;
//    }
//
//    public double getCurrentNodeFromLat() {
//        return fromLatitude;
//    }
//
    public double getNextNodeFromLng() {
        return toLongitude;
    }

    public double getNextNodeFromLat() {
        return toLatitude;
    }
//
//    public double getAngle() {
//        return angle;
//    }
}
