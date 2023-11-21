package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static uk.ac.ed.inf.FlightPath.findShortestPath;
//import static uk.ac.ed.inf.OutputFileWriter.outputGeoJson;

public class App {
    /**
     * Passing in a valid order and testing that jar file works
     */
    public static void main(String[] args) {

        String todayDate = "2023-11-19";

        DeliveryManager deliveryManager = new DeliveryManager();
        deliveryManager.runDeliveryProcess(todayDate);




//        LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
//        LngLat startLocation = APPLETON_TOWER;
//        AStarSearch aStarSearch = new AStarSearch();
////        Node3 goBack = new Node3(APPLETON_TOWER);
//        List<Node3> movesMade = new ArrayList<>();
//
//        String todayDate = "2023-11-18";
//        OrdersToDeliver ordersToDeliver = new OrdersToDeliver(todayDate);
//        Queue<Order> orders = ordersToDeliver.getValidOrdersToDeliver();
////
//        System.out.println("No of orders for " + todayDate + ": " + orders.size()); //should be 37
//
//        // Create an instance of RetrieveRestData
//        RetrieveRestData restDataRetriever = new RetrieveRestData();
//
//        // Retrieve restaurant data from the REST API
//        Restaurant[] allRestaurants = restDataRetriever.retrieveRestaurantData();
//
//        // Get the first order from the queue
//        Order firstOrder = ordersToDeliver.getValidOrdersToDeliver().poll();
//
//
//        // Find the corresponding restaurant for the first order
//        Restaurant correspondingRestaurant = OrdersToDeliver.findCorrespondingRestaurant(firstOrder, allRestaurants);
//        LngLat restaurantLocation = correspondingRestaurant.location();
//        System.out.println(restaurantLocation);
//        Node3 restaurantDelivery = new Node3(restaurantLocation);
//        aStarSearch.pathFindingAlgorithm(startLocation, restaurantDelivery);
//        List<Node3> pathToRestaurant = aStarSearch.getPathBack();
//        //List<Node3> pathToRestaurantFixed = pathToRestaurant.subList(1, pathToRestaurant.size());
//        Node3 lastNodeToRestaurant = pathToRestaurant.get(pathToRestaurant.size() - 1);
//
//
//        //pathToRestaurant.add(Node3.createHoverNode(restaurantLocation, 999));
//        //List<Node3> pathToRestaurantFixed = pathToRestaurant.subList(1, pathToRestaurant.size());
//
//        //coorect
//        //List<Node3> pathFromRestaurant = new ArrayList<>(pathToRestaurant);
//
//        //Collections.reverse(pathFromRestaurant);
//        //pathToRestaurant.add(Node3.createHoverNode(lastNodeToRestaurant.location, lastNodeToRestaurant.destinationNode, 999));
//        //pathFromRestaurant.add(Node3.createHoverNode(startLocation, null,999));
//
////        System.out.println("Path to Restaurant:");
////        for (int i = 0; i < pathToRestaurant.size() - 1; i++) {
////            Node3 currentNode = pathToRestaurant.get(i);
////            Node3 nextNode = pathToRestaurant.get(i + 1);
////            System.out.println("Move from " + currentNode.location + " to " + nextNode.location + ", Angle: " + nextNode.angle);
////        }
////        System.out.println(); // Separate the output
////
////        System.out.println("Path from Restaurant:");
////        for (int i = 0; i < pathFromRestaurant.size() - 1; i++) {
////            Node3 currentNode = pathFromRestaurant.get(i);
////            Node3 nextNode = pathFromRestaurant.get(i + 1);
////
////            // Print "Angle: 999" for the hover node
////            if (currentNode.angle == 999) {
////                System.out.println("Hover at " + currentNode.location + ", Angle: 999");
////            } else {
////                System.out.println("Move from " + currentNode.location + " to " + nextNode.location + ", Angle: " + nextNode.angle);
////            }
////        }
//
////        Node3 pathFromRestaurantEndNode = aStarSearch.pathFindingAlgorithm(pathToRestaurantEndNode.location, goBack);
////        List<Node3> pathFromRestaurant = aStarSearch.getPathBack();
//        //List<Node3> pathFromRestaurantFixed = pathFromRestaurant.subList(1, pathFromRestaurant.size());
//        //when we do a for loop
////        startLocation = pathFromRestaurantEndNode.location;
//
//        //movesMade.addAll(pathToRestaurant);
////        movesMade.ADD HOVER NODE
//        //movesMade.addAll(pathFromRestaurant);
////
////
////
////
////
//        // Print the move from current node to next node
//        System.out.println("Moves from start to end:");
//        for (int i = 0; i < pathToRestaurant.size() - 1; i++) {
//            Node3 currentNode = pathToRestaurant.get(i);
//            Node3 nextNode = pathToRestaurant.get(i +1);
//            LngLat currentNodeLoc = currentNode.location;
//            // Print "Angle: 999" for the hover node
////            if (nextNode.angle == 999) {
////                System.out.println("Move from " + currentNode.location + " to " + nextNode.location + ", Angle: 999");
////            } else {
//            System.out.println("Move from " + currentNode.location + " to " + nextNode.location + ", Angle: " + nextNode.angle);
////            }
//        }
//        System.out.println(lastNodeToRestaurant.location + " " + lastNodeToRestaurant.location + ", Angle: " + Direction.HOVER);
//
//        Collections.reverse(pathToRestaurant);
//        System.out.println("Moves from start to end:");
//        for (int i = 0; i < pathToRestaurant.size() - 1; i++) {
//            Node3 currentNode = pathToRestaurant.get(i);
//            Node3 nextNode = pathToRestaurant.get(i +1);
//            // Print "Angle: 999" for the hover node
////            if (nextNode.angle == 999) {
////                System.out.println("Move from " + currentNode.location + " to " + nextNode.location + ", Angle: 999");
////            } else {
//            System.out.println("Move from " + currentNode.location + " to " + nextNode.location + ", Angle: " + currentNode.angle);
////            }
//        }
//        //same thimg here for reverse
//        List<DroneMove> droneMoves = deliveryManager.getDroneMoves();
//       // String date = // provide the date as needed
//
//
////        to output geoJson files
//        outputGeoJson(droneMoves, todayDate);



//        // Define start and end locations
//        LngLat endLocation = new LngLat(-3.1940174102783203, 55.94390696616939);
//
//        // Create a Node for the destination
//        Node3 destinationNode = new Node3(endLocation);
//
//        // Find the end node using A* algorithm
//        Node3 endNode = aStarSearch.pathFindingAlgorithm(startLocation, destinationNode);
//
//
//        // Get the path back
//        List<Node3> path = aStarSearch.getPathBack();
//
//        // Print the path
//        if (path != null) {
//            System.out.println("Path from start to end:");
//            for (int i = 0; i < path.size() - 1; i++) {
//                Node3 currentNode = path.get(i);
//                Node3 nextNode = path.get(i + 1);
//                System.out.println("Move from " + currentNode.location + " to " + nextNode.location + ", Angle: " + nextNode.angle);
//            }
//        } else {
//            System.out.println("No path found.");
//        }

    }
    }
//
//    AStarSearch aStarSearch = new AStarSearch();
//
//    // Define start and end locations
//    LngLat startLocation = new LngLat(-3.18, 55.94);
//    LngLat endLocation = new LngLat(-3.19, 55.94);
//
//    // Create a Node for the destination
//    Node3 destinationNode = new Node3(endLocation);
//
//    // Find the end node using A* algorithm
//    Node3 endNode = aStarSearch.pathFindingAlgorithm(startLocation, destinationNode);
//
//
//    // Get the path back
//    List<Node3> path = aStarSearch.getPathBack();
//
//// Print the path
//        if (path != null) {
//                System.out.println("Path from start to end:");
//                for (int i = 0; i < path.size() - 1; i++) {
//        Node3 currentNode = path.get(i);
//        Node3 nextNode = path.get(i + 1);
//        System.out.println("Move from " + currentNode.location + " to " + nextNode.location + ", Angle: " + nextNode.angle);
//        }
//        } else {
//        System.out.println("No path found.");
//        }
//
//        }
//        }

//        // Define start and end points
//        LngLat start = new LngLat(-3.186874, 55.944494);
//        LngLat end = new LngLat(3.1940174102783203, 55.94390696616939);
//
//        // Create nodes for start and end
//        Node2 startNode = new Node2(start, start);
//        Node2 endNode = new Node2(end, end);
//
//        // Create an instance of FlightPath
//        FlightPath flightPath = new FlightPath();
//
//        // Find the shortest path
//        boolean pathFound = flightPath.findShortestPath(startNode, endNode);
//
//        // Check if a path is found
//        if (pathFound) {
//            // Access the path from the FlightPath class
//            List<Node2> path = FlightPath.path;
//
//            // Print or process the path
//            for (Node2 node : path) {
//                System.out.println("Node: " + node.lng + ", " + node.lat);
//            }
//        } else {
//            System.out.println("No path found.");
//        }
//    }







//        String todayDate = "2023-11-07";
//        OrdersToDeliver ordersToDeliver = new OrdersToDeliver(todayDate);
//        Queue<Order> orders = ordersToDeliver.getValidOrdersToDeliver();
//
//        if (!orders.isEmpty()) {
//            System.out.println("Orders for " + todayDate + ":");
//            for (Order order : orders) {
//                System.out.println("Status Code: " + order.getOrderNo());
//            }
//        } else {
//            System.out.println("No orders found for " + todayDate);
//        }
//        System.out.println("No of orders for " + todayDate + ": " + orders.size());
//
//        // Create an instance of RetrieveRestData
//        RetrieveRestData restDataRetriever = new RetrieveRestData();
//
//        // Retrieve restaurant data from the REST API
//        Restaurant[] allRestaurants = restDaer from the queutaRetriever.retrieveRestaurantData();
//
//        // Get the first orde
//        Order firstOrder = ordersToDeliver.getValidOrdersToDeliver().poll();
//
//        // Find the corresponding restaurant for the first order
//        Restaurant correspondingRestaurant = MyRestaurant.findCorrespondingRestaurant(firstOrder, allRestaurants);
//
//        if (correspondingRestaurant != null) {
//            System.out.println("Corresponding restaurant found for the first order: " + correspondingRestaurant.name());
//        } else {
//            System.out.println("No corresponding restaurant found for the first order.");
//        }


//        // Call the retrieveNoFlyZones method
//        NamedRegion[] noFlyZones = restDataRetriever.retrieveNoFlyZones();
//        NamedRegion centralArea = restDataRetriever.retrieveCentralArea();
//
//        // Print information for the first NamedRegion
//        if (noFlyZones != null && noFlyZones.length > 0) {
//            NamedRegion firstNoFlyZone = noFlyZones[1];
//            System.out.println("No Fly Zone Name: " + firstNoFlyZone.name());
//            System.out.println("Vertices:");
//            for (LngLat lngLat : firstNoFlyZone.vertices()) {
//                System.out.println("  Lng: " + lngLat.lng() + ", Lat: " + lngLat.lat());
//            }
//        } else {
//            System.out.println("No data retrieved for No Fly Zones.");
//        }
//        if (centralArea != null) {
//            NamedRegion centralArea1 = centralArea;
//            System.out.println("No Fly Zone Name: " + centralArea1.name());
//            System.out.println("Vertices:");
//            for (LngLat lngLat : centralArea1.vertices()) {
//                System.out.println("  Lng: " + lngLat.lng() + ", Lat: " + lngLat.lat());
//            }
//        } else {
//            System.out.println("No data retrieved for No Fly Zones.");
//        }













//        String todayDate = "2023-10-31"; // Format: yyyy-MM-dd
//
//        // Create an instance of RetrieveRestData
//        RetrieveRestData restDataRetriever = new RetrieveRestData();
//
//        // Use RetrieveRestData to retrieve orders for today
//        Order[] orders = restDataRetriever.retrieveData("/orders/" + todayDate, Order[].class);
//
//
////        RetrieveRestData dataRetriever = new RetrieveRestData();
////        dataRetriever.retrieveOrderData();
//
//        // After calling retrieveOrderData(), you can access the retrieved orders
//        Order[] orders = restDataRetriever.getOrdersToDeliver();
//
//        if (orders != null && orders.length > 0) {
//            // Access the first order
//            Order firstOrder = orders[0];
//            String orderNo = firstOrder.getOrderNo();
//            System.out.println("Order Number: " + orderNo);
//        } else {
//            System.out.println("No orders retrieved.");
//        }
//        OrderValidator orderValidator = new OrderValidator();
//        Pizza[] pizzas = new Pizza[] { new Pizza("All Shrooms", 1000), new Pizza("Margarita", 2000)};
//        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "11/23", "123");
//        Order order = new Order("123", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 3100, pizzas, creditCardInfo);
//        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
//        LngLat restaurantLocation2 = new LngLat(-3.44455, 55.9422);
//        Pizza[] restaurantMenu = {
//                new Pizza("All Shrooms", 1400),
//                new Pizza("Margarita", 900)
//        };
//        Pizza[] restaurantMenu2 = {
//                new Pizza("Hawaiian", 1500),
//                new Pizza("Super Cheese", 3000),
//                new Pizza("Spicy Chicken Tikka", 1700)
//        };
//        DayOfWeek[] openingDays = {
//                DayOfWeek.MONDAY,
//                DayOfWeek.TUESDAY,
//                DayOfWeek.WEDNESDAY,
//                DayOfWeek.THURSDAY,
//                DayOfWeek.FRIDAY,
//                DayOfWeek.SATURDAY,
//                DayOfWeek.SUNDAY
//        };
//        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu), new Restaurant("Pizza Hut", restaurantLocation2, openingDays, restaurantMenu2)};
//        Order validatedOrder = orderValidator.validateOrder(order, definedRestaurants);
//        OrderStatus statusCode = validatedOrder.getOrderStatus();
//        OrderValidationCode validationCode = validatedOrder.getOrderValidationCode();
//        System.out.println(validationCode);
//        System.out.println(statusCode);

