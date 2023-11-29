package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class OrderValidator implements OrderValidation {
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {

        //Checking Credit Card Number validity (16 digits and numeric)
        CreditCardInformation creditCard = orderToValidate.getCreditCardInformation();
        String creditCardNumber = creditCard.getCreditCardNumber();

        if (creditCardNumber.length() != 16 || !creditCardNumber.matches("\\d+")) {
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            return orderToValidate;
        }

        //Checking CVV (3 digits and numeric)
        String cvv = creditCard.getCvv();

        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
            return orderToValidate;
        }

        //Checking maximum pizza count
        Pizza[] pizzas = orderToValidate.getPizzasInOrder();
        if (pizzas.length > SystemConstants.MAX_PIZZAS_PER_ORDER) {
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            return orderToValidate;
        }

        //Checking expiry date of credit card
        String creditCardExpiry = creditCard.getCreditCardExpiry();
        //if (isValidExpiryDateFormat(creditCardExpiry)) {
        if (!isValidExpiryDate(creditCardExpiry)) {
                orderToValidate.setOrderStatus(OrderStatus.INVALID);
                orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
                return orderToValidate;
        }


        //Checking if total is incorrect. Total = sum of pizza costs + delivery charge
        int totalPizzaCost = totalPizzaCost(orderToValidate);
        int totalCost = totalPizzaCost + SystemConstants.ORDER_CHARGE_IN_PENCE;
        if (totalCost != orderToValidate.getPriceTotalInPence()) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        //Checking if pizza is not defined
        Set<String> definedPizzas = new HashSet<>();

        for (Restaurant restaurant : definedRestaurants) {
            for (Pizza definedPizza : restaurant.menu()) {
                definedPizzas.add(definedPizza.name());
            }
        }

        for (Pizza pizza : orderToValidate.getPizzasInOrder()) {
            if (!definedPizzas.contains(pizza.name())) {
                orderToValidate.setOrderStatus(OrderStatus.INVALID);
                orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
                return orderToValidate;
            }
        }

        //Checking if pizza(s) are from multiple restaurants
        Map<String, Restaurant> pizzaRestaurantMap = new HashMap<>();

        for (Pizza pizza : orderToValidate.getPizzasInOrder()) {
            boolean pizzaFound = false;

            for (Restaurant restaurant : definedRestaurants) {
                for (Pizza definedPizza : restaurant.menu()) {
                    if (pizza.name().equals(definedPizza.name())) {
                        pizzaRestaurantMap.put(pizza.name(), restaurant);
                        pizzaFound = true;
                        break;
                    }
                }
                if (pizzaFound) {
                    break;
                }
            }
        }

        if (pizzaRestaurantMap.values().stream().distinct().count() > 1) {
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
            return orderToValidate;
        }

        //Checking whether the restaurant is closed on the order date
//        LocalDate orderDay = orderToValidate.getOrderDate();
//        for (Restaurant restaurant : definedRestaurants) {
//            DayOfWeek[] openingDays = restaurant.openingDays();
//            var testVariable = orderDay.getDayOfWeek();
//            boolean isOpenOnOrderDay = Arrays.asList(openingDays).contains(testVariable);
//
//            if (!isOpenOnOrderDay) {
//                orderToValidate.setOrderStatus(OrderStatus.INVALID);
//                orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
//                return orderToValidate;
//            }
//        }

        LocalDate orderDay = orderToValidate.getOrderDate();

        for (Pizza pizza : orderToValidate.getPizzasInOrder()) {
            Restaurant pizzaRestaurant = definedRestaurants[0]; // Initialize with the first restaurant

            for (Restaurant restaurant : definedRestaurants) {
                for (Pizza definedPizza : restaurant.menu()) {
                    if (pizza.name().equals(definedPizza.name())) {
                        pizzaRestaurant = restaurant;
                        break;
                    }
                }
            }

            DayOfWeek[] openingDays = pizzaRestaurant.openingDays();
            DayOfWeek orderDayOfWeek = orderDay.getDayOfWeek();

            if (!Arrays.asList(openingDays).contains(orderDayOfWeek)) {
                orderToValidate.setOrderStatus(OrderStatus.INVALID);
                orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
                return orderToValidate;
            }
        }

        //If no validation errors were encountered, set the validation code to NO_ERROR
        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        //If no validation errors were encountered, set the status code to VALID_BUT_NOT_DELIVERED
        orderToValidate.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
        return orderToValidate;
    }

    //Helper method to validate expiry date
//    private boolean isValidExpiryDate(String creditCardExpiry) {
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
//        //Parses the 'creditCardExpiry' into a 'YearMonth' object which ensures that 'creditCardExpiry' string adheres to "MM/yy" format
//        YearMonth parsedCreditCardExpiry = YearMonth.parse(creditCardExpiry, formatter);
//        LocalDate currentDate = LocalDate.now();
//
//        return !parsedCreditCardExpiry.isBefore(YearMonth.from(currentDate));
//    }
    private boolean isValidExpiryDate(String creditCardExpiry) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        // Check if the creditCardExpiry is in the correct "MM/yy" format
        if (creditCardExpiry.matches("\\d{2}/\\d{2}")) {
            String[] parts = creditCardExpiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            // Check if the month is within a valid range (1 to 12)
            if (month >= 1 && month <= 12) {
                YearMonth parsedCreditCardExpiry = YearMonth.of(2000 + year, month);
                LocalDate currentDate = LocalDate.now();
                return !parsedCreditCardExpiry.isBefore(YearMonth.from(currentDate));
            }
        }

        // Handle the case where the date format is incorrect or the month is invalid
        return false;
    }


    // Helper method to check the format of the credit card expiry
//    private boolean isValidExpiryDateFormat(String creditCardExpiry) {
//        return creditCardExpiry.matches("\\d{2}/\\d{2}");
//    }

    private int totalPizzaCost(Order order) {
        int totalPizzaCost = 0;

        for (Pizza pizza : order.getPizzasInOrder()) {
            totalPizzaCost += pizza.priceInPence();
        }
        return totalPizzaCost;
    }

//    /**
//     * Helper method to find intersection between two lists
//     * @param orderList the list of pizzas in the order
//     * @param menu the list of pizzas in the restaurant
//     * @return the number of pizzas in the order that are in the restaurant
//     */
//    public static int intersection(java.util.List<Pizza> orderList, java.util.List<Pizza> menu) {
//        int counter = 0;
//        for (Pizza pizza : orderList) {
//            if (menu.contains(pizza)) {
//                counter++;
//            }
//        }
//        return counter;
//    }


}


