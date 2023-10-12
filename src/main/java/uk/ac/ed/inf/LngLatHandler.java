package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

public class LngLatHandler implements LngLatHandling {
    /**
     * get the distance between two positions
     * @param startPosition is where the start is
     * @param endPosition is where the end is
     * @return the euclidean distance between the positions
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
     * check if two positions are close (<= than SystemConstants.DRONE_IS_CLOSE_DISTANCE)
     * @param startPosition is the starting position
     * @param otherPosition is the position to check
     * @return if the positions are close
     */
    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        return distanceTo(startPosition, otherPosition) < SystemConstants.DRONE_IS_CLOSE_DISTANCE;
    }

    /**
     * check if the @position is in the @region using the ray casting algorithm (includes the border)
     * @param position to check
     * @param region as a closed polygon
     * @return if the position is inside the region (including the border)
     */

    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        int numVertices = region.vertices().length;
        LngLat[] vertices = region.vertices();

        boolean isInside = false;
        for (int i = 0, j = numVertices - 1; i < numVertices; j = i++) {
            double xi = vertices[i].lng();
            double yi = vertices[i].lat();
            double xj = vertices[j].lng();
            double yj = vertices[j].lat();

            boolean intersect = ((yi > position.lat()) != (yj > position.lat())) && (position.lng() < (xj - xi) * (position.lat() - yi) / (yj - yi) + xi);

            if (intersect) {
                isInside = !isInside;
            }
        }
        return isInside;
    }

    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        //If drone is hovering, return the same position
        if (angle == 999) {
            return startPosition;
        } else {
            //If drone is flying, calculate the new position
            double angleInRadians = Math.toRadians(angle);

            double newLng = startPosition.lng() + Math.cos (angleInRadians) * 0.00015;
            double newLat = startPosition.lat() + Math.sin (angleInRadians) * 0.00015;

            return new LngLat(newLng, newLat);
        }
    }
}
