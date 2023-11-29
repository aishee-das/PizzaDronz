package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.LngLat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DronePathCalculator {
    private final NamedRegion centralArea;
    private final NamedRegion[] noFlyZones;
    private final HashMap<String, List<DroneMove>> cachedPaths = new HashMap<>();

    /**
     * Helper class to calculate the path of a drone
     * @param noFlyZones the no-fly-zones
     * @param centralArea the central area
     */
    public DronePathCalculator(NamedRegion[] noFlyZones, NamedRegion centralArea) {
        this.noFlyZones = noFlyZones;
        this.centralArea = centralArea;
    }

    /**
     * Helper function to find the path from one location to another
     * @param start the start location
     * @param dest the end location
     * @param orderNo the order number
     * @return a list of moves
     */
    private List<DroneMove> calculatePath(LngLat start, LngLat dest, String orderNo) {
        List<LngLat> prevMoves = new ArrayList<>();
        LngLatHandler handler = new LngLatHandler();
        LngLat currentPos = start;
        ArrayList<DroneMove> path = new ArrayList<>();

        double moveAngle = 0;
        double distance;

        while (!handler.isCloseTo(currentPos, dest)) {
            double closest = Double.MAX_VALUE;

            for (Direction direction : Direction.values()) {
                LngLat nextPos = handler.nextPosition(currentPos, direction.angle);

                // Check if next position is previously considered
                if (!prevMoves.contains(new LngLat(nextPos.lng(), nextPos.lat()))) {
                    boolean currInCentral = handler.isInCentralArea(currentPos, centralArea);
                    boolean nextInCentral = handler.isInCentralArea(nextPos, centralArea);
                    boolean isInNoFlyZone = false;

                    // Checks if next position is in a noFlyZone
                    for (NamedRegion noFlyZone : this.noFlyZones) {
                        if (handler.isInRegion(nextPos, noFlyZone)) {
                            isInNoFlyZone = true;
                            break;
                        }
                    }

                    // If next position is not in a noFlyZone
                    if (!isInNoFlyZone) {

                        // If we leave the central area we are not allowed to go back in
                        if (currInCentral || !nextInCentral) {
                            distance = handler.distanceTo(nextPos, dest);

                            // If the distance is closer than the current closest distance
                            if (distance < closest) {

                                // Set new closest to this distance
                                closest = distance;

                                // Save the move angle
                                moveAngle = direction.angle;
                            }
                        }
                    }

                    // Add this move to the list of previous moves
                    prevMoves.add(new LngLat(nextPos.lng(), nextPos.lat()));
                }
            }

            // Using the closest distance to the destination create a new move and add it to the path
            DroneMove move = new DroneMove(currentPos, moveAngle, handler.nextPosition(currentPos, moveAngle), orderNo);
            path.add(move);

            // Set currentPosition to the end of the new move
            currentPos = handler.nextPosition(currentPos, moveAngle);
        }
        path.add(new DroneMove(currentPos, 999, currentPos, orderNo));
        return path;
    }
    public List<DroneMove> calculateTotalPath(LngLat restaurantLoc, LngLat deliveryLoc, String orderNo) {
        String cacheKey = generateCacheKey(restaurantLoc, deliveryLoc);

        if (cachedPaths.containsKey(cacheKey)) {
            return copyAndModifyCachedPath(cachedPaths.get(cacheKey), orderNo);
        } else {
            List<DroneMove> path = calculatePath(deliveryLoc, restaurantLoc, orderNo);
            List<DroneMove> reversedPath = reversePath(path, orderNo);

            path.addAll(reversedPath);
            cachedPaths.put(cacheKey, path);

            return path;
        }
    }

    private String generateCacheKey(LngLat start, LngLat dest) {
        return "KEY:" + start.lng() + start.lat() + dest.lng() + dest.lat();
    }

    private List<DroneMove> copyAndModifyCachedPath(List<DroneMove> cachedPath, String orderNo) {
        List<DroneMove> modifiedPath = new ArrayList<>();

        for (DroneMove move : cachedPath) {
            DroneMove modifiedMove = new DroneMove(move.getStart(), move.getAngle(), move.getEnd(), orderNo);
            modifiedPath.add(modifiedMove);
        }

        return modifiedPath;
    }

    private List<DroneMove> reversePath(List<DroneMove> path, String orderNo) {
        ArrayList<DroneMove> reversedPath = new ArrayList<>();

        for (DroneMove move : path) {
            if (move.getAngle() == 999) {
                continue;
            }

            DroneMove reversedMove = new DroneMove(move.getEnd(), (move.getAngle() + 180) % 360, move.getStart(), orderNo);
            reversedPath.add(reversedMove);
        }

        Collections.reverse(reversedPath);
        LngLat endHover = reversedPath.get(reversedPath.size() - 1).getEnd();
        reversedPath.add(new DroneMove(endHover, 999, endHover, orderNo));

        return reversedPath;
    }

//    /**
//     * Helper function to find the total path from one location to another and back again
//     * @param restaurantLoc the restaurant location
//     * @param dropOff the drop off location
//     * @param orderNo the order number
//     * @return a list of moves
//     */
//    public List<Move> calculateTotalPath(LngLat restaurantLoc, LngLat dropOff, String orderNo) {
//        // Creates a cache key for each path
//        String key = "KEY:" + restaurantLoc.lng() + restaurantLoc.lat() + dropOff.lng() + dropOff.lat();
//        if (cachedPaths.containsKey(key)) {
//
//            // Copying the cache
//            List<Move> cachedResult = new ArrayList<>();
//
//            // Set all orderNo to the current orderNo
//            for (Move move : cachedPaths.get(key)) {
//
//                // Copying the value of each move in the cachedPaths
//                Move copy = new Move(move.getStart(), move.getAngle(), move.getEnd(), orderNo);
//
//                // Adding the copied move to the cachedResult
//                cachedResult.add(copy);
//            }
//            return cachedResult;
//        }
//        else {
//
//            // Find path from drop off location to restaurant
//            List<Move> path = calculatePath(dropOff, restaurantLoc, orderNo);
//
//            // Creates a new list to store the reversed path
//            ArrayList<Move> copy = new ArrayList<>();
//            for (Move move : path) {
//
//                // If the move is a hover move, skip it
//                if (move.getAngle() == 999) {
//                    continue;
//                }
//
//                // Reverses positions and angle for each move
//                Move copyMove = new Move(move.getEnd(), (move.getAngle()+180)%360, move.getStart(), orderNo);
//
//                // Adds the reversed move to the copy
//                copy.add(copyMove);
//            }
//
//            // Reverses the path
//            Collections.reverse(copy);
//
//            // Saves position to hover after reversing path
//            LngLat endHover = copy.get(copy.size()-1).getEnd();
//
//            // Adds hover move at the end of the path
//            copy.add(new Move(endHover, 999, endHover, orderNo));
//
//            // Adds the reversed path to the original path
//            path.addAll(copy);
//
//            // Adds the path to the cache
//            cachedPaths.put(key, path);
//        }
//        // Returns the path
//        return cachedPaths.get(key);
//    }
}
