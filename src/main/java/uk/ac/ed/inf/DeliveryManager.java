package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class DeliveryManager {

    public ArrayList<Order> allOrders;

    private Queue<Order> validOrders;

    private List<DroneMove> droneMoves = new ArrayList<>();

        public void runDeliveryProcess(String date) {
            LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
            LngLat startLocation = APPLETON_TOWER;
            AStarSearch aStarSearch = new AStarSearch();
            ArrayList<DroneMove> droneMoves = new ArrayList<>();
            this.droneMoves = droneMoves;

            String todayDate = date;
            RetrieveRestData restDataRetriever = new RetrieveRestData();
//            Order[] allOrders = restDataRetriever.retrieveOrderDataByDate(todayDate);
//            setValidOrdersQueue(todayDate);

            OrdersToDeliver ordersToDeliver = new OrdersToDeliver(todayDate);
            Queue<Order> orders = ordersToDeliver.getValidOrdersToDeliver();

            Restaurant[] allRestaurants = restDataRetriever.retrieveRestaurantData();
            Order firstOrder = ordersToDeliver.getValidOrdersToDeliver().poll();
            String orderNo = firstOrder.getOrderNo();
            // Check if there is a second order
            if (!orders.isEmpty()) {
                // Dequeue the second order
                Order secondOrder = orders.poll();
                Restaurant correspondingRestaurant = OrdersToDeliver.findCorrespondingRestaurant(secondOrder, allRestaurants);
                LngLat restaurantLocation = correspondingRestaurant.location();
                Node3 restaurantDelivery = new Node3(restaurantLocation);
                aStarSearch.pathFindingAlgorithm(startLocation, restaurantDelivery);
                List<Node3> pathToRestaurant = aStarSearch.getPathBack();
                Node3 lastNodeToRestaurant = pathToRestaurant.get(pathToRestaurant.size() - 1);

                // Forward traversal
                for (int i = 0; i < pathToRestaurant.size() - 1; i++) {
                    Node3 currentNode = pathToRestaurant.get(i);
                    Node3 nextNode = pathToRestaurant.get(i + 1);
                    double fromLongitude = currentNode.location.lng();
                    double fromLatitude = currentNode.location.lat();
                    double angle = nextNode.angle;
                    double toLongitude = nextNode.location.lng();
                    double toLatitude = nextNode.location.lat();

                    DroneMove move = new DroneMove(orderNo, fromLongitude, fromLatitude, angle, toLongitude, toLatitude);
                    droneMoves.add(move);
                }

        // Hover node
                LngLat hoverCurrentNodeLoc = lastNodeToRestaurant.location;
                LngLat hoverNextNodeLoc = lastNodeToRestaurant.location;
                double fromLongitude = hoverCurrentNodeLoc.lng();
                double fromLatitude = hoverCurrentNodeLoc.lat();
                double angle = 999;
                double toLongitude = hoverNextNodeLoc.lng();
                double toLatitude = hoverNextNodeLoc.lat();

                DroneMove hoverMove = new DroneMove(orderNo, fromLongitude, fromLatitude, angle, toLongitude, toLatitude);
                droneMoves.add(hoverMove);

        // Reverse traversal
                Collections.reverse(pathToRestaurant);

                for (int i = 0; i < pathToRestaurant.size() - 1; i++) {
                    Node3 currentNode = pathToRestaurant.get(i);
                    Node3 nextNode = pathToRestaurant.get(i + 1);
        //                LngLat reverseCurrentNodeLoc = currentNode.location;
        //                LngLat reverseNextNodeLoc = nextNode.location;
        //                double reverseAngle = currentNode.angle;
                    double fromLongitude1 = currentNode.location.lng();
                    double fromLatitude1 = currentNode.location.lat();
                    double angle1 = currentNode.angle;
                    double toLongitude1 = nextNode.location.lng();
                    double toLatitude1 = nextNode.location.lat();

                    DroneMove reverseMove = new DroneMove(orderNo, fromLongitude1, fromLatitude1, angle1, toLongitude1, toLatitude1);
                    droneMoves.add(reverseMove);
                }

                // Hover node
                double hoverReverseNodeLocFromLong = startLocation.lng();
                double hoverReverseNextNodeLocFromLat = startLocation.lat();
                double hoverReverseAngle = 999;
                double hoverReverseNodeLocToLong = startLocation.lng();
                double hoverReverseNextNodeLocToLat = startLocation.lat();

                DroneMove hoverReverseMove = new DroneMove(orderNo, hoverReverseNodeLocFromLong, hoverReverseNextNodeLocFromLat, hoverReverseAngle, hoverReverseNodeLocToLong, hoverReverseNextNodeLocToLat);
                droneMoves.add(hoverReverseMove);

                secondOrder.setOrderStatus(OrderStatus.DELIVERED);


                writeJsonToFile(droneMoves, todayDate);
//                deliveriesJsonWriter(Arrays.asList(allOrders), todayDate);

//                for (DroneMove move : droneMoves) {
//                    System.out.println("From Longitude " + move.getCurrentNodeFromLng() + " " + "From Latitude " + move.getCurrentNodeFromLat() + ", Angle: " + move.getAngle() + " " + "To Longitude" + move.getNextNodeFromLng() + " " + "To Latitude" + move.getNextNodeFromLat());
//                }




                // Getter method for droneMoves



            }


        }


    private void setValidOrdersQueue(String date) {
        this.allOrders = new ArrayList<>();
        this.validOrders = new LinkedList<>();
        OrderValidator orderValidator = new OrderValidator();
        // Create an instance of RetrieveRestData
        RetrieveRestData restDataRetriever = new RetrieveRestData();
        Order[] orders = restDataRetriever.retrieveOrderDataByDate(date);


        if (orders != null) {
            for (Order order : orders) {
                allOrders.add(order);
            }
        }
        // Retrieve restaurant data from the REST API
        Restaurant[] definedRestaurants = restDataRetriever.retrieveRestaurantData();
        for (Order order : allOrders) {
            Order validatedOrder = orderValidator.validateOrder(order, definedRestaurants);

            if (validatedOrder.getOrderValidationCode() == OrderValidationCode.NO_ERROR &&
                    validatedOrder.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED) {
                validOrders.add(validatedOrder);
            }
        }

    }
    //flightpath file - records every move of the drone
    private void writeJsonToFile(List<DroneMove> moves, String date) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert DroneMove objects to JSON
            String jsonString = objectMapper.writeValueAsString(moves);

            // Specify the directory path
            String directoryPath = "resultfiles/";

            // Write string to file in the specified directory
            String fileName = directoryPath + "flightpath-" + date + ".json";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // New method to write deliveries and non-deliveries to a JSON file
//    public void writeDeliveryDetailsToJson(List<Order> allOrders, String date) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ArrayNode ordersArray = objectMapper.createArrayNode();
//
//        for (Order order : allOrders) {
//            ObjectNode orderNode = objectMapper.createObjectNode();
//            orderNode.put("orderNo", order.getOrderNo());
//            orderNode.put("orderStatus", order.getOrderStatus().toString());
//            orderNode.put("orderValidationCode", order.getOrderValidationCode().toString());
//
//
//            ordersArray.add(orderNode);
//        }
//
//        try {
//            // Create a directory if it doesn't exist
//
//            // Write JSON to file
//            File outputFile = new File("resultfiles/deliveries-" + date + ".json");
//            try (FileWriter writer = new FileWriter(outputFile)) {
//                objectMapper.writeValue(writer, ordersArray);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public void writeDeliveryDetailsToJson(DeliveryDetails, String date) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<DeliveryDetails> deliveryDetailsList = new ArrayList<>();
//
//        for (Order order : allOrders) {
//            String orderNo = order.getOrderNo();
//            OrderStatus orderStatus = order.getOrderStatus();
//            OrderValidationCode orderValidationCode = order.getOrderValidationCode();
////            int costInPence = calculateCostInPence(order); // Replace with your logic
//
//            DeliveryDetails deliveryDetails = new DeliveryDetails(orderNo, orderStatus, orderValidationCode);
//            deliveryDetailsList.add(deliveryDetails);
//        }
//
//        try {
//            // Convert DeliveryDetails objects to JSON
//            String jsonString = objectMapper.writeValueAsString(deliveryDetailsList);
//
//            // Specify the directory path
//            String directoryPath = "resultfiles/";
//
//            // Write string to file in the specified directory
//            String fileName = directoryPath + "deliveries-" + date + ".json";
//            try (FileWriter writer = new FileWriter(fileName)) {
//                writer.write(jsonString);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    private JSONObject deliveriesHelper(Order order) {
        JSONObject pathObject = new JSONObject();

        pathObject.put("orderNo", order.getOrderNo());
        pathObject.put("orderStatus", order.getOrderStatus().toString()); // Assuming OrderStatus is an enum
        pathObject.put("orderValidationCode", order.getOrderValidationCode().toString()); // Assuming OrderValidationCode is an enum
//        pathObject.put("costInPence", order.priceTotalInPence());

        return pathObject;
    }

    public void deliveriesJsonWriter(List<Order> ordersList, String date) {
        try {
            // Create the resultfiles directory if it doesn't exist
            String directoryPath = "resultfiles/";

            JSONArray deliveries = new JSONArray();

            for (Order order : ordersList) {
                JSONObject pathObject = deliveriesHelper(order);
                deliveries.add(pathObject);
            }
            String fileName = directoryPath + "flightpath-" + date + ".json";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(deliveries.toJSONString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Getter method for droneMoves
    public List<DroneMove> getDroneMoves() {
        return droneMoves;

        }
}
