package uk.ac.ed.inf;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;

import java.util.*;
class Cell {
    LngLat coords;
    double f, g, h;
    Cell parent;
    double angle;

    public Cell(LngLat coords) {
        this.coords = coords;
        parent = null;
        f = 0;
        g = 0;
        h = 0;
    }

    @Override
    public int hashCode(){
        return Objects.hash(coords);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }

        if(obj == null || getClass() != obj.getClass()){
            return false;
        }

        Cell other = (Cell)obj;
        return other.coords == coords;
    }


}

public class AStar {
    private static final double[] DIRS = {0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5, 180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5};
    //private static final double[] DIRS = {0.0, 45.0, 90.0, 135.0, 180.0, 225.0, 270.0, 315.0};
    static PriorityQueue<Cell> openSet;
    static HashSet<Cell> closedSet;
    static List<Cell> path;
    static Map<LngLat, Cell> coordinateToCellMap;
    static LngLatHandler lngLatHandler = new LngLatHandler();

    public static boolean findShortestPath(NamedRegion[] noFlyZones, Cell start, Cell goal, NamedRegion Central) {
        openSet.add(start);
        coordinateToCellMap.put(start.coords, start);

        while (!openSet.isEmpty()) {
            Cell current = openSet.poll();
            closedSet.add(current);

            boolean close = lngLatHandler.isCloseTo(current.coords, goal.coords);
            if (close) {
                path = new ArrayList<>();
                while (current != null) {
                    path.add(current);
                    current = current.parent;
                }
                Collections.reverse(path);
                return true;
            }

            for (double dir : DIRS) {
                LngLat nextCoords = lngLatHandler.nextPosition(current.coords, dir);
                Cell next = new Cell(nextCoords);

                boolean noFly = false;
                for (NamedRegion noFlyZone : noFlyZones) {
                    if (lngLatHandler.isInRegion(nextCoords, noFlyZone)) {
                        noFly = true;
                    }
                }

                if (lngLatHandler.isInRegion(current.coords, Central)) {
                    if (!(lngLatHandler.isInRegion(nextCoords, Central))) {
                        noFly = true;
                    }
                }

                if (!noFly && !closedSet.contains(next)) {
                    double tentativeG = current.g + SystemConstants.DRONE_MOVE_DISTANCE;

                    Cell existing_neighbor = findNeighbor(nextCoords);

                    if (existing_neighbor != null) {
                        if (tentativeG < existing_neighbor.g) {
                            existing_neighbor.angle = dir;
                            existing_neighbor.parent = current;
                            existing_neighbor.g = tentativeG;
                            existing_neighbor.h = 2 * heuristic(existing_neighbor, goal);
                            existing_neighbor.f = existing_neighbor.g + existing_neighbor.h;
                        }
                    } else {
                        Cell neighbor = new Cell(nextCoords);
                        neighbor.angle = dir;
                        neighbor.parent = current;
                        neighbor.g = tentativeG;
                        neighbor.h = 2 * heuristic(neighbor, goal);
                        neighbor.f = neighbor.g + neighbor.h;

                        openSet.add(neighbor);
                        coordinateToCellMap.put(nextCoords, neighbor);
                    }
                }
            }
        }
        return false;
    }

    public static Cell findNeighbor(LngLat coords) {
        return coordinateToCellMap.get(coords);
    }

    public static double heuristic(Cell a, Cell b) {
        return lngLatHandler.distanceTo(a.coords, b.coords);
    }
}