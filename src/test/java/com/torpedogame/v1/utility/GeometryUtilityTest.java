package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.utility.MoveModification;
import com.vividsolutions.jts.geom.Coordinate;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class GeometryUtilityTest extends TestCase {

    public GeometryUtilityTest(String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( GeometryUtilityTest.class );
    }

    // TODO Again parametrized tests are cool things...
    public void testGetDegreeUnder180() {
        // Arrange
        Coordinate p0 = new Coordinate(0, 0);
        Coordinate p1 = new Coordinate(-20, 20);

        double expectedDegree = 135;

        // Act
        double actualDegree = GeometryUtility.getDegree(p0, p1);

        // Assert
        assertTrue(actualDegree == expectedDegree);

    }

    public void testGetDegreeOver180() {
        // Arrange
        Coordinate p0 = new Coordinate(0, 0);
        Coordinate p1 = new Coordinate(-20, -20);

        double expectedDegree = 225;

        // Act
        double actualDegree = GeometryUtility.getDegree(p0, p1);

        // Assert
        assertTrue(actualDegree == expectedDegree);

    }

    public void testGetDegreeZero() {
        // Arrange
        Coordinate p0 = new Coordinate(0, 0);
        Coordinate p1 = new Coordinate(20, 0);

        double expectedDegree = 0;

        // Act
        double actualDegree = GeometryUtility.getDegree(p0, p1);

        // Assert
        assertTrue(actualDegree == expectedDegree);

    }
}
