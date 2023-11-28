package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;

import java.util.*;

/**
 * This class is used to find the path that the drone should take going between any two points using a
 * weighted A* (A star) search algorithm.
 */
public class AStarSearch {

    private final LngLatHandler lngLatHandler = new LngLatHandler();
    public static Node3 endNode;

    private List<Node3> getPathBack() {
        List<Node3> path = new ArrayList<>();
        for (Node3 node = endNode; node != null; node = node.parent) {
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }

    public List<Node3> pathFindingAlgorithm(LngLat startLocation, Node3 goal, NamedRegion[] noFlyZones) {
        Set<Node3> explored = new HashSet<>();

        Node3 source = new Node3(startLocation, goal);
        source.parent = null;

        PriorityQueue<Node3> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f_scores));

        source.g_scores = 0;
        queue.add(source);

        double droneMoveDistance = SystemConstants.DRONE_MOVE_DISTANCE;

        while (!queue.isEmpty()) {
            Node3 current = queue.poll();

            explored.add(current);
            if (lngLatHandler.isCloseTo(current.location, goal.location)) {
                endNode = current;
                List<Node3> path = getPathBack();
                if (path != null) {
                    return path;
                } else {
                    System.out.println("Error: getPathBack() returned null");
                    // Additional error handling if needed
                    return null;
                }
            }

            current.setNextNodes(noFlyZones);

            for (Node3 nextNode : current.nextNodes) {
                double cost = droneMoveDistance;
                double temp_g_scores = current.g_scores + cost;
                double temp_f_scores = temp_g_scores + nextNode.h_scores;

                if (explored.contains(nextNode) && temp_f_scores >= nextNode.f_scores) {
                    continue;
                } else if (!queue.contains(nextNode) || temp_f_scores < nextNode.f_scores) {
                    nextNode.parent = current;
                    nextNode.g_scores = temp_g_scores;
                    nextNode.f_scores = temp_f_scores;

                    if (!queue.contains(nextNode)) {
                        queue.add(nextNode);
                    }
                }
            }
        }

        System.out.println("Error: pathFindingAlgorithm did not find a valid path");
        return null;
    }
}
