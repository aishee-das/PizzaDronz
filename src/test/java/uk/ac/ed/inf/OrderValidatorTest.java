package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.time.DayOfWeek;
import java.time.LocalDate;


public class OrderValidatorTest extends TestCase {

    public void testValidCreditCard() {
        OrderValidator orderValidator = new OrderValidator();
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234444444444442", "12/23", "123");
        Order order = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2000, new Pizza[] {new Pizza("All Shrooms", 1900)}, creditCardInfo);
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1900),
                new Pizza("Margarita", 900)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

    public void testInvalidCreditCard() {
        OrderValidator orderValidator = new OrderValidator();
        CreditCardInformation creditCardInfo = new CreditCardInformation("134ABC", "06/28", "823");
        Order order = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2000, new Pizza[] {new Pizza("All Shrooms", 1900)}, creditCardInfo);;
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1900),
                new Pizza("Margarita", 900)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    public void testValidCVV() {
        OrderValidator orderValidator = new OrderValidator();
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "12/23", "123");
        Order order = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2000, new Pizza[] {new Pizza("All Shrooms", 1900)}, creditCardInfo);;
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1900),
                new Pizza("Margarita", 900)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

    public void testInvalidCVV() {
        OrderValidator orderValidator = new OrderValidator();
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "12/23", "12");
        Order order = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2000, new Pizza[] {new Pizza("All Shrooms", 1900)}, creditCardInfo);;
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1900),
                new Pizza("Margarita", 900)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    public void testValidPizzaCount() {
        OrderValidator orderValidator = new OrderValidator();
        Pizza[] pizzas = new Pizza[] { new Pizza("All Shrooms", 1000), new Pizza("Margarita", 2000), new Pizza("Hawaiian", 1500), new Pizza("Super Cheese", 3000)};
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "12/23", "123");
        Order order = new Order("123", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 7600, pizzas, creditCardInfo);
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        LngLat restaurantLocation2 = new LngLat(-3.44455, 55.9422);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1400),
                new Pizza("Margarita", 900)
        };
        Pizza[] restaurantMenu2 = {
                new Pizza("Hawaiian", 1500),
                new Pizza("Super Cheese", 3000),
                new Pizza("Spicy Chicken Tikka", 1700)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu), new Restaurant("Pizza Hut", restaurantLocation2, openingDays, restaurantMenu2)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

    public void testExceededPizzaCount() {
        OrderValidator orderValidator = new OrderValidator();
        Pizza[] pizzas = new Pizza[] { new Pizza("All Shrooms", 1000), new Pizza("Margarita", 2000), new Pizza("Hawaiian", 1500), new Pizza("Super Cheese", 3000), new Pizza("Spicy Chicken Tikka", 1700)};
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "12/23", "123");
        Order order = new Order("123", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 9300, pizzas, creditCardInfo);
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        LngLat restaurantLocation2 = new LngLat(-3.44455, 55.9422);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1400),
                new Pizza("Margarita", 900)
        };
        Pizza[] restaurantMenu2 = {
                new Pizza("Hawaiian", 1500),
                new Pizza("Super Cheese", 3000),
                new Pizza("Spicy Chicken Tikka", 1700)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu), new Restaurant("Pizza Hut", restaurantLocation2, openingDays, restaurantMenu2)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED, order.getOrderValidationCode());
    }

    public void testValidExpiryDate() {
        OrderValidator orderValidator = new OrderValidator();
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "10/23", "123");
        Order order = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2000, new Pizza[] {new Pizza("All Shrooms", 1900)}, creditCardInfo);;
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1900),
                new Pizza("Margarita", 900)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

    public void testInvalidExpiryDate() {
        OrderValidator orderValidator = new OrderValidator();
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "09/23", "123");
        Order order = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2000, new Pizza[] {new Pizza("All Shrooms", 1900)}, creditCardInfo);;
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1900),
                new Pizza("Margarita", 900)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu)};
        orderValidator.validateOrder(order,definedRestaurants);
        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    public void testTotalIncorrect() {
        OrderValidator orderValidator = new OrderValidator();
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "11/23", "123");
        Order order = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2200, new Pizza[] {new Pizza("All Shrooms", 1900)}, creditCardInfo);;
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1900),
                new Pizza("Margarita", 900)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.TOTAL_INCORRECT, order.getOrderValidationCode());
    }
    public void testTotalCorrect() {
        OrderValidator orderValidator = new OrderValidator();
        Pizza[] pizzas = new Pizza[] { new Pizza("All Shrooms", 1900), new Pizza("Margarita", 900)};
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "11/23", "123");
        Order order = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2900, pizzas, creditCardInfo);;
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1900),
                new Pizza("Margarita", 900)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

    public void testPizzaNotDefined() {
        OrderValidator orderValidator = new OrderValidator();
        Pizza[] pizzas = new Pizza[] { new Pizza("All Shrooms", 1000), new Pizza("Margarita", 2000), new Pizza("Hawaiian", 1500), new Pizza("Super Cheese", 3000)};
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "11/23", "123");
        Order order = new Order("123", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 7600, pizzas, creditCardInfo);
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        LngLat restaurantLocation2 = new LngLat(-3.44455, 55.9422);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1400),
                new Pizza("Margarita", 900)
        };
        Pizza[] restaurantMenu2 = {
                new Pizza("hawaiian", 1500),
                new Pizza("Super Cheese", 3000),
                new Pizza("Spicy Chicken Tikka", 1700)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu), new Restaurant("Pizza Hut", restaurantLocation2, openingDays, restaurantMenu2)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, order.getOrderValidationCode());
    }

    public void testPizzaDefined() {
        OrderValidator orderValidator = new OrderValidator();
        Pizza[] pizzas = new Pizza[] { new Pizza("All Shrooms", 1000), new Pizza("Margarita", 2000), new Pizza("Hawaiian", 1500), new Pizza("Super Cheese", 3000)};
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567890123456", "11/23", "123");
        Order order = new Order("123", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 7600, pizzas, creditCardInfo);
        LngLat restaurantLocation = new LngLat(-3.19, 55.94);
        LngLat restaurantLocation2 = new LngLat(-3.44455, 55.9422);
        Pizza[] restaurantMenu = {
                new Pizza("All Shrooms", 1400),
                new Pizza("Margarita", 900)
        };
        Pizza[] restaurantMenu2 = {
                new Pizza("Hawaiian", 1500),
                new Pizza("Super Cheese", 3000),
                new Pizza("Spicy Chicken Tikka", 1700)
        };
        DayOfWeek[] openingDays = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };
        Restaurant[] definedRestaurants = new Restaurant[] { new Restaurant("Civerinos Slice", restaurantLocation, openingDays, restaurantMenu), new Restaurant("Pizza Hut", restaurantLocation2, openingDays, restaurantMenu2)};
        orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

}
