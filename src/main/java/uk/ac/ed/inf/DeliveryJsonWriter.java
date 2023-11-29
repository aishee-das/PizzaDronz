package uk.ac.ed.inf;

import org.json.JSONArray;
import org.json.JSONObject;
import uk.ac.ed.inf.ilp.data.Order;

public class DeliveryJsonWriter {
    /**
     * Helper function to write a JSON string of deliveries
     * @param orders the orders to write
     * @return a JSON string of deliveries
     */
    public static String writeDeliveryJson(Order[] orders) {
        // Creates a JSON array of deliveries
        JSONArray deliveries = new JSONArray();
        for (Order order : orders) {
            JSONObject delivery = new JSONObject();
            delivery.put("orderNo", order.getOrderNo());
            delivery.put("orderStatus", order.getOrderStatus());
            delivery.put("orderValidationCode", order.getOrderValidationCode());
            delivery.put("costInPence", order.getPriceTotalInPence());
            deliveries.put(delivery);
        }
        return deliveries.toString();
    }
}
