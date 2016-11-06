package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.vividsolutions.jts.geom.Coordinate;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;

/**
 * Created by user on 11/5/2016.
 */
public class TargetComputerTest extends TestCase {
    public TargetComputerTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( TargetComputerTest.class );
    }

    public void testFirstRoundTarget() {
        MapConfiguration mapConfiguration = new MapConfiguration();
        mapConfiguration.setWidth(1000);
        mapConfiguration.setHeight(1500);
        mapConfiguration.setIslandSize(2*50);
        mapConfiguration.setIslandPositions(Arrays.asList(new Coordinate(500, 500)));
        TargetComputer.setMapConfiguration(mapConfiguration);

        Coordinate nextTarget = TargetComputer.getNextTarget(new Coordinate(0, 0), null);
        assertNotNull(nextTarget);
        assertTrue(nextTarget.x <= mapConfiguration.getWidth());
        assertTrue(nextTarget.y <= mapConfiguration.getHeight());
    }

    public void testIsWithinDistance() {
        MapConfiguration mapConfiguration = new MapConfiguration();
        mapConfiguration.setWidth(1000);
        mapConfiguration.setHeight(1500);
        mapConfiguration.setIslandSize(2*50);
        mapConfiguration.setIslandPositions(Arrays.asList(new Coordinate(500, 500)));
        TargetComputer.setMapConfiguration(mapConfiguration);

        Coordinate nextTarget = TargetComputer.getNextTarget(new Coordinate(10, 10), new Coordinate(20, 20));
        assertNotNull(nextTarget);
        assertTrue(nextTarget.x <= mapConfiguration.getWidth());
        assertTrue(nextTarget.y <= mapConfiguration.getHeight());
    }

    public void testIsNotWithinDistance() {
        MapConfiguration mapConfiguration = new MapConfiguration();
        mapConfiguration.setWidth(1000);
        mapConfiguration.setHeight(1500);
        mapConfiguration.setIslandSize(2*50);
        mapConfiguration.setIslandPositions(Arrays.asList(new Coordinate(500, 500)));
        TargetComputer.setMapConfiguration(mapConfiguration);

        Coordinate nextTarget = TargetComputer.getNextTarget(new Coordinate(10, 10), new Coordinate(40, 50));
        assertNotNull(nextTarget);
        assertTrue(nextTarget.x == 40);
        assertTrue(nextTarget.y == 50);
    }

}
