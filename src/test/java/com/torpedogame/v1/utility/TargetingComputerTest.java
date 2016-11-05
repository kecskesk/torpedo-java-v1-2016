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

}
