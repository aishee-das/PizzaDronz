package uk.ac.ed.inf;

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
    public static void outputGeoJson(List<Node3> allMoves, String date) {
        JsonArray features = new JsonArray();
        for (Node3 move : allMoves) {
            JsonObject geometry = new JsonObject();
            geometry.addProperty("type", "Point");
            JsonArray coordinates = new JsonArray();
            coordinates.add(move.location.lng());
            coordinates.add(move.location.lat());
            geometry.add("coordinates", coordinates);

            JsonObject properties = new JsonObject();
            // Add any other properties you want to include

            JsonObject feature = new JsonObject();
            feature.addProperty("type", "Feature");
            feature.add("geometry", geometry);
            feature.add("properties", properties);

            features.add(feature);
        }

        JsonObject pathTaken = new JsonObject();
        pathTaken.addProperty("type", "LineString");
        JsonArray pathCoordinates = new JsonArray();
        for (Node3 move : allMoves) {
            JsonArray coordinate = new JsonArray();
            coordinate.add(move.location.lng());
            coordinate.add(move.location.lat());
            pathCoordinates.add(coordinate);
        }
        pathTaken.add("coordinates", pathCoordinates);

        JsonObject feature = new JsonObject();
        feature.addProperty("type", "Feature");
        feature.add("geometry", pathTaken);

        features.add(feature);

        JsonObject totalFeature = new JsonObject();
        totalFeature.addProperty("type", "FeatureCollection");
        totalFeature.add("features", features);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String geoJsonString = gson.toJson(totalFeature);

        System.out.println(geoJsonString);
    }
}
