package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import java.time.DayOfWeek;
import java.util.*;

/**
 *This class is used to contain vital features that completely define a move that the drone makes.
 */
public class Node {
    public LngLat location;   //node position

    public int f, g, h; // A* algorithm value parameters

    public Node parent;  //parent record: come from

    public Node(LngLat location) {
        this.location = location;
        parent = null;
        f = 0;
        g = 0;
        h = 0;
    }

    @Override
    public int hashCode(){
        return Objects.hash(location);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }

        if(obj == null || getClass() != obj.getClass()){
            return false;
        }

        Node other = (Node)obj;
        return other.location == location;
    }

}
