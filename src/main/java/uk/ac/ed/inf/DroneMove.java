package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;

/**
 * Helper class to store the start, end coordinates of a move, the angle of the move and the order number
 */
public final class DroneMove {
    private final LngLat start;
    private final LngLat end;
    private double angle;
    private String orderNo;
    public DroneMove(LngLat start, double angle, LngLat end, String orderNo) {
        this.start = start;
        this.angle = angle;
        this.end = end;
        this.orderNo = orderNo;
    }
    public LngLat getStart() {
        return start;
    }
    public double getAngle() {
        return angle;
    }
    public LngLat getEnd() {
        return end;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
