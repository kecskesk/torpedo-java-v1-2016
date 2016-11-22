package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.torpedogame.v1.model.utility.MoveModification;
import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.BoundaryOp;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a static method collection for the navigation related calculations.
 * Created by Dombi Soma on 03/11/2016.
 */
public class NavigationComputer {
    private static int width;
    private static int height;
    private static List<Coordinate> islandPositions;
    private static int islandSize;

    private static double MAX_STEERING_PER_ROUND;
    private static double MAX_ACCELERATION_PER_ROUND;
    private static double MAX_SPEED;
    private static double MIN_SPEED;
    private static double SONAR_RANGE;

    private static double DANGER_ZONE_THRESHOLD;

    private static List<Coordinate> PATROL_COORDINATES;


    public static void setWidth(int width) {
        NavigationComputer.width = width;
    }

    public static void setHeight(int height) {
        NavigationComputer.height = height;
    }

    public static void setIslandPositions(List<Coordinate> islandPositions) {
        NavigationComputer.islandPositions = islandPositions;
    }

    public static void setIslandSize(int islandSize) {
        NavigationComputer.islandSize = islandSize;
    }

    public static void setMaxSteeringPerRound(double maxSteeringPerRound) {
        NavigationComputer.MAX_STEERING_PER_ROUND = maxSteeringPerRound;
    }

    public static void setMaxAccelerationPerRound(int maxAccelerationPerRound) {
        NavigationComputer.MAX_ACCELERATION_PER_ROUND = maxAccelerationPerRound;
    }

    public static void setMaxSpeed(double maxSpeed) {
        NavigationComputer.MAX_SPEED = maxSpeed;
    }

    public static void setMinSpeed(double minSpeed) {
        NavigationComputer.MIN_SPEED = minSpeed;
    }

    public static void setSonarRange(double sonarRange) {
        NavigationComputer.SONAR_RANGE = sonarRange;
    }
    public static void setDangerZoneThreshold(double dangerZoneThreshold) {
        NavigationComputer.DANGER_ZONE_THRESHOLD = dangerZoneThreshold;
    }
    public static void setPatrolCoordinates(List<Coordinate> patrolCoordinates) {
        NavigationComputer.PATROL_COORDINATES = patrolCoordinates;
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
        double minimumDistance = 10000;
        MoveModification minimumMoveModification = new MoveModification(0,0);

        if (currentVelocity < 1) {
            // We're basically standing still
            // Only the angle should determine the move modification
            Coordinate expectedPosition = getExpectedPosition(currentPosition, currentVelocity+1, currentAngle);
            double angleModification = getAngleModification(expectedPosition, currentPosition, targetPosition);

            // Check if target is within steering range
            if (Math.abs(angleModification) < MAX_STEERING_PER_ROUND) {
                minimumMoveModification = new MoveModification(MAX_ACCELERATION_PER_ROUND, angleModification);
            } else if (angleModification < 0) { // Target is outside steering range and we have to turn clockwise.
                minimumMoveModification = new MoveModification(MAX_ACCELERATION_PER_ROUND, -MAX_STEERING_PER_ROUND);
            } else { // Target is outside steering range and we have to turn counterclockwise.
                minimumMoveModification = new MoveModification(MAX_ACCELERATION_PER_ROUND, MAX_STEERING_PER_ROUND);
            }
        } else {
            for (double d = -MAX_STEERING_PER_ROUND; d <= MAX_STEERING_PER_ROUND; d += 2 * MAX_STEERING_PER_ROUND / 40) {
                double tempAngle = currentAngle + d;
                // Calculate expected positions for slower speeds
                if (currentVelocity > MIN_SPEED) {
                    Coordinate slowerPosition = getExpectedPosition(currentPosition, currentVelocity - MAX_ACCELERATION_PER_ROUND, tempAngle);
                    double slowerDistance = targetPosition.distance(slowerPosition);

                    if (slowerDistance < minimumDistance) {
                        minimumDistance = slowerDistance;
                        minimumMoveModification = new MoveModification(-MAX_ACCELERATION_PER_ROUND, d); // Since d is the
                    }
                }

                // Calculate expected positions for faster speeds
                if (currentVelocity < MAX_SPEED) {
                    Coordinate fasterPosition = getExpectedPosition(currentPosition, currentVelocity + MAX_ACCELERATION_PER_ROUND, tempAngle);
                    double fasterDistance = targetPosition.distance(fasterPosition);

                    if (fasterDistance < minimumDistance) {
                        minimumDistance = fasterDistance;
                        minimumMoveModification = new MoveModification(MAX_ACCELERATION_PER_ROUND, d);
                    }
                }

                // Calculate the expected position with no speed change
                Coordinate expectedPosition = getExpectedPosition(currentPosition, currentVelocity, tempAngle);
                double expectedDistance = targetPosition.distance(expectedPosition);

                if (expectedDistance < minimumDistance) {
                    minimumDistance = expectedDistance;
                    minimumMoveModification = new MoveModification(0, d);
                }
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
        for (int i = 0; i < length; i++) {
            // Calculate the next coordinate based on the previous.
            expectedPositions.add(NavigationComputer.getExpectedPosition(expectedPositions.get(i), currentVelocity, currentAngle));
        }

        return expectedPositions;
    }
    
    public static boolean isTargetOnMap(Coordinate target) {
        // Shooting off the left/right egdes
        if (target.x < 0 || target.x > width) {
            return false;
        }
        
        // Shooting above/below the map
        if (target.y < 0 || target.y > height) {
            return false;
        }
        
        // Hitting an island
        for (Coordinate island : islandPositions) {
            Double size = new Double(islandSize);
            Double dist = island.distance(target);
            if (dist < size) {
                return false;
            }
        }
        
        return true;
    }

    public static MoveModification getSlowerMoveModification() {
        return new MoveModification( - MAX_ACCELERATION_PER_ROUND,0);
    }

    /**
     * Return the angle modification based on the current position vector and the target vector
     * @param expectedPosition
     * @param currentPosition
     * @param targetPosition
     * @return positive if the modification is counterclockwise otherwise negative.
     */
    public static double getAngleModification(Coordinate expectedPosition, Coordinate currentPosition, Coordinate targetPosition) {
        double orientedAngle = Angle.angleBetweenOriented(expectedPosition, currentPosition, targetPosition);
        return Angle.toDegrees(orientedAngle);
    }

    private static double getDistance(Coordinate currentPos, double velocity, double angle, Coordinate targetPos) {
       Coordinate fasterPosition = getExpectedPosition(currentPos, velocity, angle);
       return targetPos.distance(fasterPosition);
    }

    public static boolean isInDangerZone(Coordinate currentPos) {
        for (Coordinate island : islandPositions) {
            if (island.distance(currentPos) < DANGER_ZONE_THRESHOLD + islandSize) {
                return true;
            }
        }
        return false;
    }

    public static Coordinate getIntermediateTarget(Coordinate submarinePos, Coordinate target) {
        Coordinate islandPos = islandPositions.get(0);
        for (Coordinate island : islandPositions) {
            if (island.distance(submarinePos) < DANGER_ZONE_THRESHOLD + islandSize) {
                islandPos = island;
                break;
            }
        }
        //int safeMapMaxX = (int)Math.floor(mapConfiguration.getWidth() - DANGER_ZONE_THRESHOLD);
        //int safeMapMaxY = (int)Math.floor(mapConfiguration.getHeight() - DANGER_ZONE_THRESHOLD);

        int safeIslandSize = (int)Math.ceil(islandSize + DANGER_ZONE_THRESHOLD);
        //Envelope safeMap = new Envelope(DANGER_ZONE_THRESHOLD, safeMapMaxX, DANGER_ZONE_THRESHOLD, safeMapMaxY);
        Geometry island = getIsland(islandPos, safeIslandSize);
        //Geometry map = getMap(safeMap);

        Point currentPosition = new GeometryFactory().createPoint(submarinePos);
        LineString line = new GeometryFactory().createLineString(new Coordinate[]{submarinePos, island.getBoundary().getCoordinate()});
        return line.getStartPoint().getCoordinate();
    }

    private static Geometry getIsland(Coordinate center, int size) {
        GeometricShapeFactory islandFactory = new GeometricShapeFactory();
        islandFactory.setCentre(center);
        islandFactory.setSize(size);
        return islandFactory.createEllipse();
    }

    private static Geometry getMap(Envelope map) {
        GeometricShapeFactory mapFactory = new GeometricShapeFactory();
        mapFactory.setEnvelope(map);
        return mapFactory.createRectangle();
    }

    //  2 | 1
    // -------
    //  3 | 4
    public static Coordinate getNextPatrolTarget (Coordinate startingPosition, boolean patrolClockwise) {
        int quarter = getQuarter(startingPosition);
        int index = quarter - 1;
        if (patrolClockwise) {
            if (quarter == 1) {
                return PATROL_COORDINATES.get(3);
            } else {
                return PATROL_COORDINATES.get(quarter - 2); // 2 because of indexing start from 0, target for quarter III is at position 2
            }
        } else {
            if (quarter == 4) {
                return PATROL_COORDINATES.get(0);
            } else {
                return PATROL_COORDINATES.get(quarter);
            }
        }
    }

    //  2 | 1
    // -------
    //  3 | 4
//    public static Coordinate getNextPatrolTarget2 (Coordinate startingPosition, boolean patrolClockwise, Coordinate reachedTarget) {
//        if (reachedTarget == null) {
//            double minDist = 10000000;
//            Coordinate minTarget = new Coordinate();
//            for (Coordinate c: PATROL_COORDINATES ) {
//                if (c.distance(startingPosition) < minDist){
//                    minDist = c.distance(startingPosition);
//                    minTarget = c;
//                }
//            }
//            return minTarget;
//        }
//        int reachedTargetIndex = PATROL_COORDINATES.indexOf(reachedTarget);
//        int nextIndex = 0;
//        if(reachedTargetIndex > -1) {
//            if (patrolClockwise)  {
//                nextIndex = reachedTargetIndex == 0? PATROL_COORDINATES.size()-1 : reachedTargetIndex-1;
//            } else {
//                nextIndex = reachedTargetIndex == PATROL_COORDINATES.size()-1? 0 : reachedTargetIndex + 1;
//            }
//        }
//        return PATROL_COORDINATES.get(nextIndex);
//    }

        //  2 | 1
    // -------
    //  3 | 4
    public static boolean patrolClockwise(Coordinate startingPosition) {
        int quarter = getQuarter(startingPosition);
        if (quarter == 1 || quarter == 3) {
            return true;
        } else {
            return false;
        }
    }

    // Returns the number of the quarter where the given coordinate takes place
    //  2 | 1
    // -------
    //  3 | 4
    private static int getQuarter(Coordinate position) {
        if (position.x > width / 2 && position.y > height / 2)  { // RIGHT UP
            return 1;
        } else if (position.x < width / 2 && position.y > height / 2) {  // LEFT UP
            return 2;
        } else  if (position.x < width / 2 && position.y < height / 2) { // LEFT DOWN
            return 3;
        } else {
            return 4; // RIGHT DOWN
        }
    }

    //  2 | 1
    // -------
    //  3 | 4
    public static void initializePatrolCoordinates() {
        PATROL_COORDINATES = new ArrayList<>();

        // Order is IMPORTANT
        PATROL_COORDINATES.add(new Coordinate(width - 200, height - 200));  // I quarter
        PATROL_COORDINATES.add(new Coordinate(200, height - 200));          // II quarter
        PATROL_COORDINATES.add(new Coordinate(200, 200));                   // III quarter
        PATROL_COORDINATES.add(new Coordinate(width - 200, 200));           // IV quarter

    }
}