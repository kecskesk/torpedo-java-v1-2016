package com.torpedogame.v1.utility;

import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.List;

/**
 * Created by Dombi Soma on 05/11/2016.
 */
public class TargetingComputer {
    private static int TORPEDO_SPEED;
    private static int TORPEDO_RANGE;

    public static void setTorpedoSpeed(int torpedoSpeed) {
        TargetingComputer.TORPEDO_SPEED = torpedoSpeed;
    }

    public static void setTorpedoRange(int torpedoRange) {
        TargetingComputer.TORPEDO_RANGE = torpedoRange;
    }

    /**
     * This function returns the angle that the torpedo should be fired in
     * to hit specified target with the given speed vector
     *
     * @param currentPosition The position of the ship
     * @param targetPosition  The position of the target
     * @param targetVelocity  The velocity of the target
     * @param targetAngle     The angle of the target
     * @return The angle in which the torpedo should be fired
     */
    public static double getShootingAngle(Coordinate currentPosition, Coordinate targetPosition, double targetVelocity, double targetAngle) {

        List<Coordinate> expectedTargetRoute = NavigationComputer.getExpectedRoute(targetPosition, targetVelocity, targetAngle, TORPEDO_RANGE);

        // The position where the target will be when the torpedo hits in
        Coordinate impactPosition = null;

        for (int i = 1; i < TORPEDO_RANGE; i++) {
            Coordinate expectedPosition = expectedTargetRoute.get(i);
            double roundToGetThere = currentPosition.distance(expectedPosition) / TORPEDO_SPEED;
            if (roundToGetThere >= i - 1 && roundToGetThere < i) {
                impactPosition = expectedPosition;
            }
            System.out.println();
        }

        // The impact position should be always predictable
        if (impactPosition == null) {
            throw new IllegalStateException("The impact position should be always predictable!");
        }

        // This solution the rounding problem is copied from here
        // http://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java
        return Math.round(Math.toDegrees(Angle.angle(currentPosition, impactPosition)) * 100.0) / 100.0;
    }
}