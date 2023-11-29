package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.data.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//import static uk.ac.ed.inf.OutputFileWriter.outputGeoJson;

public class App {
    /**
     * Passing in a valid order and testing that jar file works
     */
    public static void main(String[] args) {
        // Start timer
        long StartTime = System.currentTimeMillis();

        // Input validation for arguments
        if (args.length != 2) {
            throw new IllegalArgumentException("Arg length error: Incorrect number of arguments");
        }
        String date = args[0];
        String url = args[1];

        RetrieveRestData retrieveRestData = new RetrieveRestData(url);
        // Fetch data from API
        Order[] orders = retrieveRestData.retrieveOrderDataByDate(date);
        if (orders.length == 0) {
            throw new IllegalArgumentException("Order error: No orders for this date");
        }
        Restaurant[] restaurants = retrieveRestData.retrieveRestaurantData();
        NamedRegion centralArea = retrieveRestData.retrieveCentralArea();
        NamedRegion[] noFlyZones = retrieveRestData.retrieveNoFlyZones();

        System.out.println("Data fetched successfully\n");

        // Validate orders, adding all valid orders to a list "validOrders"
        OrderValidator validator = new OrderValidator();
        List<Order> validOrders = new ArrayList<>();
        for (Order order : orders) {
            Order orderCheck = validator.validateOrder(order, restaurants);
            if (orderCheck.getOrderStatus() != OrderStatus.INVALID) { //change to VALID BUT NOT DELIVERED
                validOrders.add(order);
            }
        }

        System.out.println("Number of validated orders of " + date + ": " + validOrders.size());

        // Calculate optimal route
        DroneRoutePlanner routePlanner = new DroneRoutePlanner(noFlyZones, centralArea, restaurants, validOrders);
        List<DroneMove> paths = routePlanner.findPaths();

        // Create results folder in root directory
        new File("resultfiles").mkdirs();

        // Creates the delivery JSON file
        String deliveryFileName="deliveries-" + date + ".json";
        try (FileWriter fileWriter = new FileWriter("resultfiles/" + deliveryFileName)) {
            fileWriter.write(DeliveryJsonWriter.writeDeliveryJson(orders));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log delivery JSON file creation
        System.out.println("Created " + deliveryFileName);

        // Creates the flightpath JSON file
        String flightpathFileName="flightpath-" + date + ".json";
        try (FileWriter fileWriter = new FileWriter("resultfiles/" + flightpathFileName)) {
            fileWriter.write(FlightpathJsonWriter.writeFlightpathJson(paths));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log FlightPath JSON file creation
        System.out.println("Created " + flightpathFileName);

        // Creates the drone GeoJSON file
        String droneFileName="drone-" + date + ".geojson";
        try (FileWriter fileWriter = new FileWriter("resultfiles/" + droneFileName)) {
            fileWriter.write(GeoJsonWriter.writeGeoJson(paths));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log drone GeoJSON file creation
        System.out.println("Created " + droneFileName);

        // Log runtime
        long EndTime = System.currentTimeMillis();
        double elapsedTime = (double) (EndTime - StartTime)/1000;
        System.out.println("Runtime: " + elapsedTime + " seconds");

    }
//        String date = args[0];
//        String BASEURL = args[1];
//        validateTerminalInputs(args);
//        RetrieveRestData retrieveRestData = new RetrieveRestData(BASEURL);
//        NamedRegion[] noFlyZones = retrieveRestData.retrieveNoFlyZones();
//        NamedRegion Central = retrieveRestData.retrieveCentralArea();
//
//        // Check if the API is alive
//        String apiStatus = retrieveRestData.checkApiStatus();
//
//        if (apiStatus.equals("false")) {
//            System.err.println("Error: API is not currently alive");
//        } else if (apiStatus.equals("error")) {
//            System.err.println("Error: Unable to determine API status");
//        } else {
//            List<String> orderNumValid = new ArrayList<>();
//            List<Restaurant> restsToVisit = new ArrayList<>();
//
//            Restaurant[] restrnts = retrieveRestData.retrieveRestaurantData();
//            Order[] orderList = retrieveRestData.retrieveOrderDataByDate(date);
//            List<Order> validatedList = new ArrayList<>();
//
//            if (orderList != null) {
//                if (orderList.length == 0) {
//                    System.err.println("No orders for date specified");
//                } else {
//                    directory.main();
//                    for (Order order : orderList) {
//                        Order validatedOrder =
//                                new OrderValidator().validateOrder(order, restrnts);
//                        if (validatedOrder != null) {
//                            if (validatedOrder.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED) {
//                                orderNumValid.add(order.getOrderNo());
//                                restsToVisit.add(getRestrnt(restrnts, validatedOrder));
//                            }
//                            validatedList.add(validatedOrder);
//
//                            //create OrderJSON from input validatedOrder
//                        } else {
//                            System.err.println("no order validated");
//                        }
//                    }
//                    orderJSON.main(validatedList, date);
//                    pathGEO.main(orderNumValid, restsToVisit, BASEURL, date, noFlyZones, Central);
//                }
//            }
//        }
//    }
//        public static Restaurant getRestrnt(Restaurant[] restrnts,Order validOrder){
//        for (Restaurant definedRestaurant : restrnts) {
//            if (Arrays.asList(definedRestaurant.menu()).contains(validOrder.getPizzasInOrder()[0])) {
//                return definedRestaurant;
//            }
//        }
//        return null;
//      }
//    /**
//     * this helper method, validates all the inputs passed to the Controller.
//     * @param args it gets the date at which the drone delivers the order for (as a String),
//     *             the URL to the server's base address (As a String),
//     *             and a random seed (as a String).
//     */
//    private static void validateTerminalInputs(String[] args){
//        inputsNumberValidator(args);
//        isValidDateFormat(args[0]);
//        validateURL(args[1]);
//    }
//
//    private static String validateURL(String urlToCheck) {
//        try {
//            // Check if the URL is valid
//            new URL(urlToCheck);
//
//            return urlToCheck;
//        } catch (MalformedURLException e) {
//            System.err.println("Error: Invalid URL specified");
//            System.exit(1); // You might want to handle this differently based on your application's requirements
//            return null; // unreachable, but added to satisfy the compiler
//        }
//    }
//
//    /**
//     * This method checks if the number of arguments passed to the program are incorrect.
//     * In which case the programs is forced to stop.
//     * @param args The arguments passed to the program.
//     */
//    private static void inputsNumberValidator(String[] args){
//        if(args.length!=2){
//            //Prints an error message stating the error
//            System.err.println("Not all required inputs were passed");
//            //Exits the program
//            System.exit(1);
//        }
//    }
//
//    /**
//     * This method checks if the provided date string is in the correct format "yyyy-MM-dd".
//     *
//     * @param dateString The date string to be validated.
//     * @return True if the date is in the correct format, false otherwise.
//     */
//    private static boolean isValidDateFormat(String dateString) {
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        try {
//            dateFormatter.parse(dateString);
//            return true; // Parsing successful, date is in the correct format
//        } catch (DateTimeParseException e) {
//            System.err.println("Error: Invalid date format. Please use the format 'yyyy-MM-dd'.");
//            return false;
//        }
//    }


}





























//    LngLat restaurantLngLat = new LngLat(-3.1930174102783203,55.94550696616939);
//    Order order = new Order("1234",
//            LocalDate.now(),
//            OrderStatus.UNDEFINED,
//            OrderValidationCode.UNDEFINED,
//            2100,
//            new Pizza[]{new Pizza("Margarita", 1100), new Pizza("Hawaiian", 900)},
//            null);
//        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
//                "12/26",
//                "123"));
//                DayOfWeek[] openingDays = {DayOfWeek.THURSDAY, DayOfWeek.TUESDAY};
//                Pizza[] menu = {new Pizza("Margarita", 1100), new Pizza("Hawaiian", 900)};
//                Restaurant[] restaurants = {new Restaurant("Sodeberg Pavillion", restaurantLngLat, openingDays, menu)};
//
//                String BASEURL = "https://ilp-rest.azurewebsites.net"; // Replace with your actual BASEURL
//                String date = LocalDate.now().toString(); // Use the current date
//                RetrieveRestData retrieveRestData = new RetrieveRestData();
//                NamedRegion[] NoFlyZones = retrieveRestData.retrieveNoFlyZones();
//                NamedRegion Central = retrieveRestData.retrieveCentralArea();
//
//                //NamedRegion[] NoFlyZones = retrieveRestData.retrieveNoFlyZones();
////            NamedRegion[] NoFlyZones = new NamedRegion[]{new NamedRegion("George Square Area",
////                    new LngLat[]{new LngLat(-3.190578818321228, 55.94702412577528),
////                            new LngLat(-3.1899887323379517, 55.94184650540911),
////                            new LngLat(-3.187097311019897, 55.94228811724263),
////                            new LngLat(-3.187682032585144, 55.944477740393744),
////                            new LngLat(-3.190578818321228, 55.94702412577528)})};
//
////            NamedRegion[] NoFlyZones = new NamedRegion[]{new NamedRegion("George Square Area",
////                    new LngLat[]{new LngLat(-3.1922869215011597, 55.946135152517735),
////                            new LngLat(-3.1892869215011597, 55.94613515251773),
////                            new LngLat(-3.1892869215011597, 55.94545152517735),
////                            new LngLat(-3.1922869215011597, 55.945535152517735),
////                            new LngLat(-3.1922869215011597, 55.946135152517735)}),new NamedRegion("George Square Area",
////                    new LngLat[]{new LngLat(-3.1922869215011597, 55.946135152517735),
////                            new LngLat(-3.1892869215011597, 55.94613515251773),
////                            new LngLat(-3.1892869215011597, 55.94545152517735),
////                            new LngLat(-3.1922869215011597, 55.945535152517735),
////                            new LngLat(-3.1922869215011597, 55.946135152517735)})};
//                //NamedRegion Central = retrieveRestData.retrieveCentralArea();
//                if (!(new RESThandler(BASEURL).isAlive())) {
//                System.out.println("Website is not currently alive");
//                } else {
//                List<String> orderNumValid = new ArrayList<>();
//        List<Restaurant> restsToVisit = new ArrayList<>();
//
//        orderJSON.main(new Order[]{order}, date);
//
//        Order validatedOrder = new OrderValidator().validateOrder(order, restaurants);
//
//        if (validatedOrder != null) {
//        if (validatedOrder.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED) {
//        orderNumValid.add(order.getOrderNo());
//        restsToVisit.add(getRestrnt(restaurants, validatedOrder));
//
//        // Process the order and restaurant data
//        pathGEO.main(orderNumValid, restsToVisit, BASEURL, date, NoFlyZones, Central);
//        } else {
//        System.out.println("Order is not valid or already delivered");
//        }
//        } else {
//        System.out.println("No order validated");
//        }
//        }
//
//        }

//            String BASEURL = args[1];
//            String date = args[0];
//            RetrieveRestData retrieveRestData = new RetrieveRestData();
//
//            if (!(new RESThandler(BASEURL).isAlive())){
//                System.out.println("Website is not currently alive");
//            } else {
//
//
//                List<String> orderNumValid = new ArrayList<>();
//                List<Restaurant> restsToVisit = new ArrayList<>();
//
//                Restaurant[] restrnts = retrieveRestData.retrieveRestaurantData();
//                Order[] orderList = retrieveRestData.retrieveOrderDataByDate(date);
//                if (orderList != null) {
//                    orderJSON.main(orderList, date);
//                    for (Order order : orderList) {
//                        Order validatedOrder =
//                                new OrderValidator().validateOrder(order, restrnts);
//                        if (validatedOrder != null) {
//                            if (validatedOrder.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED) {
//                                orderNumValid.add(order.getOrderNo());
//                                restsToVisit.add(getRestrnt(restrnts, validatedOrder));
//                            }
//
//                            //create OrderJSON from input validatedOrder
//                        } else {
//                            System.out.println("no order validated");
//                        }
//                    }
//                    pathGEO.main(orderNumValid, restsToVisit, BASEURL, date);
//                }
//            }
//        }
//
//        public static Restaurant getRestrnt(Restaurant[] restrnts,Order validOrder){
//            for (Restaurant definedRestaurant : restrnts) {
//                if (Arrays.asList(definedRestaurant.menu()).contains(validOrder.getPizzasInOrder()[0])) {
//                    return definedRestaurant;
//                }
//            }
//            return null; //this would never be reached
//        }
//
//    }

//        NamedRegion centralArea = new NamedRegion(SystemConstants.CENTRAL_REGION_NAME,
//                new LngLat[]{new LngLat(-3.192473, 55.946233),
//                        new LngLat(-3.192473, 55.942617),
//                        new LngLat(-3.184319, 55.942617),
//                        new LngLat(-3.184319, 55.946233) });

        //LngLat restaurantLngLat = new LngLat(-3.1930174102783203,55.94550696616939); //-3.1940174102783203,55.94390696616939
//        Order order = new Order("1234",
//                LocalDate.now(),
//                OrderStatus.UNDEFINED,
//                OrderValidationCode.UNDEFINED,
//                2100,
//                new Pizza[]{new Pizza("Margarita", 1100), new Pizza("Hawaiian", 900)},
//                null);
//        order.setCreditCardInformation(new CreditCardInformation("1234567891123456",
//                "12/26",
//                "123"));
        //CoordinatePoint restaurant = new CoordinatePoint(restaurantLngLat.lng(), restaurantLngLat.lat());
//        DayOfWeek[] openingDays = {DayOfWeek.THURSDAY, DayOfWeek.MONDAY};
//        Pizza[] menu = {new Pizza("Margarita", 1100), new Pizza("Hawaiian", 900)};
//        Restaurant[] restaurants = new Restaurant[]{new Restaurant("Sodeberg Pavillion", restaurantLngLat, openingDays, menu)};

//        long startTime = System.currentTimeMillis();
//        NamedRegion[] noFlyZones = new NamedRegion[]{new NamedRegion("George Square Area",
//                new LngLat[]{new LngLat(-3.191578818321228, 55.94402412577528),
//                        new LngLat(-3.189987323379517, 55.94284650540911),
//                        new LngLat(-3.187097311019897, 55.94328811724263),
//                        new LngLat(-3.187682032585144, 55.944477740393744),
//                        new LngLat(-3.191578818321228, 55.94402412577528)})};

//        String todayDate = "2023-11-27";
//
//        new DeliveryManager1(todayDate);
//        long endTime = System.currentTimeMillis();
//        long elapsedTime = endTime - startTime;
//
//        // Convert the elapsed time to seconds
//        double elapsedTimeInSeconds = elapsedTime / 1000.0;
//
//        System.out.println("Time taken: " + elapsedTimeInSeconds + " seconds");
//        String url = "https://ilp-rest.azurewebsites.net/";
//        LocalDate date = LocalDate.now().minusDays(2);
//
//        LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
//
//        RestService restService = new RestService(url);
//        OrderValidator orderValidator = new OrderValidator();
//
//        Restaurant[] openRestaurants = restService.getOpenRestaurants(date);
//        Order[] orders = restService.getOrdersForDate(date);
//        AStar router = new AStar(restService.getCentralArea(), noFlyZones);
//
//        HashMap<Restaurant, ArrayList<PathNode>> restaurantPaths = new HashMap<>();
//        HashMap<String, ArrayList<PathNode>> paths = new HashMap<>();
//
//        System.out.println("Orders for " + date + ":");
//
//        int counter = 1;
//        for (Order order : orders) {
//            order = orderValidator.validateOrder(order, openRestaurants);
//            if (order.getOrderStatus() == VALID_BUT_NOT_DELIVERED) {
//                try {
//                    Restaurant orderedRestaurant = getRestaurant(order, openRestaurants);
//
//                    if (restaurantPaths.containsKey(orderedRestaurant)) {
//                        ArrayList<PathNode> orderPath = restaurantPaths.get(orderedRestaurant);
//
//                        paths.put(order.getOrderNo(), orderPath);
//                        Collections.reverse(orderPath);
//                        paths.put(order.getOrderNo(), orderPath);
//
//                    } else {
//                        assert orderedRestaurant != null;
//                        ArrayList<PathNode> path = router.getRoute(APPLETON_TOWER, orderedRestaurant.location());
//
//                        restaurantPaths.put(orderedRestaurant, path);
//                        System.out.println("> Route for restaurant " + orderedRestaurant.name() + " completed");
//
//                        if (path != null) {
//                            paths.put(order.getOrderNo(), path);
//                            Collections.reverse(path);
//                            paths.put(order.getOrderNo(), path);
//                        }
//
//                    }
//
//                    order.setOrderStatus(OrderStatus.DELIVERED);
//                    System.out.println("    > Order " + order.getOrderNo() + " route completed [" + (counter++) + "/" + (orders.length) + "]");
//                    for (Map.Entry<String, ArrayList<PathNode>> entry : paths.entrySet()) {
//                        String orderNo = entry.getKey();
//                        ArrayList<PathNode> path = entry.getValue();
//
//                        System.out.println("Path for Order " + orderNo + ":");
//
//                        for (PathNode node : path) {
//                            System.out.println("From: " + (node.prev() != null ? node.prev() : "Start") +
//                                    " To: " + node.curr() +
//                                    " Angle: " + node.angle() +
//                                    " Step: " + node.step());
//                        }
//
////                        // Break out of the loop after processing the first order
////                        break;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
////
//        FileOutputter fileOutputter = new FileOutputter();
//
//        fileOutputter.writeDeliveries("/deliveries-" + date + ".json", orders);
//        fileOutputter.writeFlightpathJson("/flightpath-" + date + ".json", paths);
//        fileOutputter.writePathGeoJson("/drone-" + date + ".geojson", paths);

    /**
     * Get the restaurant for the order
     *
     * @param order:           The order whose restaurant we want to determine
     * @param openRestaurants: The list of restaurants currently open
     * @return: The restaurant ordered from
     */
//    private static Restaurant getRestaurant(Order order, Restaurant[] openRestaurants) {
//        for (Restaurant restaurant : openRestaurants) {
//            // If menu contains pizza - the restaurant is found
//            if (Arrays.asList(restaurant.menu()).contains(order.getPizzasInOrder()[0])) {
//                return restaurant;
//            }
//        }
//        // Should not be reached
//        return null;
//    }
//}

//        long endTime = System.currentTimeMillis();
//        long elapsedTime = endTime - startTime;
//
//        // Convert the elapsed time to seconds
//        double elapsedTimeInSeconds = elapsedTime / 1000.0;
//
//        System.out.println("Time taken: " + elapsedTimeInSeconds + " seconds");
//        String todayDate = "2023-11-21";
//        OrdersToDeliver ordersToDeliver = new OrdersToDeliver(todayDate);
//        Queue<Order> orders = ordersToDeliver.getValidOrdersToDeliver();
////
//        System.out.println("No of orders for " + todayDate + ": " + orders.size()); //should be 37
//
//        while (!orders.isEmpty()) {
//            Order currentOrder = orders.poll();
//            System.out.println(currentOrder.getOrderNo());
//        }




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

//    }

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

