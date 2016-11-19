package com.torpedogame.v1.model.strategy;

import com.torpedogame.v1.model.protocol.Submarine;
import com.torpedogame.v1.model.utility.MoveModification;
import com.torpedogame.v1.utility.NavigationComputer;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a collection of ships that are supposed to move in formation.
 * <p>
 * Created by Dombi Soma on 19/11/2016.
 */
public class Fleet {
    // This map stores the submarines with their relative position in the formation
    private Map<Integer, Coordinate> formation = new HashMap<>();

    // The current movement target of the fleet
    // The fleet will move towards this coordinate in formation
    private Coordinate target;

    private List<Submarine> submarines;

    private final Integer TARGET_REACHING_THRESHOLD = 100;
    /**
     * This function ensures the fleet to keep formation.
     * TODO implement move in formation logic here
     * @return The MoveModification for each registered ship to stay in formation
     */
    public Map<Integer, MoveModification> getMoveModifications() {
        Map<Integer, MoveModification> moveModifications = new HashMap<>();

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

    public void setSubmarines(List<Submarine> subs) {this.submarines = subs;}

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
