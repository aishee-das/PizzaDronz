//package uk.ac.ed.inf;
//
//import uk.ac.ed.inf.ilp.constant.SystemConstants;
//import uk.ac.ed.inf.ilp.data.LngLat;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class MyLngLat {
//    private final LngLat lngLat;
//    private double f = Double.MAX_VALUE;
//    private double g = Double.MAX_VALUE;
//    private ArrayList<LngLat> neighbours = new ArrayList<>();
//    private MyLngLat parent = null;
//
//    public MyLngLat(double lng, double lat) {
//        this.lngLat = new LngLat(lng, lat);
//    }
//
//    public double getLng() {
//        return lngLat.lng();
//    }
//
//    public double getLat() {
//        return lngLat.lat();
//    }
//
//    public double getF() {
//        return f;
//    }
//
//    public double getG() {
//        return g;
//    }
//
//    public MyLngLat getParent() {
//        return parent;
//    }
//
//    public void setParent(MyLngLat parent) {
//        this.parent = parent;
//    }
//
//    public void setF(double f) {
//        this.f = f;
//    }
//
//    public void setG(double g) {
//        this.g = g;
//    }
//
//    public int compareTo(MyLngLat lngLat) {
//        return Double.compare(this.f, lngLat.f);
//    }
//
//    // Use LngLatHandler for distance calculations
//    public double distanceTo(LngLat other) {
//        LngLatHandler lngLatHandler = new LngLatHandler();
//        return lngLatHandler.distanceTo(this.lngLat, other);
//    }
//
//    public LngLat nextPosition(LngLat start, double angle) {
//        LngLatHandler lngLatHandler = new LngLatHandler();
//        return lngLatHandler.nextPosition(start, angle);
//    }
//
//    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
//        LngLatHandler lngLatHandler = new LngLatHandler();
//        return lngLatHandler.isCloseTo(startPosition, otherPosition);
//    }
//
//    public void setNeighbours(LngLat currentPosition) {
//        Arrays.stream(Direction.values())
//                .forEach(d -> neighbours.add(nextPosition(currentPosition, d.angle)));
//    }
//
//    public ArrayList<LngLat> getNeighbours() {
//        return neighbours;
//    }
//
//    public void clearNeighbours() {
//        neighbours.clear();
//    }
//}
