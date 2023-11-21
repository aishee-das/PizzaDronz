package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.util.Queue;

public class DeliveryManagerNOT {
//
//    public void runDeliveryProcess(String date) {
//        LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
//        LngLat startLocation = APPLETON_TOWER;
//        AStarSearch aStarSearch = new AStarSearch();
//
//        String todayDate = date;
//        OrdersToDeliver ordersToDeliver = new OrdersToDeliver(todayDate);
//        Queue<Order> orders = ordersToDeliver.getValidOrdersToDeliver();
//        RetrieveRestData restDataRetriever = new RetrieveRestData();
//        Restaurant[] allRestaurants = restDataRetriever.retrieveRestaurantData();
//    }







//    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
//    private Queue<Order> validOrdersToDeliver;
//
//    private static String date;
//    List<Node3> movesMade = new ArrayList<>();
//
//    private List<Order> ordersDelivered = new ArrayList<>();

//    public DeliveryManager(String date) {
//        OrdersToDeliver ordersToDeliver = new OrdersToDeliver(date);
//        Queue<Order> orders = ordersToDeliver.getValidOrdersToDeliver();
//        System.out.println("No of orders for " + date + ": " + orders.size());
//        // Create an instance of RetrieveRestData
//        RetrieveRestData restDataRetriever = new RetrieveRestData();
//        // Retrieve restaurant data from the REST API
//        Restaurant[] allRestaurants = restDataRetriever.retrieveRestaurantData();
//        Order firstOrder = ordersToDeliver.getValidOrdersToDeliver().poll();
//        Restaurant correspondingRestaurant = OrdersToDeliver.findCorrespondingRestaurant(firstOrder, allRestaurants);
//        LngLat restaurantLocation = correspondingRestaurant.location();
//        System.out.println(restaurantLocation);
//
//        LngLat startLocation = APPLETON_TOWER;
//        AStarSearch aStarSearch = new AStarSearch();
//
//        Node3 restaurantDelivery = new Node3(restaurantLocation);
//        Node3 pathToRestaurantEndNode = aStarSearch.pathFindingAlgorithm(startLocation, restaurantDelivery);
//        List<Node3> pathToRestaurant = aStarSearch.getPathBack();
//        List<Node3> pathToRestaurantFixed = pathToRestaurant.subList(1, pathToRestaurant.size());
//        List<Node3> pathFromRestaurant = new ArrayList<>(pathToRestaurant);
//        Collections.reverse(pathFromRestaurant);
//        //        Node3 pathFromRestaurantEndNode = aStarSearch.pathFindingAlgorithm(pathToRestaurantEndNode.location, goBack);
////        List<Node3> pathFromRestaurant = aStarSearch.getPathBack();
//        List<Node3> pathFromRestaurantFixed = pathFromRestaurant.subList(1, pathFromRestaurant.size());
////        startLocation = pathFromRestaurantEndNode.location;
//
//        //     movesMade.addAll(pathToRestaurant);
//        //node  ADD HOVER NODE W ANGLE 999
//        movesMade.addAll(pathFromRestaurant);
////        hover node
////
////                ordersDeliver
//
//
//
//    }
//
//    LngLat startLocation = APPLETON_TOWER;
//    AStarSearch aStarSearch = new AStarSearch();
//    //        Node3 goBack = new Node3(APPLETON_TOWER);
//

//    String todayDate = "2023-11-07";
//    OrdersToDeliver ordersToDeliver = new OrdersToDeliver(todayDate);
//    Queue<Order> orders = ordersToDeliver.getValidOrdersToDeliver();
////
//    System.out.println("No of orders for " + todayDate + ": " + orders.size()); //should be 37
//
//    // Create an instance of RetrieveRestData
//    RetrieveRestData restDataRetriever = new RetrieveRestData();
//
//    // Retrieve restaurant data from the REST API
//    Restaurant[] allRestaurants = restDataRetriever.retrieveRestaurantData();
//
//    // Get the first order from the queue
//    Order firstOrder = ordersToDeliver.getValidOrdersToDeliver().poll();
//
//
//    // Find the corresponding restaurant for the first order
//    Restaurant correspondingRestaurant = OrdersToDeliver.findCorrespondingRestaurant(firstOrder, allRestaurants);
//    LngLat restaurantLocation = correspondingRestaurant.location();
//        System.out.println(restaurantLocation);
//    Node3 restaurantDelivery = new Node3(restaurantLocation);
//    Node3 pathToRestaurantEndNode = aStarSearch.pathFindingAlgorithm(startLocation, restaurantDelivery);
//    List<Node3> pathToRestaurant = aStarSearch.getPathBack();
//    List<Node3> pathToRestaurantFixed = pathToRestaurant.subList(1, pathToRestaurant.size());
//    List<Node3> pathFromRestaurant = new ArrayList<>(pathToRestaurant);
//        Collections.reverse(pathFromRestaurant);
//    //        Node3 pathFromRestaurantEndNode = aStarSearch.pathFindingAlgorithm(pathToRestaurantEndNode.location, goBack);
////        List<Node3> pathFromRestaurant = aStarSearch.getPathBack();
//    List<Node3> pathFromRestaurantFixed = pathFromRestaurant.subList(1, pathFromRestaurant.size());
////        startLocation = pathFromRestaurantEndNode.location;
//
//    //     movesMade.addAll(pathToRestaurant);
//        movesMade.addAll(pathFromRestaurant);
}
