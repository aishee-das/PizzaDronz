package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyRestaurant {
    private final Restaurant restaurant;
    private ArrayList<LngLat> flightPath;

    public MyRestaurant(String name, LngLat location, DayOfWeek[] openingDays, Pizza[] menu) {
        this.restaurant = new Restaurant(name, location, openingDays, menu);
        this.flightPath = new ArrayList<>();
    }

    // Additional methods and functionality

    public void setFlightPath(ArrayList<LngLat> path) {
        this.flightPath = path;
    }

    public ArrayList<LngLat> getFlightPath() {
        return this.flightPath;
    }

//    public static LngLat restaurantLocation(Restaurant[] restaurants) {
//        for (Restaurant restaurant : restaurants) {
//            restaurant.location();
//        }
//    }

//    /**
//     * Find corresponding restaurant for order.
//     * @param order The order for which to find the corresponding restaurant.
//     * @param restaurants List of all restaurants.
//     * @return The corresponding restaurant for the order.
//     */
//    public static Restaurant findCorrespondingRestaurant(Order order, Restaurant[] restaurants) {
//        Pizza[] orderPizzas = order.getPizzasInOrder();
//
//        for (Restaurant restaurant : restaurants) {
//            ArrayList<String> restaurantPizzaNames = Arrays.stream(restaurant.menu())
//                    .map(Pizza::name)
//                    .collect(Collectors.toCollection(ArrayList::new));
//
//            boolean allPizzasFound = Arrays.stream(orderPizzas)
//                    .allMatch(pizza -> restaurantPizzaNames.contains(pizza.name()));
//
//            if (allPizzasFound) {
//                return restaurant;
//            }
//        }
//
//        // If no corresponding restaurant is found, return null or handle it as needed
//        return null;
//    }
}