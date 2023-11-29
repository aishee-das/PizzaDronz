package uk.ac.ed.inf;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;

public class GeoJsonWriter {
    /**
     * Helper function to write a GeoJSON string of the moves made
     * @param moves the moves to write
     * @return a GeoJSON string of the moves made
     */
    public static String writeGeoJson(List<DroneMove> moves) {

        // Create a JSON Object for FeatureCollection
        JsonObject featureCollection = new JsonObject();
        featureCollection.addProperty("type", "FeatureCollection");

        // Create a JSON array of features
        JsonArray features = new JsonArray();

        // Create a feature object
        JsonObject feature = new JsonObject();
        feature.addProperty("type", "Feature");
//        feature.addProperty("properties", "NULL");

        // Create a properties object and add it to the feature object
        JsonObject properties = new JsonObject();
        feature.add("properties", properties);

        // Create a geometry object
        JsonObject geometry = new JsonObject();
        geometry.addProperty("type", "LineString");

        // Create a coordinates array
        JsonArray coords = new JsonArray();

        // Creates an array of coordinates for each move
        for (DroneMove move : moves) {
            JsonArray lngLat = new JsonArray();
            lngLat.add(move.getStart().lng());
            lngLat.add(move.getStart().lat());
            coords.add(lngLat);
        }

        // Add the coordinates array to the geometry object under "type": "LineString"
        geometry.add("coordinates", coords);

        // Add the geometry object to the feature object under "type": "Feature"
        feature.add("geometry", geometry);

        // Add the feature object to the features array
        features.add(feature);

        // Add the feature object under "type": "FeatureCollection"
        featureCollection.add("features", features);

        return featureCollection.toString();
    }
}
