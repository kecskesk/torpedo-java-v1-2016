package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import java.util.Random;

/**
 * Created by user on 11/5/2016.
 */
public class TargetComputer {
    private static MapConfiguration mapConfiguration;
    private static final double SAFE_MAP_WIDTH_FACTOR = 0.9;
    private static final double SAFE_MAP_HEIGHT_FACTOR = 0.9;
    private static final double SAFE_ISLAND_SIZE = 1.15;
    private static final int TARGET_DISTANCE_THRESHOLD = 15;
    private static final int TARGET_ANGLE_THRESHOLD = 45;

    public static void setMapConfiguration(MapConfiguration mapConfiguration) {
        TargetComputer.mapConfiguration = mapConfiguration;
    }

    public static Coordinate getNextTarget(boolean firstRound, Coordinate currentPosition, Coordinate currentTarget, double currentSpeed, int currentAngle) {

        int safeMapWidth = (int)Math.floor(mapConfiguration.getWidth() * SAFE_MAP_WIDTH_FACTOR);
        int safeMapHeight = (int)Math.floor(mapConfiguration.getHeight() * SAFE_MAP_HEIGHT_FACTOR);

        int safeIslandSize = (int)Math.floor(mapConfiguration.getIslandSize() * SAFE_ISLAND_SIZE);
        Envelope safeMap = new Envelope(0, safeMapWidth, 0, safeMapHeight);
        Geometry island = getIsland(mapConfiguration.getIslandPositions().get(0), safeIslandSize);
        Geometry map = getMap(safeMap);

        Geometry mapWithHole = map.difference(island);
        int sonarRange = mapConfiguration.getSonarRange();
        Point currentPosPoint = new GeometryFactory().createPoint(currentPosition);
        boolean isTargetWithinDistance = false;
        if (currentTarget != null) {
            Point currentTargetPoint = new GeometryFactory().createPoint(currentTarget);
            isTargetWithinDistance = currentPosPoint.isWithinDistance(currentTargetPoint, TARGET_DISTANCE_THRESHOLD);
        }

        if (firstRound || isTargetWithinDistance) {
            return getRandomTarget(safeMapWidth, safeMapHeight, mapWithHole, currentPosition, currentAngle, sonarRange);
        } else {
            // We haven't reached the target so continue with it.
            return currentTarget;
        }
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

    private static Coordinate getRandomTarget(int mapWidth, int mapHeight, Geometry map, Coordinate currentPosition, int currentAngle, int sonarRange) {
        boolean targetFound = false;
        Point nextTargetCoords = null;
        Random rand = new Random();

        while (!targetFound) {
            int targetWidth = rand.nextInt(((mapWidth / 2) - sonarRange) + 1) + sonarRange;
            int targetHeight = rand.nextInt(((mapHeight / 2) - sonarRange) + 1) + sonarRange;
            nextTargetCoords = new GeometryFactory().createPoint(new Coordinate(targetWidth, targetHeight));
            double newAngle = Angle.toDegrees(Angle.angle(currentPosition, nextTargetCoords.getCoordinate()));
            if (newAngle < 0) {
                newAngle = Math.abs(newAngle) + 180;
            }

            double angleDifference = Math.abs(currentAngle - newAngle);
            if (nextTargetCoords.within(map) && angleDifference < TARGET_ANGLE_THRESHOLD) {
                targetFound = true;
            }
        }

        return nextTargetCoords.getCoordinate();
    }
}

