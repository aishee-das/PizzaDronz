package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class App {
    /**
     * Passing in a valid order and testing that jar file works
     */
    public static void main(String[] args) {
        String todayDate = "2023-11-07";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate date = LocalDate.parse(todayDate, formatter); // Parse the string into a LocalDate
//        String formattedDate = date.format(formatter);

        OrdersToDeliver ordersToDeliver = new OrdersToDeliver(todayDate);
        Queue<Order> orders = ordersToDeliver.getOrdersToDeliver();

        if (!orders.isEmpty()) {
            System.out.println("Orders for " + todayDate + ":");
            for (Order order : orders) {
                System.out.println("Status Code: " + order.getOrderNo());
                // Print other order details as needed
            }
        } else {
            System.out.println("No orders found for " + todayDate);
        }
        System.out.println("No of orders for " + todayDate + ": " + orders.size());

        //OrdersToDeliver ordersToDeliver = new OrdersToDeliver("2023-09-01");
        // Create an instance of RetrieveRestData
        RetrieveRestData restDataRetriever = new RetrieveRestData();

        // Retrieve restaurant data from the REST API
        Restaurant[] allRestaurants = restDataRetriever.retrieveRestaurantData();

        // Get the first order from the queue
        Order firstOrder = ordersToDeliver.getOrdersToDeliver().poll();

//        // Assuming you have retrieved the restaurants data from the REST API
//        String restaurantsEndpoint = "/restaurants";
//        RetrieveRestData restDataRetriever = new RetrieveRestData();
//        Restaurant[] allRestaurants = restDataRetriever.retrieveData(restaurantsEndpoint, Restaurant[].class);

        // Find the corresponding restaurant for the first order
        Restaurant correspondingRestaurant = OrdersToDeliver.findCorrespondingRestaurant(firstOrder, allRestaurants);

        if (correspondingRestaurant != null) {
            System.out.println("Corresponding restaurant found for the first order: " + correspondingRestaurant.name());
        } else {
            System.out.println("No corresponding restaurant found for the first order.");
        }
}}
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

