package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Pizza;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static uk.ac.ed.inf.ilp.constant.OrderStatus.DELIVERED;

public class DroneRoutePlanner {
    private final NamedRegion[] noFlyZones;
    private final NamedRegion centralArea;
    private final Restaurant[] restaurants;
    private final List<Order> orders;

    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    /**
     * Constructor for RoutePlanner class
     * @param noFlyZones the no-fly zones
     * @param centralArea the central area
     * @param restaurants the restaurants
     * @param orders the orders
     */
    public DroneRoutePlanner(NamedRegion[] noFlyZones, NamedRegion centralArea, Restaurant[] restaurants, List<Order> orders) {
        this.noFlyZones = noFlyZones;
        this.centralArea = centralArea;
        this.restaurants = restaurants;
        this.orders = orders;
    }

//    /**
//     * Helper function to find the location of the restaurant for an order
//     * @param order the order
//     * @return the location of the restaurant
//     */
//    private LngLat getRestaurantLoc(Order order) {
//        // Checks each restaurant to find which restaurant contains every pizza in the order
//        for (Restaurant restaurant : restaurants) {
//            // Converts the array of pizzas in the order and the restaurant menus to lists
//            List<Pizza> pizzasList = Arrays.stream(order.getPizzasInOrder()).toList();
//            List<Pizza> menuList = Arrays.stream(restaurant.menu()).toList();
//
//            // Returns the restaurant where every pizza in the order is in the restaurant's menu
//            if (OrderValidator.intersection(pizzasList, menuList) == order.getPizzasInOrder().length) {
//                return restaurant.location();
//            }
//        }
//        throw new IllegalArgumentException("Restaurant not found for order");
//    }
    /**
     * Finds and returns the location of the restaurant corresponding to the given order,
     * based on matching pizzas in the order with those in the registered restaurants' menus.
     *
     * @param order The order for which the corresponding restaurant location is sought.
     * @return The location of the corresponding restaurant if found; otherwise, null.
     */
    public LngLat findCorrespondingRestaurantLoc(Order order) {
        Pizza[] orderPizzas = order.getPizzasInOrder();

        for (Restaurant restaurant : restaurants) {
            ArrayList<String> restaurantPizzaNames = Arrays.stream(restaurant.menu())
                    .map(Pizza::name)
                    .collect(Collectors.toCollection(ArrayList::new));

            boolean allPizzasFound = Arrays.stream(orderPizzas)
                    .allMatch(pizza -> restaurantPizzaNames.contains(pizza.name()));

            if (allPizzasFound) {
                return restaurant.location();
            }
        }

        // If no corresponding restaurant is found, return null
        return null;
    }

    /**
     * Finds delivery paths for a list of orders, considering specified no-fly zones and a central area.
     * This method utilizes pathCalculator to determine the optimal paths for picking up orders
     * from corresponding restaurant locations and delivering them to a predefined drop-off location.
     * After calculating the paths, it updates the order status to "DELIVERED" for each processed order.
     *
     * @return A list of Move objects representing the sequence of movements for all the calculated paths.
     */
    public List<DroneMove> findPaths() {
        DronePathCalculator pathCalculator = new DronePathCalculator(noFlyZones, centralArea);
        List<DroneMove> allPaths = new ArrayList<>();
        for (Order order : orders) {
            LngLat restaurantLoc = findCorrespondingRestaurantLoc(order);
            List<DroneMove> path = pathCalculator.calculateTotalPath(restaurantLoc, APPLETON_TOWER, order.getOrderNo());
            allPaths.addAll(path);
            order.setOrderStatus(DELIVERED);
        }
        return allPaths;
    }
}
