package com.torpedogame.v1.model.strategy;

import com.torpedogame.v1.model.protocol.Entity;
import com.torpedogame.v1.model.protocol.Submarine;
import com.torpedogame.v1.model.utility.MoveModification;
import com.torpedogame.v1.utility.NavigationComputer;
import com.torpedogame.v1.utility.ShootingComputer;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a collection of ships that are supposed to move in submarinesRelativePositions.
 *
 * Created by Dombi Soma on 19/11/2016.
 */
public class Fleet {
    // This map stores the submarines with their relative position to the FLAGSHIP
    private Map<Integer, Coordinate> submarinesRelativePositions = new HashMap<>();

    // The current movement target of the fleet
    // The fleet will move towards this coordinate in submarinesRelativePositions
    private Coordinate target;
    
    /**
     * Contains the cool-down times of all the submarines in the list
     */
    private Map<Integer, Integer> sonarCooldowns = new HashMap<>();

    // The list of the fleet members
    // SHOULD BE SET BY EVERY ROUND
    // ***The first element of the list is considered as the FLAGSHIP,
    // ***which is a pivot point for all the other ships in submarinesRelativePositions movement.
    private List<Submarine> submarines;

    // The list of the visible entities
    // SHOULD BE SET BY EVERY ROUND
    private List <Entity> visibleEntities;

    private int fleetSpeed = 10;

    private String formation;

    private final Integer TARGET_REACHING_THRESHOLD = 50;
    /**
     * This function ensures the fleet to keep submarinesRelativePositions.
     * @return The MoveModification for each registered ship to stay in submarinesRelativePositions
     */
    public Map<Integer, MoveModification> getMoveModifications() {
        Map<Integer, MoveModification> moveModifications = new HashMap<>();
        // TODO Check for dangerous torpedoes
        // TODO Add rotation of the formation
        // For each registered ship
        for (Submarine submarine : submarines) {
            if (target != null) {
                if (submarines.indexOf(submarine) == 0) {
                    // FLAGSHIP MoveModification
                    MoveModification flagshipMM = NavigationComputer.getMoveModification(submarine.getPosition(), target, submarine.getVelocity(), submarine.getAngle());
//                    if (submarine.getVelocity() + flagshipMM.getSpeed() > fleetSpeed) flagshipMM.setSpeed(0);
                    System.out.println("QWER " + flagshipMM);
                    moveModifications.put(submarine.getId(), flagshipMM);
                } else {
                    // Other MoveModification
                    Coordinate relativePosition = new Coordinate();
                    // Calculate target
                    if (submarinesRelativePositions.containsKey(submarine.getId())) {
                        relativePosition = submarinesRelativePositions.get(submarine.getId());
                    }
                    Coordinate flagshipCoordinate = submarines.get(0).getPosition();
                    Coordinate targetCoordinate = new Coordinate(flagshipCoordinate.x + relativePosition.x, flagshipCoordinate.y + relativePosition.y);
                    MoveModification asd = NavigationComputer.getMoveModification(submarine.getPosition(), targetCoordinate, submarine.getVelocity(), submarine.getAngle());
                    System.out.println("QWER " + asd);
                    moveModifications.put(submarine.getId(),asd);
                }
            } else if (submarine.getVelocity() > 0) {
                moveModifications.put(submarine.getId(), NavigationComputer.getSlowerMoveModification());
            }
        }
        return moveModifications;
    }

    /**
     * IMPORTANT: Only include ships that can fire and are not on cooldown.
     * @return
     */
    public Map<Integer, Double> getShootingAngles() {
        // TODO avoid friendly fire
        Map<Integer, Double> shootingAngles = new HashMap<>();
        for(Submarine submarine : submarines){
            if(submarine.getTorpedoCooldown() == 0) {
                for (Entity e: visibleEntities){
                    if(!e.getOwner().getName().equals("Thats No Moon") && e.getType().equals("Submarine")) {
                        try {
                            shootingAngles.put(submarine.getId(), ShootingComputer.getShootingAngle(submarine.getPosition(), e.getPosition(), e.getVelocity(), e.getAngle()));
                            break;
                        } catch (Exception ise) {
                            System.out.println(ise.getMessage());
                        }
                    }
                }
            } else {
                System.out.println("Reload is complete in " + submarine.getTorpedoCooldown() + " rounds.");
            }
        }
        return shootingAngles;
    }

    /**
     * @return True if the fleet has reached it's current target
     */
    public boolean hasReachedTarget() {
        if (target == null) return false;
        for(Submarine sub : submarines) {
            Coordinate relPos = submarinesRelativePositions.get(sub.getId());
            Coordinate targetPos = new Coordinate(target.x + relPos.x, target.y + relPos.y);
            if(sub.getPosition().distance(targetPos) > TARGET_REACHING_THRESHOLD) return false;
        }
        return true;
    }

    /**
     * This function sets the relative position for the given ship,
     * and adds it to the fleet if previously was not present.
     */
    public void setSubmarinesRelativePosition(Integer submarineId, Coordinate relativePosition) {
        this.submarinesRelativePositions.put(submarineId, relativePosition);
    }

    /**
     * This function sets the ship list of the fleet and designates their relative position
     */
    public void setSubmarines(List<Submarine> subs) {
        // Set relative position of submarines
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(0, 0));
        coordinates.add(new Coordinate(100, -100));
        coordinates.add(new Coordinate(-100, 100));


        for (int i = 0; i < subs.size(); i++) {
            Submarine s = subs.get(i);
            Integer id = s.getId();
            System.out.println("rel pos for " + id + " is " + coordinates.get(i));
            this.setSubmarinesRelativePosition(id, coordinates.get(i));
        }

        this.submarines = subs;
    }

    public void setVisibleEntities(List<Entity> entities) {
        this.visibleEntities = entities;
    }

    /**
     * Pretty obvious...
     */
    public void setTarget(Coordinate newTarget) {
        this.target = newTarget;
    }

    public Coordinate getTarget() {
        return target;
    }

    public Map<Integer, Integer> getSonarCooldowns() {
        return sonarCooldowns;
    }

    public void printFleetInfo() {
        for(Submarine s: submarines) printSubmarineInformation(s);
    }

    public Coordinate getFlagshipPosition() {
        return submarines.get(0).getPosition();
    }

    public boolean hasTarget () {
        return target != null;
    }

    public boolean isInDangerZone() {
        for(Submarine submarine: submarines){
            if(NavigationComputer.isInDangerZone(submarine.getPosition())) {
                return true;
            }
        }
        return false;
    }
    private void printSubmarineInformation(Submarine submarine) {
        System.out.println("SHIP " + submarine.getId());
        System.out.println("position: " + submarine.getPosition());
        System.out.println("angle: " + submarine.getAngle());
        System.out.println("speed: " + submarine.getVelocity());
    }

    public void updateCooldowns() {
        sonarCooldowns = new HashMap<>();
        for (Submarine sub: submarines) {
            sonarCooldowns.put(sub.getId(), sub.getSonarCooldown());
        }
    }
}
