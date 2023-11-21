package uk.ac.ed.inf;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class OutputFileWriter {
    public static void outputGeoJson(List<DroneMove> allMoves, String date) {
        JsonArray coordinates = new JsonArray();

        for (DroneMove move : allMoves) {
            JsonArray coordinate = new JsonArray();
            coordinate.add(move.getNextNodeFromLng());
            coordinate.add(move.getNextNodeFromLat());
            coordinates.add(coordinate);
        }

        JsonObject lineStringGeometry = new JsonObject();
        lineStringGeometry.addProperty("type", "LineString");
        lineStringGeometry.add("coordinates", coordinates);

        JsonObject lineStringFeature = new JsonObject();
        lineStringFeature.addProperty("type", "Feature");
        lineStringFeature.add("geometry", lineStringGeometry);

        JsonObject featureCollection = new JsonObject();
        featureCollection.addProperty("type", "FeatureCollection");

        JsonArray features = new JsonArray();
        features.add(lineStringFeature);

        featureCollection.add("features", features);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String geoJsonString = gson.toJson(featureCollection);

        // Specify the directory path
        String directoryPath = "resultfiles/";

        // Write string to file in the specified directory
        String fileName = directoryPath + "path-" + date + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(geoJsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("GeoJSON written to: " + fileName);
    }

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
