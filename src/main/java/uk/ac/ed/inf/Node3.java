package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *This class is used to contain vital features that completely define a move that the drone makes.
 */
public class Node3 {
    /**
     * This variable stores the location that the drone is at the beginning of the move.
     */
    public LngLat location;

    /**
     * This stores the distance that the drone has travelled since it started attempting to get to its destination
     */
    public double g_scores;

    /**
     *This stores the euclidean distance from the current node to the destination the drone attempts to get to.
     */
    public double h_scores;

    /**
     * This stores the total score: the g_scores + the h_score.
     */
    public double f_scores;

    /**
     * This stores the list next nodes that the drone can get to from the current node.
     */
    public ArrayList<Node3> nextNodes = new ArrayList<>();

    /**
     * This stores the parent Node of the current node, i.e., the node the drone was at before getting to
     * the current node.
     */
    public Node3 parent;

    /**
     * This stores the angle the drone turns in regard to the compass directions to execute the move.
     */
    public double angle;

    /**
     * This stores the destination the drone is attempting to find a path to.
     */
    public Node3 destinationNode;

    // Instance of LngLatHandler
    private final LngLatHandler lngLatHandler = new LngLatHandler();


    /**
     * A parameterized constructor for Node, this constructor is used when we attempt to make a Node
     * for the destination of the path.
     * @param loc this stores the location that the node has (the location the drone attempts to get close to)
     */
    public Node3(LngLat loc) {
        location = loc;
    }

    /**
     * A parameterized constructor for Node, this constructor is used when we make a node for a location
     * that is not the required destination nor is it close to the destination.
     * @param loc this stores the location that the node has (i.e., the location the drone is currently at)
     * @param destination this stores the node that depicts the destination of the path the drone attempts to find.
     */

    public Node3(LngLat loc, Node3 destination) {
        location = loc;
        destinationNode = destination;

        // Calculating and storing the euclidean distance from the current location to the destination the
        // drone attempts to get to/ get close to.
        h_scores = lngLatHandler.distanceTo(loc, destinationNode.location);
    }


    /**
     * This method sets all the next nodes the  drone can travel to.
     */
    public void setNextNodes(NamedRegion[] noFlyZones) {
        // Iterating over every direction the drone can move
        for (Direction direction : Direction.values()) {
            // Stores the location the drone would be at if it were to move in this direction.
            LngLat nextPos = lngLatHandler.nextPosition(location, direction.angle);
            // Makes a new node for every one of the positions that the drone could get to.
            Node3 newNode = new Node3(nextPos, destinationNode);

            // sets the parent of this next node to the current node
            newNode.parent = this;

            // Check if the next node is in any of the no-fly zones
            if (newNode.pruneNextNodes(noFlyZones)) {
                // Add the new node to the list of next nodes only if it's not pruned
                nextNodes.add(newNode);
            }

            newNode.angle = direction.angle;
            // Add the new node to the list of next nodes
            nextNodes.add(newNode);
        }
    }

//    private boolean pruneNextNodes(Node3 current, Node3 adjacent) {
//        LngLat p1 = current.location;
//        LngLat p2 = adjacent.location;
//        RetrieveRestData restDataRetriever = new RetrieveRestData();
//        NamedRegion[] noFlyZoneList = restDataRetriever.retrieveNoFlyZones();
//
//        for (int i = 0, j = noFlyZoneList.length - 1; i < noFlyZoneList.length; j = i++) {
//            if (lngLatHandler.isInRegion(p1, noFlyZoneList[i]) &&
//
//        }
////        if (lngLatHandler.isInRegion(p2, region[0])) {
////            return false;
////
////
////        }
//        return true;
//    }

    public boolean pruneNextNodes(NamedRegion[] noFlyZones) {
        for (Iterator<Node3> iterator = nextNodes.iterator(); iterator.hasNext();) {
            Node3 nextNode = iterator.next();

            // Check if the next node is in any of the no-fly zones
            for (NamedRegion noFlyZone : noFlyZones) {
                if (lngLatHandler.isInRegion(nextNode.location, noFlyZone)) {
                    // Remove the next node from the list if it is in the no-fly zone
                    iterator.remove();
                    return false; // Node should be pruned
                }
            }
        }

        return true; // Node should not be pruned
    }



}
