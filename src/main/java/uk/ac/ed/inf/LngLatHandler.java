package uk.ac.ed.inf;


import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;


public class LngLatHandler implements LngLatHandling {
    /**
     * Returns the Euclidean distance between the provided points
     *
     * @param startPosition: The initial point (usually the drone)
     * @param endPosition: The end point (usually the drone's intended destination)
     * @return: The distance between startPosition and endPosition
     */
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        double x1 = startPosition.lng();
        double y1 = startPosition.lat();
        double x2 = endPosition.lng();
        double y2 = endPosition.lat();

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    /**
     * Check whether two positions are considered "close", i.e. within 0.00015 degrees of one another
     *
     * @param startPosition: The initial position (usually the drone)
     * @param otherPosition: The point we want to check proximity to
     * @return: Whether the two points are "close"
     */
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        return distanceTo(startPosition, otherPosition) < SystemConstants.DRONE_IS_CLOSE_DISTANCE;
    }

    /**
     * Uses the Ray casting algorithm for finding a point in a polygon
     *
     * Given a point `position` and a polygon `region`, we can draw a line
     * from the point beyond the bounding box of the polygon
     *
     * If this line crosses an even number of edges the point is not within the polygon,
     * where if it crosses an odd number it is
     *
     * @param position: The lng, lat position to be checked
     * @param region: The polygon to check against
     * @return: Whether `position` lies inside `region`
     */
    public boolean isInRegion(LngLat position, NamedRegion region) {
        Path2D polygon = new Path2D.Double();
        polygon.moveTo(region.vertices()[0].lat(), region.vertices()[0].lng());

        for (LngLat vertex : region.vertices()) {
            polygon.lineTo(vertex.lat(), vertex.lng());
        }

        polygon.closePath();

        return polygon.contains(new Point2D.Double(position.lat(), position.lng()));
    }

    /**
     * Produces the next position when given a point and an angle
     *
     * Includes special case for hovering when angle is set to 999
     *
     * @param startPosition: The start position
     * @param angle: Angle to move along, in degrees
     * @return: The updated position
     */
    public LngLat nextPosition(LngLat startPosition, double angle) {
        //If drone is hovering, return the same position
        if (angle == 999) {
            return startPosition;
        } else {
            //If drone is flying, calculate the new position
            double angleInRadians = Math.toRadians(angle);
            double newLng = startPosition.lng() + Math.cos (angleInRadians) * SystemConstants.DRONE_MOVE_DISTANCE;
            double newLat = startPosition.lat() + Math.sin (angleInRadians) * SystemConstants.DRONE_MOVE_DISTANCE;

            return new LngLat(newLng, newLat);
        }
    }
}

//package uk.ac.ed.inf;
//
//import uk.ac.ed.inf.ilp.constant.SystemConstants;
//import uk.ac.ed.inf.ilp.data.LngLat;
//import uk.ac.ed.inf.ilp.data.NamedRegion;
//import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class LngLatHandler implements LngLatHandling {
//    /**
//     * get the distance between two positions
//     * @param startPosition is where the start is
//     * @param endPosition is where the end is
//     * @return the euclidean distance between the positions
//     */
//    @Override
//    public double distanceTo(LngLat startPosition, LngLat endPosition) {
//        double x1 = startPosition.lng();
//        double y1 = startPosition.lat();
//        double x2 = endPosition.lng();
//        double y2 = endPosition.lat();
//
//        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
//    }
//
//    /**
//     * check if two positions are close (<= than SystemConstants.DRONE_IS_CLOSE_DISTANCE)
//     * @param startPosition is the starting position
//     * @param otherPosition is the position to check
//     * @return if the positions are close
//     */
//    @Override
//    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
//        return distanceTo(startPosition, otherPosition) < SystemConstants.DRONE_IS_CLOSE_DISTANCE;
//    }
//
//
//    /**
//     * check if the @position is in the @region using the ray casting algorithm (includes the border)
//     * @param position to check
//     * @param region as a closed polygon
//     * @return if the position is inside the region (including the border)
//     */
//
//    @Override
//    public boolean isInRegion(LngLat position, NamedRegion region) {
//        LngLat[] vertices = region.vertices();
//
//        int n = vertices.length;
//        boolean isInside = false;
//
//        for (int i = 0, j = n - 1; i < n; j = i++) {
//            LngLat vertexI = vertices[i];
//            LngLat vertexJ = vertices[j];
//
//            double xi = vertexI.lng();
//            double yi = vertexI.lat();
//            double xj = vertexJ.lng();
//            double yj = vertexJ.lat();
//
//            // Check if the position is on the border
//            if (isOnBoundary(position, xi, yi, xj, yj)) {
//                return true;
//            }
//
//            // Check if the position is within the polygon using ray casting algorithm
//            if (((yi > position.lat()) != (yj > position.lat())) &&
//                    (position.lng() < (xj - xi) * (position.lat() - yi) / (yj - yi) + xi)) {
//                isInside = !isInside;
//            }
//        }
//
//        return isInside;
//    }
//
//    private double getAngleFromDirection(Direction direction) {
//        return direction.angle;
//    }
//
//    /**
//     * find the next position if an @angle is applied to a @startPosition
//     * @param startPosition is where the start is
//     * @param angle is the angle to use in degrees
//     * @return the new position after the angle is used
//     */
//    @Override
//    public LngLat nextPosition(LngLat startPosition, double angle) {
//        //If drone is hovering, return the same position
//        if (angle == 999) {
//            return startPosition;
//        } else {
//            //If drone is flying, calculate the new position
//            double angleInRadians = Math.toRadians(angle);
//            double newLng = startPosition.lng() + Math.cos (angleInRadians) * SystemConstants.DRONE_MOVE_DISTANCE;
//            double newLat = startPosition.lat() + Math.sin (angleInRadians) * SystemConstants.DRONE_MOVE_DISTANCE;
//
//            return new LngLat(newLng, newLat);
//        }
//    }
//
//    // Additional method to use Direction enum
//    public LngLat nextDirectionPosition(LngLat startPosition, Direction direction) {
//        double angle = getAngleFromDirection(direction);
//        return nextPosition(startPosition, angle);
//    }
//
//    // Helper method to check if a point is on the boundary
//    private boolean isOnBoundary(LngLat position, double xi, double yi, double xj, double yj) {
//        return (position.lng() == xi && position.lat() == yi) ||
//                (position.lng() == xj && position.lat() == yj) ||
//                (position.lat() - yi) / (yj - yi) == (position.lng() - xi) / (xj - xi);
//    }
//
//
//}
