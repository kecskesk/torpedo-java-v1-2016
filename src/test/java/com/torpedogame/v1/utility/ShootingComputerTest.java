package com.torpedogame.v1.utility;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class ShootingComputerTest extends TestCase {

    public ShootingComputerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ShootingComputerTest.class);
    }

    /**
     * The target is right ahead of us. More specifically it's speed vector is
     * pointing exactly on us.
     */
    public void testFrontalAngle() {
        setUpMapForTest();
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

    private void setUpMapForTest() {
        NavigationComputer.setHeight(1000);
        NavigationComputer.setWidth(1000);
        NavigationComputer.setIslandPositions(new ArrayList<>());
        NavigationComputer.setIslandSize(10);
    }

}
