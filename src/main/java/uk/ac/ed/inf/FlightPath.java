package uk.ac.ed.inf;

import org.springframework.boot.Banner;
import uk.ac.ed.inf.MyRestaurant;
import uk.ac.ed.inf.Node;
import uk.ac.ed.inf.RetrieveRestData;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.util.*;

public class FlightPath {

    private static final double[] DIRS = {(0.0), (22.5), (45.0), (67.5), (90.0), (112.5), (135.0), (157.5),
            (180.0), (202.5), (225.0), (247.5), (270.0), (292.5), (315.0), (337.5)};

    //public static HashSet<Node2> openSet;
    public static PriorityQueue<Node2> openSet;
    public static HashSet<Node2> closedSet;

    public static List<Node2> path;



    public FlightPath() {
        //openSet = new HashSet<>();
        openSet = new PriorityQueue<>(Comparator.comparingDouble(c -> c.f));
        closedSet = new HashSet<>();
        path = new ArrayList<>();
    }


    public static boolean findShortestPath(Node2 start, Node2 goal) {
        //add start to queue first
        openSet.add(start);

        //once there is element in the queue, then keep running
        while (!openSet.isEmpty()) {

            // get the cell with smallest cost
            Node2 current = openSet.poll();

//            //get the cell with smallest cost
//            Node2 current = openSet.iterator().next();
//            openSet.remove(current);

            //mark the cell to be visited
            closedSet.add(current);

            //find the goal: early exit
//            if (current.equals(goal)) {
            if (current.lng == goal.lng && current.lng == goal.lng) {
                //Reconstruct the path: trace by find the parent cell

                path = new ArrayList<>();
                while (current != null) {
                    path.add(current);
                    current = current.parent;
                }
                Collections.reverse(path);

                return true;
            }

            for (double dir: DIRS) {
                LngLatHandler lngLatHandler = new LngLatHandler();
                LngLat newLng = lngLatHandler.nextPosition(current.lng, dir);
                LngLat newLat = lngLatHandler.nextPosition(current.lat, dir);

                Node2 newNeighbour = new Node2(newLng, newLat);
                if (!closedSet.contains(new Node2(newLng, newLat))) {

                    //new movement is laways 1 cost even if its diagonal
                    double tentativeG = current.g + SystemConstants.DRONE_MOVE_DISTANCE;

                    //find the cell if it is in the frontier but not visited to see if cost updating is needed
                    Node2 existing_neighbour = findNeighbour(newLng, newLat);

                    if(existing_neighbour != null) {
                        //Chcek if this path is better than any previously generated path to the neighbor
                        if (tentativeG < existing_neighbour.g){
                            existing_neighbour.parent = current;
                            existing_neighbour.g = tentativeG;
                            existing_neighbour.h = heuristic(existing_neighbour, goal);
                            existing_neighbour.f = existing_neighbour.g + existing_neighbour.h;
                        }
                    }

                    else {
                        //or directly add this cell to the frontier
                        Node2 neighbour = new Node2(newLng, newLat);
                        neighbour.parent = current;
                        neighbour.g = tentativeG;
                        neighbour.h = heuristic(newNeighbour, goal);
                        neighbour.f = newNeighbour.g + newNeighbour.h;

                        openSet.add(neighbour);
                    }
                }
                //neighbour cell location

            }
        }

        //No path found
        return  false;

    }

      public static Node2 findNeighbour(LngLat lng, LngLat lat) {
          if (openSet.isEmpty()) {
              return null;
          }

          Iterator<Node2> iterator = openSet.iterator();

          Node2 find = null;
          while (iterator.hasNext()) {
              Node2 next = iterator.next();
              if (next.lng == lng && next.lat == lat) {
                  find = next;
                  break;
              }
          }
          return find;
      }

//        if (openSet.isEmpty()) {
//            return null;
//        }
//
//        Iterator<Node2> iterator = openSet.iterator();
//
//        Node2 find = null;
//        while (iterator.hasNext()) {
//            Node2 next = iterator.next();
//            if (next.lng == lng && next.lat == lat){
//                find = next;
//                break;
//            }
//        }
//        return find;


    // Helper function to check if a cell is part of the path
    public static boolean isInPath(LngLat lng, LngLat lat) {
        for (Node2 node : path) {
            if (node.lng == lng && node.lat == lat) {
                return true;
            }
        }
        return false;
    }

    public static int heuristic(Node2 a, Node2 b) {
        // A simple heuristic: Manhattan distance
        double lngDiff = a.lng.lng() - b.lng.lng();
        double latDiff = a.lat.lat() - b.lat.lat();
        double distance = Math.sqrt(lngDiff * lngDiff + latDiff * latDiff);

        // You may need to adjust the return type based on your needs.
        return (int) distance;
    }
}



//    public FlightPath(Queue<Order> validOrdersToDeliver, NamedRegion centralArea, NamedRegion[] noFlyZones) {
//        this.validOrdersToDeliver = validOrdersToDeliver;
//        this.centralArea = centralArea;
//        this.noFlyZones = noFlyZones;
//    }

//    public void generateFlightPaths() {
//        while (!validOrdersToDeliver.isEmpty()) {
//            Order currentOrder = validOrdersToDeliver.poll();
//            Restaurant[] allRestaurants = restDataRetriever.retrieveRestaurantData();
//            Restaurant matchingRestaurant = MyRestaurant.findCorrespondingRestaurant(currentOrder, allRestaurants);
//            LngLat restaurantLoc = matchingRestaurant.location();
//
//
//            // Generate flight path from Appleton Tower to the restaurant
//            List<LngLat> toRestaurantPath = findShortestPath(APPLETON_TOWER, restaurantLoc);
//
//
//            // Generate flight path from restaurant to Appleton Tower (reverse of the path to the restaurant)
//            List<LngLat> toAppletonTowerPath = new ArrayList<>(toRestaurantPath);
//            Collections.reverse(toAppletonTowerPath);
//
//            // Print or store the flight paths for the current order
//            System.out.println("Flight path to restaurant for Order " + currentOrder.getOrderNo() + ": " + toRestaurantPath);
//            System.out.println("Flight path to Appleton Tower for Order " + currentOrder.getOrderNo() + ": " + toAppletonTowerPath);
//        }
