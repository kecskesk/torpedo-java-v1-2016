package com.torpedogame.v1.utility;

import com.vividsolutions.jts.geom.Coordinate;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class ShootingComputerTest extends TestCase {

    public ShootingComputerTest(String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( ShootingComputerTest.class );
    }

    /**
     * The target is right ahead of us. More specifically it's speed vector is pointing
     * exactly on us.
     */

    public void testFrontalAngle()
    {
        // Before
        ShootingComputer.setTorpedoSpeed(40);
        ShootingComputer.setTorpedoRange(20);

        // Arrange
        Coordinate currentPos = new Coordinate(200, 200);
        Coordinate targetPos = new Coordinate(300, 200);
        int targetVelocity = 20;
        double targetAngle = 180;

        double expectedAngle = 0.0;

        // Act
        double actualAngle = ShootingComputer.getShootingAngle(currentPos, targetPos, targetVelocity, targetAngle);


        // Assert
        assertTrue(actualAngle == expectedAngle); // The steering should be the maximum possible

    }
//    public void testRegression1()
//    {
//        // Before
//        ShootingComputer.setTorpedoSpeed(40);
//        ShootingComputer.setTorpedoRange(10);
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
//        double actualAngle = ShootingComputer.getShootingAngle(currentPos, targetPos, targetVelocity, targetAngle);
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
//        ShootingComputer.setTorpedoSpeed(40);
//        ShootingComputer.setTorpedoRange(10);
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
//        double actualAngle = ShootingComputer.getShootingAngle(currentPos, targetPos, targetVelocity, targetAngle);
//        System.out.println("asd");
//        System.out.println(actualAngle);
//
//        // Assert
//        assertTrue(actualAngle == expectedAngle); // The steering should be the maximum possible
//
//    }

}
