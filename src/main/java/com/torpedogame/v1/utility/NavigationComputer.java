package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.utility.MoveModification;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a static method collection for the navigation related calculations.
 * Created by Dombi Soma on 03/11/2016.
 */
public class NavigationComputer {

    private static double MAX_STEERING_PER_ROUND;
    private static double MAX_ACCELERATION_PER_ROUND;
    private static double MAX_SPEED;
    private static double MIN_SPEED;


    public static void setMaxSteeringPerRound(double maxSteeringPerRound){
        NavigationComputer.MAX_STEERING_PER_ROUND = maxSteeringPerRound;
    }

    public static void setMaxAccelerationPerRound(int maxAccelerationPerRound){
        NavigationComputer.MAX_ACCELERATION_PER_ROUND = maxAccelerationPerRound;
    }

    public static void setMaxSpeed(double maxSpeed){
        NavigationComputer.MAX_SPEED = maxSpeed;
    }

    public static void setMinSpeed(double minSpeed){
        NavigationComputer.MIN_SPEED = minSpeed;
    }

    /**
     * This function returns the recommended speed vector modification of the ship
     *
     * @param currentPosition The ships current position
     * @param targetPosition The target that the ship wants to reach
     * @param currentVelocity The ships velocity
     * @param currentAngle The ships direction
     * @return The speed and turn value that the ship should modify its speed vector with.
     */
    public static MoveModification getMoveModification(Coordinate currentPosition, Coordinate targetPosition, double currentVelocity, double currentAngle){
        Coordinate positionWithoutMovementModification = getExpectedPosition(currentPosition, currentVelocity, currentAngle);
        double minimumDistance = 10000; // targetPosition.distance(positionWithoutMovementModification);
        MoveModification minimumMoveModification = new MoveModification(0,0);

        for(double d = -MAX_STEERING_PER_ROUND; d <= MAX_STEERING_PER_ROUND; d += 2*MAX_STEERING_PER_ROUND/40) {
            double tempAngle = currentAngle + d;
            // There is a slower speed level
            if(currentVelocity > MIN_SPEED) {
                Coordinate slowerPosition = getExpectedPosition(currentPosition, currentVelocity - MAX_ACCELERATION_PER_ROUND, tempAngle);
                double slowerDistance = targetPosition.distance(slowerPosition);

                if (slowerDistance < minimumDistance) {
                    minimumDistance = slowerDistance;
                    minimumMoveModification = new MoveModification(-MAX_ACCELERATION_PER_ROUND , d); // Since d is the
                }
            }

            // There is a faster speed level
            if (currentVelocity < MAX_SPEED) {
                Coordinate fasterPosition = getExpectedPosition(currentPosition, currentVelocity + MAX_ACCELERATION_PER_ROUND, tempAngle);
                double fasterDistance = targetPosition.distance(fasterPosition);

                if (fasterDistance < minimumDistance) {
                    minimumDistance = fasterDistance;
                    minimumMoveModification = new MoveModification(MAX_ACCELERATION_PER_ROUND , d);
                }
            }

            // Calculate the expected position, with no speed change
            Coordinate expectedPosition = getExpectedPosition(currentPosition, currentVelocity , tempAngle);
            double expectedDistance = targetPosition.distance(expectedPosition);

            if (expectedDistance < minimumDistance) {
                minimumDistance = expectedDistance;
                minimumMoveModification = new MoveModification(0 , d);
            }


        }

        return minimumMoveModification;
    }

    /**
     * This function returns where the ship will be if it is moved by the specified vector(currentVelocity & currentAngle)
     *
     * @param currentPosition The ships current position
     * @param currentVelocity The ships velocity
     * @param currentAngle The ships direction
     * @return The location where the ship will move
     */
    public static Coordinate getExpectedPosition(Coordinate currentPosition, double currentVelocity, double currentAngle){
        // This solution the rounding problem is copied from here
        // http://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java
        double expectedX = Math.round((currentPosition.x + Math.cos(Math.toRadians(currentAngle)) * currentVelocity)*100.0)/100.0;
        double expectedY = Math.round((currentPosition.y + Math.sin(Math.toRadians(currentAngle)) * currentVelocity)*100.0)/100.0;

        return new Coordinate(expectedX, expectedY);
    }

    /**
     * This function returns the expected coordinates of an entity. The first element is the current coordinate of the
     * entity, the rest of the list contains the coordinates of the expected route where the index is the round and the
     * value is coordinate of the coordinate of the entity in that round.
     *
     * NOTE
     * **In case of length = 5, the result list will be 6 long**
     *
     * @param currentPosition The current position of the entity
     * @param currentVelocity The current velocity of the entity
     * @param currentAngle The current angle of the entity
     * @return The Coordinates of the expected route
     */
    public static List<Coordinate> getExpectedRoute(Coordinate currentPosition, double currentVelocity, double currentAngle, int length) {
        // Generate expected positions of the target
        // Each index represents a round and the value is a Coordinate
        // where the target is expected to be in that specific round
        List<Coordinate> expectedPositions = new ArrayList<>();

        // Add the current target position for round 0.
        expectedPositions.add(currentPosition);
        for(int i = 0; i < length; i++) {
            // Calculate the next coordinate based on the previous.
            expectedPositions.add(NavigationComputer.getExpectedPosition(expectedPositions.get(i), currentVelocity, currentAngle));
        }

        return expectedPositions;
    }
}
