package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.utility.MoveModification;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.math.MathUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class NavigationComputerTest extends TestCase {

    public NavigationComputerTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( NavigationComputerTest.class );
    }

    /**
     * This function tests if a full turn is needed then the NavigationComputer returns the max angle.
     * In this case the ship is moving upwards parallel with Y axis and the target has the same Y coordinate as the ship.
     * If the maxSteeringAngle is smaller then 90 degree, the NavigationComputer should return the maxSteeringAngle in this case.
     */

    public void testFullTurn()
    {
        // Before
        double maxSteeringPerRound = 20.0;
        NavigationComputer.setMaxSteeringPerRound(maxSteeringPerRound);

        // Arrange
        Coordinate currentPos = new Coordinate(2, 1);
        Coordinate targetPos = new Coordinate(200, 1);
        int currentVelocity = 20;
        double currentAngle = 90;

        // Act
        MoveModification moveModification = NavigationComputer.getMoveModification(currentPos, targetPos, currentVelocity, currentAngle);
        System.out.println(moveModification.getSpeed());
        System.out.println(moveModification.getTurn());

        // Assert
        assertTrue(moveModification.getTurn() == maxSteeringPerRound); // The steering should be the maximum possible
    }
    // TODO fuckin refactor this to parametrized test cases please somebody
    // TODO hint upgrade to JUnit 4.^
    public void testExpectedPositionUpward(){
        // Arrange
        Coordinate currentPos = new Coordinate(1, 2);
        int currentVelocity = 20;
        double currentAngle = 90;

        Coordinate expectedPos = new Coordinate(1, 22);


        // Act
        Coordinate actualPos = NavigationComputer.getExpectedPosition(currentPos, currentVelocity, currentAngle);

        // Assert
        assertTrue(actualPos.equals(expectedPos));
    }

    public void testExpectedPositionDownward(){
        // Arrange
        Coordinate currentPos = new Coordinate(1, 2);
        int currentVelocity = 20;
        double currentAngle = 270;

        Coordinate expectedPos = new Coordinate(1, -18);

        // Act
        Coordinate actualPos = NavigationComputer.getExpectedPosition(currentPos, currentVelocity, currentAngle);

        // Assert
        assertTrue(actualPos.equals(expectedPos));
    }

    public void testExpectedPositionRightward(){
        // Arrange
        Coordinate currentPos = new Coordinate(1, 2);
        int currentVelocity = 20;
        double currentAngle = 0;

        Coordinate expectedPos = new Coordinate(21, 2);

        // Act
        Coordinate actualPos = NavigationComputer.getExpectedPosition(currentPos, currentVelocity, currentAngle);

        // Assert
        assertTrue(actualPos.equals(expectedPos));
    }

    public void testExpectedPositionLeftward(){
        // Arrange
        Coordinate currentPos = new Coordinate(1, 2);
        int currentVelocity = 20;
        double currentAngle = 180;

        Coordinate expectedPos = new Coordinate(-19, 2);

        // Act
        Coordinate actualPos = NavigationComputer.getExpectedPosition(currentPos, currentVelocity, currentAngle);

        // Assert
        assertTrue(actualPos.equals(expectedPos));
    }

    public void testExpectedPositionDiagonal(){
        // Arrange
        Coordinate currentPos = new Coordinate(1, 2);
        int currentVelocity = 20;
        double currentAngle = 30;

        // cos(60 degree) * 20 = 18.32...
        Coordinate expectedPos = new Coordinate(18.32, 12);

        // Act
        Coordinate actualPos = NavigationComputer.getExpectedPosition(currentPos, currentVelocity, currentAngle);

        // Assert
        assertTrue(actualPos.equals(expectedPos));
    }
}
