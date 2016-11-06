package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.utility.MoveModification;
import com.vividsolutions.jts.geom.Coordinate;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class TargetingComputerTest extends TestCase {

    public TargetingComputerTest(String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( TargetingComputerTest.class );
    }

    /**
     * The target is right ahead of us. More specifically it's speed vector is pointing
     * exactly on us.
     */

    public void testFrontalAngle()
    {
        // Before
        TargetingComputer.setTorpedoSpeed(40);
        TargetingComputer.setTorpedoRange(20);

        // Arrange
        Coordinate currentPos = new Coordinate(200, 200);
        Coordinate targetPos = new Coordinate(300, 200);
        int targetVelocity = 20;
        double targetAngle = 180;

        double expectedAngle = 0.0;

        // Act
        double actualAngle = TargetingComputer.getShootingAngle(currentPos, targetPos, targetVelocity, targetAngle);


        // Assert
        assertTrue(actualAngle == expectedAngle); // The steering should be the maximum possible

    }
//    public void testRegression1()
//    {
//        // Before
//        TargetingComputer.setTorpedoSpeed(40);
//        TargetingComputer.setTorpedoRange(10);
//
//        // Arrange
//        Coordinate currentPos = new Coordinate(854.2704297427024, 170.1201065802797);
//        Coordinate targetPos = new Coordinate(988.8001880540814, 185.38868751025012);
//        int targetVelocity = 20;
//        double targetAngle = 255;
//
//        double expectedAngle = 0.0;
//
//        // Act
//        double actualAngle = TargetingComputer.getShootingAngle(currentPos, targetPos, targetVelocity, targetAngle);
//        System.out.println("asd");
//        System.out.println(actualAngle);
//
//        // Assert
//        assertTrue(actualAngle == expectedAngle); // The steering should be the maximum possible
//
//    }
//    public void testRegression2()
//    {
//        // Before
//        TargetingComputer.setTorpedoSpeed(40);
//        TargetingComputer.setTorpedoRange(10);
//
//        // Arrange
//        Coordinate currentPos = new Coordinate(598.2704001669214, 215.3727403655713);
//        Coordinate targetPos = new Coordinate(709.5949890059077, 248.1917190268549);
//        int targetVelocity = 20;
//        double targetAngle = 165;
//
//        double expectedAngle = 0.0;
//
//        // Act
//        double actualAngle = TargetingComputer.getShootingAngle(currentPos, targetPos, targetVelocity, targetAngle);
//        System.out.println("asd");
//        System.out.println(actualAngle);
//
//        // Assert
//        assertTrue(actualAngle == expectedAngle); // The steering should be the maximum possible
//
//    }

}
