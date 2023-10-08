package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class OrderValidator implements OrderValidation {

    private static final int FIXED_DELIVERY_CHARGE_PENCE = 100; //£1 converted to pence
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {

        //Checking Credit Card Number validity (16 digits and numeric)
        CreditCardInformation creditCard = orderToValidate.getCreditCardInformation();
        String creditCardNumber = creditCard.getCreditCardNumber();

        if (creditCardNumber.length() != 16 || !creditCardNumber.matches("\\d+")) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            return orderToValidate;
        }

        //Checking CVV (3 digits and numeric)
        String cvv = creditCard.getCvv();

        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
            return orderToValidate;
        }

        //Checking maximum pizza count
        Pizza[] pizzas = orderToValidate.getPizzasInOrder();
        if (pizzas.length > 4) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            return orderToValidate;
        }

        //Checking expiry date of credit card
        String creditCardExpiry = creditCard.getCreditCardExpiry();

        if (!isValidExpiryDate(creditCardExpiry)) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            return orderToValidate;
        }

        //Checking if total is incorrect. Total = sum of pizza costs + delivery charge
        int totalPizzaCost = totalPizzaCost(orderToValidate);
        int totalCost = totalPizzaCost + FIXED_DELIVERY_CHARGE_PENCE;

        if (totalCost != orderToValidate.getPriceTotalInPence()) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            return orderToValidate;
        }

        //PIZZA_NOT_DEFINED
        Set<String> definedPizzaNames = new HashSet<>();

        for (Restaurant restaurant : definedRestaurants) {
            for (Pizza definedPizza : restaurant.menu()) {
                definedPizzaNames.add(definedPizza.name());
            }
        }

        for (Pizza pizza : orderToValidate.getPizzasInOrder()) {
            if (!definedPizzaNames.contains(pizza.name())) {
                orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
                return orderToValidate;
            }
        }





//        for (Pizza pizza : orderToValidate.getPizzasInOrder()) {
//            boolean pizzaDefined = false;
//
//            for (Restaurant restaurant : definedRestaurants) {
//                for (Pizza definedPizza : restaurant.menu()) {
//                    if (pizza.name().equals(definedPizza.name())) {
//                        pizzaDefined = true;
//                        break;
//                    }
//                }
//                if (pizzaDefined) {
//                    break;
//                }
//            }
//
//            if (!pizzaDefined) {
//                orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
//                return orderToValidate;
//            }
//        }

        //PIZZA_FROM_MULTIPLE_RESTAURANT
        //Can you do this any other way like hashmap
        Set<String> restaurantIDs = new HashSet<>();
        for (Pizza pizza : orderToValidate.getPizzasInOrder()) {
            boolean pizzaFound = false;

            for (Restaurant restaurant : definedRestaurants) {
                for (Pizza definedPizza : restaurant.menu()) {
                    if (pizza.name().equals(definedPizza.name())) {
                        String restaurantID = generateRestaurantID(restaurant);
                        restaurantIDs.add(restaurantID);
                        pizzaFound = true;
                        break;
                    }
                }
                if (pizzaFound) {
                    break;
                }
            }
            if (restaurantIDs.size() > 1) {
                orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
                return orderToValidate;

            }
        }

        //RESTAURANT_CLOSED
        //Checking whether the restaurant is closed on the order date
        LocalDate orderDay = orderToValidate.getOrderDate();
        for (Restaurant restaurant :definedRestaurants) {
            DayOfWeek[] openingDays = restaurant.openingDays();
            boolean isOpenOnOrderDay = Arrays.asList(openingDays).contains(orderDay.getDayOfWeek());

            if (!isOpenOnOrderDay) {
                orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
                return orderToValidate;
            }
        }


        //INVALID STATUS CODE


        //If no validation errors were encountered, set the validation code to NO_ERROR
        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        return orderToValidate;
        //ALSO CHANGE STATUS CODE TO VALID_BUT_NOT_DELIVERED



//        validateCreditCard(orderToValidate);
//        validateCVV(orderToValidate);
//        validatePizzaCount(orderToValidate);
//        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
//        return orderToValidate;
    }

    //Helper method to validate expiry date
    private boolean isValidExpiryDate(String creditCardExpiry) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        //Parses the 'creditCardExpiry' into a 'YearMonth' object which ensures that 'creditCardExpiry' string adheres to "MM/yy" format
        YearMonth parsedCreditCardExpiry = YearMonth.parse(creditCardExpiry, formatter);
        LocalDate currentDate = LocalDate.now();

        return !parsedCreditCardExpiry.isBefore(YearMonth.from(currentDate));
    }

    private int totalPizzaCost(Order order) {
        int totalPizzaCost = 0;

        for (Pizza pizza : order.getPizzasInOrder()) {
            totalPizzaCost += pizza.priceInPence();
        }
        return totalPizzaCost;
    }

    //
    private String generateRestaurantID(Restaurant restaurant) {
        return restaurant.name() + restaurant.location().toString();
    }

    //Helper methods to validate pizza_not_defined
//    private boolean arePizzasDefined(Pizza[] pizzas, Restaurant[] restaurants) {
//        for (Pizza pizza : pizzas)  {
//            boolean pizzaDefined = false;
//
//            for (Restaurant restaurant : restaurants) {
//                if (isPizzaOnMenu(pizza, restaurant)) {
//                    pizzaDefined = true;
//                    break;
//                }
//                if (!pizzaDefined) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    private boolean isPizzaOnMenu(Pizza pizza, Restaurant restaurant) {
//        Pizza[] menu = restaurant.menu();
//
//        for (Pizza pizzaInMenu : menu) {
//            //Checking if pizza names match
//            if (pizzaInMenu.name().equals(pizza.name())) {
//                return true;
//            }
//        }
//        return false;
//    }












//    private boolean isNumeric(String str) {
//        for (char c : str.toCharArray()) {
//            if (!Character.isDigit(c)) {
//                return false;
//            }
//        }
//        return true;
//    }
    //Checking Credit Card Number validity i.e, 16 digit long and numeric: -------------
//    public void validateCreditCard(Order order) {
//        CreditCardInformation creditCard = order.getCreditCardInformation();
//        String creditCardNumber = creditCard.getCreditCardNumber();
//        if (creditCardNumber.length() != 16 || !creditCardNumber.matches("\\d+")) {
//            order.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
//        }
//    }
    //Maybe add Luhn's algorithm here as well?
    //----------------------------------------------------------------------------------

    //Checking CVV: --------------------------------------------------------------------
//    public void validateCVV(Order order) {
//        CreditCardInformation creditCard = order.getCreditCardInformation();
//        String cvv = creditCard.getCvv();
//        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
//            order.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
//        }
//    }
    //----------------------------------------------------------------------------------

    //Checking maximum pizza count: ----------------------------------------------------
    //throw an
//    public void validatePizzaCount(Order order) {
//        Pizza[] pizzas = order.getPizzasInOrder();
//        if (pizzas.length > MAX_PIZZA_COUNT) {
//            order.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
//        }
//    }
    //----------------------------------------------------------------------------------






//        String cvv = orderToValidate.getCreditCardInformation().getCvv();
//        if (cvv.length() > 3) {
//            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
//        }
//        return orderToValidate;
//    }

}


//    public LocalDateTime dateTimeNow() {
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        return LocalDateTime
//    }