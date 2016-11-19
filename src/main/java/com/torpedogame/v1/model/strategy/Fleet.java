package com.torpedogame.v1.model.strategy;

import com.torpedogame.v1.model.protocol.Entity;
import com.torpedogame.v1.model.protocol.Submarine;
import com.torpedogame.v1.model.utility.MoveModification;
import com.torpedogame.v1.utility.NavigationComputer;
import com.torpedogame.v1.utility.ShootingComputer;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a collection of ships that are supposed to move in formation.
 *
 * Created by Dombi Soma on 19/11/2016.
 */
public class Fleet {
    // This map stores the submarines with their relative position in the formation
    private Map<Integer, Coordinate> formation = new HashMap<>();

    // The current movement target of the fleet
    // The fleet will move towards this coordinate in formation
    private Coordinate target;

    // The list of the fleet members
    // SHOULD BE SET BY EVERY ROUND
    private List<Submarine> submarines;

    // The list of the visible entities
    // SHOULD BE SET BY EVERY ROUND
    private List <Entity> visibleEntities;

    private final Integer TARGET_REACHING_THRESHOLD = 100;
    /**
     * This function ensures the fleet to keep formation.
     * TODO implement move in formation logic here
     * @return The MoveModification for each registered ship to stay in formation
     */
    public Map<Integer, MoveModification> getMoveModifications() {
        Map<Integer, MoveModification> moveModifications = new HashMap<>();
        // TODO Check for dangerous torpedoes

        // For each registered ship
        for (Submarine submarine : submarines) {

            Coordinate relativePosition = new Coordinate();
            // Calculate target
            if (formation.containsKey(submarine.getId())) {
                relativePosition = formation.get(submarine.getId());
            }
            Coordinate targetCoordinate = new Coordinate(target.x + relativePosition.x, target.y + relativePosition.y);
            moveModifications.put(submarine.getId(), NavigationComputer.getMoveModification(submarine.getPosition(), targetCoordinate, submarine.getVelocity(), submarine.getAngle()));
        }

        return moveModifications;
    }

    /**
     * IMPORTANT: Only include ships that can fire and are not on cooldown.
     * @return
     */
    public Map<Integer, Double> getShootingAngles() {
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
        for(Submarine sub : submarines) {
            if(sub.getPosition().distance(target) < TARGET_REACHING_THRESHOLD) return true;
        }
        return false;
    }

    /**
     * This function sets the relative position for the given ship,
     * and adds it to the fleet if previously was not present.
     */
    public void setSubmarinesRelativePosition(Integer submarineId, Coordinate relativePosition) {
        this.formation.put(submarineId, relativePosition);
    }

    /**
     * This function sets the ship list of the fleet and designates their relative position
     */
    public void setSubmarines(List<Submarine> subs) {
        // Set relative position of submarines
        for (Submarine s : subs) {
            Integer id = s.getId();
            Coordinate relPos;
            if(id % 3 == 0) {
                relPos = new Coordinate(-200, 0);
            } else if (id % 3 == 1) {
                relPos = new Coordinate(200, 0);
            } else {
                relPos = new Coordinate(0, 200);
            }
            this.setSubmarinesRelativePosition(id, relPos);
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

}
