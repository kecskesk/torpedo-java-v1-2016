package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.utility.MoveModification;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.math.MathUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;

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
        NavigationComputer.setMinSpeed(0);
        NavigationComputer.setMaxSpeed(20);
        NavigationComputer.setMaxAccelerationPerRound(5);
        // Arrange
        Coordinate currentPos = new Coordinate(2, 1);
        Coordinate targetPos = new Coordinate(200, 1);
        int currentVelocity = 20;
        double currentAngle = 90;

        // Act
        MoveModification moveModification = NavigationComputer.getMoveModification(currentPos, targetPos, currentVelocity, currentAngle);

        // Assert
        assertTrue(moveModification.getTurn() == -maxSteeringPerRound); // The steering should be the maximum possible
        assertTrue(moveModification.getSpeed() == 0); // The steering should be the maximum possible

    }

    public void testStraightForwardMovement()
    {
        // Before
        double maxSteeringPerRound = 20.0;
        NavigationComputer.setMaxSteeringPerRound(maxSteeringPerRound);
        NavigationComputer.setMinSpeed(0);
        NavigationComputer.setMaxSpeed(20);
        NavigationComputer.setMaxAccelerationPerRound(5);

        // Arrange
        Coordinate currentPos = new Coordinate(0, 0);
        Coordinate targetPos = new Coordinate(0, 100);
        int currentVelocity = 20;
        double currentAngle = 90;

        // Act
        MoveModification moveModification = NavigationComputer.getMoveModification(currentPos, targetPos, currentVelocity, currentAngle);

        // Assert
        assertTrue(moveModification.getTurn() == 0); // The steering should be the maximum possible
        assertTrue(moveModification.getSpeed() == 0);
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

    public void testExpectedRoute(){
        // Arrange
        Coordinate currentPos = new Coordinate(200, 200);
        int currentVelocity = 20;
        double currentAngle = 90;
        int length = 3;

        Coordinate expectedPos1 = new Coordinate(200, 220);
        Coordinate expectedPos2 = new Coordinate(200, 240);
        Coordinate expectedPos3 = new Coordinate(200, 260);

        // Act
        List<Coordinate> actualRoute = NavigationComputer.getExpectedRoute(currentPos, currentVelocity, currentAngle, length);

        // Assert
        assertTrue(actualRoute.get(0).equals(currentPos));
        assertTrue(actualRoute.get(1).equals(expectedPos1));
        assertTrue(actualRoute.get(2).equals(expectedPos2));
        assertTrue(actualRoute.get(3).equals(expectedPos3));


    }
}
