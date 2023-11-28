package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import uk.ac.ed.inf.ilp.data.LngLat;

public class DroneMove {

    @JsonProperty("orderNo")
    String orderNo;
    @JsonProperty("fromLongitude")
    private LngLat fromLongitude;

    @JsonProperty("fromLatitude")
    private LngLat fromLatitude;

    @JsonProperty("angle")
    private double angle;

    @JsonProperty("toLongitude")
    private LngLat toLongitude;

    @JsonProperty("toLatitude")
    private LngLat toLatitude;

    public DroneMove(String orderNo, LngLat fromLongitude, LngLat fromLatitude, double angle,
                     LngLat toLongitude, LngLat toLatitude) {
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
    @JsonIgnore
    public LngLat getNextNodeFromLng() {
        return toLongitude;
    }
    @JsonIgnore
    public LngLat getNextNodeFromLat() {
        return toLatitude;
    }
//
//    public double getAngle() {
//        return angle;
//    }
}
