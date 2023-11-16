package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class OrdersToDeliver {

    private ArrayList<Order> ordersForDate;
    private Queue<Order> validOrdersToDeliver;

    private List<Node3> movesMade = new ArrayList<>();


    /**
     * Find corresponding restaurant for order.
     * @param order The order for which to find the corresponding restaurant.
     * @param restaurants List of all restaurants.
     * @return The corresponding restaurant for the order.
     */
    public static Restaurant findCorrespondingRestaurant(Order order, Restaurant[] restaurants) {
        Pizza[] orderPizzas = order.getPizzasInOrder();

        for (Restaurant restaurant : restaurants) {
            ArrayList<String> restaurantPizzaNames = Arrays.stream(restaurant.menu())
                    .map(Pizza::name)
                    .collect(Collectors.toCollection(ArrayList::new));

            boolean allPizzasFound = Arrays.stream(orderPizzas)
                    .allMatch(pizza -> restaurantPizzaNames.contains(pizza.name()));

            if (allPizzasFound) {
                return restaurant;
            }
        }

        // If no corresponding restaurant is found, return null or handle it as needed
        return null;
    }

    public OrdersToDeliver(String dateString) {
        this.ordersForDate = new ArrayList<>();
        this.validOrdersToDeliver = new LinkedList<>();

        // Create an instance of RetrieveRestData
        RetrieveRestData restDataRetriever = new RetrieveRestData();

        // Retrieve restaurant data from the REST API
        Restaurant[] definedRestaurants = restDataRetriever.retrieveRestaurantData();
        Order[] orders = restDataRetriever.retrieveOrderDataByDate(dateString);


        if (orders != null) {
            for (Order order : orders) {
                ordersForDate.add(order);
            }
        }

        OrderValidator orderValidator = new OrderValidator();
        for (Order order : ordersForDate) {
            Order validatedOrder = orderValidator.validateOrder(order, definedRestaurants);

            if (validatedOrder.getOrderValidationCode() == OrderValidationCode.NO_ERROR &&
                    validatedOrder.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED) {
                validOrdersToDeliver.add(validatedOrder);
            }
        }
    }

    public ArrayList<Order> getOrdersForDate() {
        return ordersForDate;
    }

    public Queue<Order> getValidOrdersToDeliver() {
        return validOrdersToDeliver;
    }

}












//    public void searchCorrespondingRestaurant(Restaurant[] definedRestaurants) {
//        for (Restaurant restaurant : definedRestaurants) {
//            ArrayList<String> restaurantItems = Arrays.stream(restaurant.menu()).map()
//
//        }
//    }


//    public PriorityQueue<Order> getValidOrdersToDeliver() {
//        return validOrdersToDeliver;
//    }

//    public OrdersToDeliver(String dateString) {
//        this.ordersForDate = new ArrayList<>();
//        this.ordersToDeliver = new ArrayList<>();
//
//        try {
////            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
////            String formattedDate = orderDay.format(formatter);
//            String format = "yyyy-MM-dd";
//            SimpleDateFormat sdf = new SimpleDateFormat(format);
//
//            Date date = sdf.parse(dateString);
//
//            if (date != null) {
//                String formattedDate = sdf.format(date);
//            //SimpleDateFormat sdf = new SimpleDateFormat(format);
//
//            //Date date = sdf.parse(dateString);
//
//
//            //String formattedDate = sdf.format(date);
//            String ordersEndpoint = "/orders/" + formattedDate;
//            String restaurantsEndpoint = "/restaurants";
//
//            RetrieveRestData restDataRetriever = new RetrieveRestData();
//            Order[] orders = restDataRetriever.retrieveData(ordersEndpoint, Order[].class);
//            Restaurant[] definedRestaurants = restDataRetriever.retrieveData(restaurantsEndpoint, Restaurant[].class);
//
//            if (orders != null) {
//                for (Order order : orders) {
//                    ordersForDate.add(order);
//                }
//            }
//
//            OrderValidator orderValidator = new OrderValidator();
//            for (Order order : ordersForDate) {
//                Order validatedOrder = orderValidator.validateOrder(order, definedRestaurants);
//                ordersToDeliver.add(validatedOrder);
//
////                    if (validatedOrder.getOrderValidationCode() == OrderValidationCode.NO_ERROR &&
////                    validatedOrder.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED) {
////                        ordersToDeliver.add(validatedOrder);
////                    }
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }