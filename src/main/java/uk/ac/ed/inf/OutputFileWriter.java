package uk.ac.ed.inf;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import uk.ac.ed.inf.ilp.data.Order;

public class OutputFileWriter {

    private static JSONObject deliveriesHelper(Order order) {
        JSONObject pathObject = new JSONObject();

        pathObject.put("orderNo", order.getOrderNo());
        pathObject.put("orderStatus", order.getOrderStatus().toString()); // Assuming OrderStatus is an enum
        pathObject.put("orderValidationCode", order.getOrderValidationCode().toString()); // Assuming OrderValidationCode is an enum
        pathObject.put("costInPence", order.getPriceTotalInPence());

        return pathObject;
    }

    public static void deliveriesJsonWriter(List<Order> ordersList, String date) {
        try {
            // Create the resultfiles directory if it doesn't exist
            String directoryPath = "resultfiles/";

            JSONArray deliveries = new JSONArray();

            for (Order order : ordersList) {
                JSONObject pathObject = deliveriesHelper(order);
                deliveries.add(pathObject);
            }
            String fileName = directoryPath + "delivery-" + date + ".json";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(deliveries.toJSONString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //    }
    //flightpath file - records every move of the drone
    public static void writeJsonToFile(List<DroneMove> moves, String date) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convert DroneMove objects to JSON
            String jsonString = objectMapper.writeValueAsString(moves);

            // Specify the directory path
            String directoryPath = "resultfiles/";

            // Write string to file in the specified directory
            String fileName = directoryPath + "flightpath-" + date + ".json";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//MAKE THIS.GEOJSON NOT JSON!!
//    public static void outputGeoJson(List<DroneMove> allMoves, String date) {
//        JsonArray coordinates = new JsonArray();
//
//        for (DroneMove move : allMoves) {
//            JsonArray coordinate = new JsonArray();
//            coordinate.add(move.getNextNodeFromLng());
//            coordinate.add(move.getNextNodeFromLat());
//            coordinates.add(coordinate);
//        }
//
//        JsonObject lineStringGeometry = new JsonObject();
//        lineStringGeometry.addProperty("type", "LineString");
//        lineStringGeometry.add("coordinates", coordinates);
//
//        JsonObject lineStringFeature = new JsonObject();
//        lineStringFeature.addProperty("type", "Feature");
//        lineStringFeature.add("geometry", lineStringGeometry);
//        lineStringFeature.add("properties", JsonNull.INSTANCE);
//
//        JsonObject featureCollection = new JsonObject();
//        featureCollection.addProperty("type", "FeatureCollection");
//
//        JsonArray features = new JsonArray();
//        features.add(lineStringFeature);
//
//        featureCollection.add("features", features);
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String geoJsonString = gson.toJson(featureCollection);
//
//        // Specify the directory path
//        String directoryPath = "resultfiles/";
//
//        // Write string to file in the specified directory
//        String fileName = directoryPath + "drone-" + date + ".geojson";
//        try (FileWriter writer = new FileWriter(fileName)) {
//            writer.write(geoJsonString);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void outputGeoJson(List<DroneMove> allMoves, String date) {
//        JsonArray features = new JsonArray();
//
//        for (DroneMove move : allMoves) {
//            JsonObject geometry = new JsonObject();
//            geometry.addProperty("type", "Point");
//
//            JsonArray coordinates = new JsonArray();
//            coordinates.add(move.getNextNodeFromLng());
//            coordinates.add(move.getNextNodeFromLat());
//            geometry.add("coordinates", coordinates);
//
//            JsonObject properties = new JsonObject();
//            // Add any other properties you want to include
//
//            JsonObject feature = new JsonObject();
//            feature.addProperty("type", "Feature");
//            feature.add("geometry", geometry);
//            feature.add("properties", properties);
//
//            features.add(feature);
//        }
//
//        JsonObject pathTaken = new JsonObject();
//        pathTaken.addProperty("type", "LineString");
//
//        JsonArray pathCoordinates = new JsonArray();
//        for (DroneMove move : allMoves) {
//            JsonArray coordinate = new JsonArray();
//            coordinate.add(move.getNextNodeFromLng());
//            coordinate.add(move.getNextNodeFromLat());
//            pathCoordinates.add(coordinate);
//        }
//        pathTaken.add("coordinates", pathCoordinates);
//
//        JsonObject feature = new JsonObject();
//        feature.addProperty("type", "Feature");
//        feature.add("geometry", pathTaken);
//
//        features.add(feature);
//
//        JsonObject totalFeature = new JsonObject();
//        totalFeature.addProperty("type", "FeatureCollection");
//        totalFeature.add("features", features);
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String geoJsonString = gson.toJson(totalFeature);
//
//        System.out.println(geoJsonString);
//    }

//    make sure you have a properties element that is also an output otherwise it wont parse
//    And just put the line coordinates, so "type": "LineString" (but ask someone for confirmation)
//    public static void outputGeoJson(List<Node3> allMoves, String date) {
//        JsonArray features = new JsonArray();
//        for (Node3 move : allMoves) {
//            JsonObject geometry = new JsonObject();
//            geometry.addProperty("type", "Point");
//            JsonArray coordinates = new JsonArray();
//            coordinates.add(move.location.lng());
//            coordinates.add(move.location.lat());
//            geometry.add("coordinates", coordinates);
//
//            JsonObject properties = new JsonObject();
//            // Add any other properties you want to include
//
//            JsonObject feature = new JsonObject();
//            feature.addProperty("type", "Feature");
//            feature.add("geometry", geometry);
//            feature.add("properties", properties);
//
//            features.add(feature);
//        }
//
//        JsonObject pathTaken = new JsonObject();
//        pathTaken.addProperty("type", "LineString");
//        JsonArray pathCoordinates = new JsonArray();
//        for (Node3 move : allMoves) {
//            JsonArray coordinate = new JsonArray();
//            coordinate.add(move.location.lng());
//            coordinate.add(move.location.lat());
//            pathCoordinates.add(coordinate);
//        }
//        pathTaken.add("coordinates", pathCoordinates);
//
//        JsonObject feature = new JsonObject();
//        feature.addProperty("type", "Feature");
//        feature.add("geometry", pathTaken);
//
//        features.add(feature);
//
//        JsonObject totalFeature = new JsonObject();
//        totalFeature.addProperty("type", "FeatureCollection");
//        totalFeature.add("features", features);
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String geoJsonString = gson.toJson(totalFeature);
//
//        System.out.println(geoJsonString);
//    }
}
