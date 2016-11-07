package com.torpedogame.v1.utility;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import java.util.*;

/**
 * Created by user on 11/5/2016.
 */
public class TargetComputer {
    private static MapConfiguration mapConfiguration;
    private static final int TARGET_MAX_DISTANCE_FACTOR = 3;
    private static final int TARGET_DISTANCE_THRESHOLD = 50;
    private static Map<Boolean, Map<Boolean, List<Coordinate>>> SUBMARINE_TARGETS;
    static {
        SUBMARINE_TARGETS = new HashMap<>();
    }
    public static void setMapConfiguration(MapConfiguration mapConfiguration) {
        TargetComputer.mapConfiguration = mapConfiguration;
    }

    public static Coordinate getNextTarget(Coordinate currentPosition, Coordinate currentTarget, boolean isLeftSide) {
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

        Geometry halfSideMap = getHalfSideMap(isLeftSide, mapWithHole);
        if (currentTarget == null) {
            List<Coordinate> targets = getTargetList(halfSideMap, sonarRange, safeIslandSize, isLeftSide);
            Map<Boolean, List<Coordinate>> targetMap = new HashMap<>();
            targetMap.put(false, targets);
            targetMap.put(true, new ArrayList<>());
            SUBMARINE_TARGETS.put(isLeftSide, targetMap);
            return targets.get(0);
        } else if (isTargetWithinDistance) {
            Map<Boolean, List<Coordinate>> targetForThisSide = SUBMARINE_TARGETS.get(isLeftSide);
            List<Coordinate> notVisited = targetForThisSide.get(false);
            List<Coordinate> visited = targetForThisSide.get(true);
            if (notVisited.contains(currentTarget)) {
                int indexOfThis = notVisited.indexOf(currentTarget);
                notVisited.remove(indexOfThis);
                visited.add(currentTarget);
            }

            if (notVisited.size() == 0) {
                notVisited = new ArrayList<>(visited);
                Collections.reverse(notVisited);
                Coordinate first = notVisited.get(0);
                visited.clear();
                visited.add(first);
                targetForThisSide.put(true, visited);
                targetForThisSide.put(false, notVisited);
            }

            return notVisited.get(0);
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

    private static List<Coordinate> getTargetList(Geometry map, int sonarRange, int safeIslandSize, boolean isLeftSide) {
        Envelope realMap = map.getEnvelopeInternal();
        List<Coordinate> targetList = new ArrayList<>();
        int sonarOverLap = 10;
        int realLength = (int) (realMap.getWidth() - safeIslandSize);
        int incrementX = sonarRange - sonarOverLap;
        int count = realLength / incrementX;
        int totalX = isLeftSide ? (int)realMap.getMinX() : (int)realMap.getMinX() + safeIslandSize;
        for (int i = 0; i < count; i++) {
            double y = i % 2 == 0 ? (realMap.getMaxY() - 20) : (realMap.getMinY() - 20);
            Coordinate target = new Coordinate(totalX, y);
            totalX += incrementX;
            targetList.add(target);
        }

        return targetList;
    }

    private static Coordinate getRandomTarget(Geometry map, int sonarRange, Coordinate currentPosition) {
        boolean targetFound = false;
        Point nextTargetCoords = null;
        Random rand = new Random();
        Envelope mapBoundary = map.getEnvelopeInternal();
        int maxDistance = TARGET_MAX_DISTANCE_FACTOR * sonarRange;

        // By default, get the whole map boundary as the target range. This is normally half of the map (left or right side).
        int minX = (int)mapBoundary.getMinX();
        int maxX = (int)mapBoundary.getMaxX();
        int minY = (int)mapBoundary.getMinY();
        int maxY = (int)mapBoundary.getMaxY();

        // If ship is already inside the boundary, select a target within maxDistance of current position.
        if ((currentPosition.x - maxDistance) > minX && (currentPosition.x + maxDistance) < maxX) {
            minX = (int)(currentPosition.x - maxDistance);
            maxX = (int)(currentPosition.x + maxDistance);
        }

        if ((currentPosition.y - maxDistance) > minY && (currentPosition.y + maxDistance) < maxY) {
            minY = (int) (currentPosition.y - maxDistance);
            maxY = (int) (currentPosition.y + maxDistance);
        }

        // Generate random coordinates until we find one that is on the map and not on the island.
        while (!targetFound) {
            int targetWidth = rand.nextInt(maxX - minX + 1) + minX;
            int targetHeight = rand.nextInt(maxY - minY +1) + minY;
            nextTargetCoords = new GeometryFactory().createPoint(new Coordinate(targetWidth, targetHeight));
            // Make sure it's not on the island
            if (nextTargetCoords.within(map)) {
                targetFound = true;
            }
        }

        return nextTargetCoords.getCoordinate();
    }

    private static Geometry getHalfSideMap(boolean leftSide, Geometry mapWithHole) {
        Geometry halfMap;
        Envelope map = mapWithHole.getEnvelopeInternal();

        if (leftSide) {
            Envelope halfRectangle = new Envelope(map.getMinX(), (map.getMaxX() / 2), map.getMinY(), map.getMaxY());
            Geometry leftSideMap = getMap(halfRectangle);
            halfMap = mapWithHole.intersection(leftSideMap);
        } else {
            Envelope halfRectangle = new Envelope((map.getMaxX() / 2), map.getMaxX(), map.getMinY(), map.getMaxY());
            Geometry rightSideMap = getMap(halfRectangle);
            halfMap = mapWithHole.intersection(rightSideMap);
        }

        return halfMap;
    }
}