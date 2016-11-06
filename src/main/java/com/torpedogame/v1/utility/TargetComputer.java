package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.game_control.MapConfiguration;
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
    private static final int TARGET_MAX_DISTANCE_FACTOR = 2;
    private static final int TARGET_DISTANCE_THRESHOLD = 15;

    public static void setMapConfiguration(MapConfiguration mapConfiguration) {
        TargetComputer.mapConfiguration = mapConfiguration;
    }

    public static Coordinate getNextTarget(Coordinate currentPosition, Coordinate currentTarget) {
        int safeMapMaxX = (int)Math.floor(mapConfiguration.getWidth() - mapConfiguration.getSonarRange());
        int safeMapMaxY = (int)Math.floor(mapConfiguration.getHeight() - mapConfiguration.getSonarRange());

        int safeIslandSize = (int)Math.ceil(mapConfiguration.getIslandSize() + (mapConfiguration.getSonarRange() / 2));
        Envelope safeMap = new Envelope(mapConfiguration.getSonarRange(), safeMapMaxX, mapConfiguration.getSonarRange(), safeMapMaxY);
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

        if (currentTarget == null || isTargetWithinDistance) {
            return getRandomTarget(mapWithHole, sonarRange, currentPosition);
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

    private static Coordinate getRandomTarget(Geometry map, int sonarRange, Coordinate currentPosition) {
        boolean targetFound = false;
        Point nextTargetCoords = null;
        Random rand = new Random();
        Envelope mapBoundary = map.getEnvelopeInternal();
        int maxDistance = TARGET_MAX_DISTANCE_FACTOR * sonarRange;

        while (!targetFound) {
            int minX = currentPosition.x - maxDistance < mapBoundary.getMinX() ? (int)mapBoundary.getMinX() : (int)(currentPosition.x - maxDistance);
            int maxX = currentPosition.x + maxDistance > mapBoundary.getMaxX() ? (int)mapBoundary.getMaxX() : (int)(currentPosition.x + maxDistance);
            int minY = currentPosition.y - maxDistance < mapBoundary.getMinY() ? (int)mapBoundary.getMinY() : (int)(currentPosition.y - maxDistance);
            int maxY = currentPosition.y + maxDistance > mapBoundary.getMaxY() ? (int)mapBoundary.getMaxY() : (int)(currentPosition.y + maxDistance);

            int targetWidth = rand.nextInt(maxX - minX + 1) + minX;
            int targetHeight = rand.nextInt(maxY - minY +1) + minY;
            nextTargetCoords = new GeometryFactory().createPoint(new Coordinate(targetWidth, targetHeight));
            if (nextTargetCoords.within(map)) {
                targetFound = true;
            }
        }

        return nextTargetCoords.getCoordinate();
    }
}

