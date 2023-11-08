package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;

public class LngLatHandlerTest extends TestCase {
    private final LngLat appletonTower = new LngLat(-3.186874, 55.944494);
    private final LngLatHandler handler = new LngLatHandler();
    private final LngLat businessSchool = new LngLat(-3.1873, 55.9430);
    private final LngLat greyYard = new LngLat(-3.1928, 55.9469);

    public void testDistanceTo() {
        LngLat start = new LngLat(9.0, 6.0);
        LngLat end = new LngLat(5.0, 3.0);
        LngLatHandler handler = new LngLatHandler();
        double distance = handler.distanceTo(start, end);
        assertEquals(5.0, distance);
    }

    public void testCloseToTrue() {
        LngLat alsoAppletonTower = new LngLat(-3.186767933982822, 55.94460006601717);
        assertTrue(handler.isCloseTo(appletonTower, alsoAppletonTower));
    }

    public void testCloseToFalse() {
        assertFalse(handler.isCloseTo(appletonTower, businessSchool));
    }
    /**
     add a test for isInCentralRegion!!!
     For the time being, you should just test if a point is in a NamedRegion -
     here you could create any dummy region for your cases.*/

    public void testIsInRegion() {
        LngLatHandler handler = new LngLatHandler();

        // Create a sample polygon (rectangle for example)
        LngLat[] vertices = {
                new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617),
                new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)
        };
        NamedRegion region = new NamedRegion("Sample Region", vertices);

        // Test points inside and outside the region
        assertTrue(handler.isInRegion(appletonTower, region)); // Inside
        assertFalse(handler.isInRegion(greyYard, region)); // Outside
    }

    public void testIsInRegionBoundary(){
        var lngLatHandler = new LngLatHandler();
        LngLat lngLat = new LngLat(-3.192473, 55.943);
        LngLat[] vertices = new LngLat[]{new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617), new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)};
        NamedRegion namedRegion = new NamedRegion("Central", vertices);
        assertTrue(lngLatHandler.isInRegion(lngLat, namedRegion));
    }

    public void testIsInRegionOnBorder(){
        var lngLatHandler = new LngLatHandler();
        LngLat lngLat = new LngLat(-3.192473, 55.942617);
        LngLat[] vertices = new LngLat[]{new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617), new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)};
        NamedRegion namedRegion = new NamedRegion("Central", vertices);
        assertTrue(lngLatHandler.isInRegion(lngLat, namedRegion));
    }


    public void testAngle90() {
        LngLatHandler handler = new LngLatHandler();
        LngLat startPosition = appletonTower;
        LngLat nextPosition = handler.nextPosition(startPosition, 90);
        LngLat calculatedPosition = new LngLat(-3.186874, 55.944644);
        assertEquals(nextPosition, calculatedPosition);

    }

    public void testAngle999() {
        LngLatHandler handler = new LngLatHandler();
        LngLat nextPosition = handler.nextPosition(appletonTower, 999);
        assertEquals(nextPosition, appletonTower);
    }
}
