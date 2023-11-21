//package uk.ac.ed.inf;
//
//
//import uk.ac.ed.inf.ilp.data.LngLat;
//import uk.ac.ed.inf.ilp.data.Pizza;
//import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;
//
//import java.time.DayOfWeek;
//import java.util.*;
//
///**
// *This class is used to contain vital features that completely define a move that the drone makes.
// */
//public class Node2 {
//    LngLat lat;
//    LngLat lng; //node position
//
//    double f, g, h; // A* algorithm value parameters
//
//    Node2 parent;  //parent record: come from
//
//    public Node2(LngLat lng, LngLat lat) {
//        this.lng = lng;
//        this.lat = lat;
//        parent = null;
//        f = 0;
//        g = 0;
//        h = 0;
//    }
//
//
//    @Override
//    public int hashCode(){
//        return Objects.hash(lng, lat);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//
//        if (obj == null || getClass() != obj.getClass()) {
//            return false;
//        }
//
//        Node2 other = (Node2) obj;
//        return Objects.equals(lng, other.lng) && Objects.equals(lat, other.lat);
//    }
//
//}
