package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.utility.MoveModification;
import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class NavigationComputerTest  {

    /**
     * This function tests if a full turn is needed then the NavigationComputer returns the max angle.
     * In this case the ship is moving upwards parallel with Y axis and the target has the same Y coordinate as the ship.
     * If the maxSteeringAngle is smaller then 90 degree, the NavigationComputer should return the maxSteeringAngle in this case.
     */
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    @Test
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
    
    // TESTS FOR isTargetOnMap -----------------------------
    @Test
    public void testShootLeft(){
        setUpMapForTest();
        Coordinate target = new Coordinate(-1 , 12);
        assertFalse(NavigationComputer.isTargetOnMap(target));
    }
    @Test
    public void testShootRight(){
        setUpMapForTest();
        Coordinate target = new Coordinate(121 , 12);
        assertFalse(NavigationComputer.isTargetOnMap(target));
    
    }
    @Test
    public void testShootUp(){
        setUpMapForTest();
        Coordinate target = new Coordinate(12 , 551);
        assertFalse(NavigationComputer.isTargetOnMap(target));
    
    }
    @Test
    public void testShootDown(){
        setUpMapForTest();
        Coordinate target = new Coordinate(-1 , -154);
        assertFalse(NavigationComputer.isTargetOnMap(target));
    
    }
    @Test
    public void testShootIsland(){
        setUpMapForTest();
        Coordinate target = new Coordinate(45 , 45);
        assertFalse(NavigationComputer.isTargetOnMap(target));
    
    }
    @Test
    public void testShootCorrect(){
        setUpMapForTest();
        Coordinate target = new Coordinate(25 , 25);
        assertTrue(NavigationComputer.isTargetOnMap(target));
    
    }

    private void setUpMapForTest() {
        NavigationComputer.setHeight(100);
        NavigationComputer.setWidth(100);
        NavigationComputer.setIslandPositions(Arrays.asList(new Coordinate[]{new Coordinate(50,50)}));
        NavigationComputer.setIslandSize(10);
    }

    @Test
    public void testAngleModificationTargetBelow90() {
        Coordinate expectedPosition = new Coordinate(115, 115);
        Coordinate currentPosition = new Coordinate(100, 100);
        Coordinate targetPosition = new Coordinate(105, 115);
        double angleMod = NavigationComputer.getAngleModification(expectedPosition, currentPosition, targetPosition);
        assertTrue(angleMod < 90);
        assertTrue(angleMod > 0);
    }

    @Test
    public void testAngleModificationTargetAbove90Below180() {
        Coordinate expectedPosition = new Coordinate(115, 115);
        Coordinate currentPosition = new Coordinate(100, 100);
        Coordinate targetPosition = new Coordinate(50, 105);
        double angleMod = NavigationComputer.getAngleModification(expectedPosition, currentPosition, targetPosition);
        assertTrue(angleMod > 45);
        assertTrue(angleMod < 135);
    }

    @Test
    public void testAngleModificationTargetAbove180Below270() {
        Coordinate expectedPosition = new Coordinate(115, 115);
        Coordinate currentPosition = new Coordinate(100, 100);
        Coordinate targetPosition = new Coordinate(90, 50);
        double angleMod = NavigationComputer.getAngleModification(expectedPosition, currentPosition, targetPosition);
        assertTrue(angleMod < -135);
        assertTrue(angleMod > -225);
    }

    @Test
    public void testAngleModificationTargetAbove270() {
        Coordinate expectedPosition = new Coordinate(115, 115);
        Coordinate currentPosition = new Coordinate(100, 100);
        Coordinate targetPosition = new Coordinate(110, 50);
        double angleMod = NavigationComputer.getAngleModification(expectedPosition, currentPosition, targetPosition);
        assertTrue(angleMod < -45);
        assertTrue(angleMod > -135);
    }

}
