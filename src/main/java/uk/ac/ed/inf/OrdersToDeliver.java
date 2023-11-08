package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class OrdersToDeliver {

    private ArrayList<Order> ordersForDate;
    private Queue<Order> ordersToDeliver;

    //private PriorityQueue<Order> validOrdersToDeliver;


    public OrdersToDeliver(String dateString) {
        this.ordersForDate = new ArrayList<>();
        this.ordersToDeliver = new LinkedList<>();

        try {
            String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date date = sdf.parse(dateString);

            if (date != null) {
                String formattedDate = sdf.format(date);

                String ordersEndpoint = "/orders/" + formattedDate;
                String restaurantsEndpoint = "/restaurants";

                RetrieveRestData restDataRetriever = new RetrieveRestData();
                Order[] orders = restDataRetriever.retrieveData(ordersEndpoint, Order[].class);
                Restaurant[] definedRestaurants = restDataRetriever.retrieveData(restaurantsEndpoint, Restaurant[].class);

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
                        ordersToDeliver.add(validatedOrder);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    public ArrayList<Order> getOrdersForDate() {
        return ordersForDate;
    }

    public Queue<Order> getOrdersToDeliver() {
        return ordersToDeliver;
    }

//    public PriorityQueue<Order> getValidOrdersToDeliver() {
//        return validOrdersToDeliver;
//    }


}

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