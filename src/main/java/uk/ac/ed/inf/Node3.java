package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;

import java.util.ArrayList;

/**
 *This class is used to contain vital features that completely define a move that the drone makes.
 */
public class Node3 {
    /**
     * This final variable stores the location that the drone is at the beginning of the move.
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


    private final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

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
    /**
     * A parameterized constructor for Node, this constructor is used when we make a node for a location
     * that is not the required destination nor is it close to the destination.
     *
     * @param loc          this stores the location that the node has (i.e., the location the drone is currently at)
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
    public void setNextNodes() {
        // Iterating over every direction the drone can move
        for (Direction direction : Direction.values()) {
            // Stores the location the drone would be at if it were to move in this direction.
            LngLat nextPos = lngLatHandler.nextPosition(location, direction.angle);
            // Makes a new node for every one of the positions that the drone could get to.
            Node3 newNode = new Node3(nextPos, destinationNode);
            // sets the parent of this next node to the current node
            newNode.parent = this;
            // Add the new node to the list of next nodes
            nextNodes.add(newNode);
        }
    }



}
