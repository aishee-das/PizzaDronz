package uk.ac.ed.inf;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class FlightpathJsonWriter {
    /**
     * Helper function to write a JSON string of flight paths
     * @param moves the moves to write
     * @return a JSON string of flight paths
     */
    public static String writeFlightpathJson(List<DroneMove> moves) {
        // Creates a JSON array of deliveries
        JSONArray flightPath = new JSONArray();
        // Creates a JSON object path for each move and adds it to FlightPath array
        for (DroneMove move: moves) {
            JSONObject path = new JSONObject();
            // The contents of this field should be the 8-character string “no-order” when the drone is making the flight back to
            // the top of the Appleton Tower when the day’s orders have been delivered.
            path.put("orderNo", move.getOrderNo());
            path.put("fromLongitude", move.getStart().lng());
            path.put("fromLatitude", move.getStart().lat());
            path.put("angle", move.getAngle());
            path.put("toLongitude", move.getEnd().lng());
            path.put("toLatitude", move.getEnd().lat());
            flightPath.put(path);
        }
        return flightPath.toString();
    }
}

