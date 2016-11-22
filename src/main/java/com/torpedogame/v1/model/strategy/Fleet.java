package com.torpedogame.v1.model.strategy;

import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.torpedogame.v1.model.protocol.Entity;
import com.torpedogame.v1.model.protocol.Submarine;
import com.torpedogame.v1.model.utility.MoveModification;
import com.torpedogame.v1.utility.NavigationComputer;
import com.torpedogame.v1.utility.ShootingComputer;
import com.vividsolutions.jts.algorithm.Angle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.math.Vector2D;

import java.util.ArrayList;
import java.util.Comparator;
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
    private final Map<Integer, Coordinate> submarinesRelativePositions = new HashMap<>();

    // The current movement target of the fleet
    // The fleet will move towards this coordinate in submarinesRelativePositions
    private Coordinate target;
    
    /**
     * Contains the cool-down times of all the submarines in the list
     */
    private Map<Integer, Integer> sonarCooldowns = new HashMap<>();

    private Coordinate intermediateTarget;

    // The list of the fleet members
    // SHOULD BE SET BY EVERY ROUND
    // ***The first element of the list is considered as the FLAGSHIP,
    // ***which is a pivot point for all the other ships in submarinesRelativePositions movement.
    private List<Submarine> submarines;

    // The list of the visible entities
    // SHOULD BE SET BY EVERY ROUND
    private List <Entity> visibleEntities;

    private int fleetSpeed = 15;

    private final Integer TARGET_REACHING_THRESHOLD = 30;

    private Boolean patrolClockwise;
    private MapConfiguration mapConfiguration;

    public Boolean getPatrolClockwise() {
        return patrolClockwise;
    }

    public void setPatrolClockwise(Boolean patrolClockwise) {
        this.patrolClockwise = patrolClockwise;
    }

    public Map<Integer, MoveModification> getMoveModifications() {

        if (hasReachedTarget(intermediateTarget)) {
            intermediateTarget = null;
        }

        if (hasReachedTarget(target)) {
//            target = NavigationComputer.getNextPatrolTarget(getFlagshipPosition(), getPatrolClockwise());
            target = NavigationComputer.getMapCenter();
        }

        Coordinate t = getNextTarget();
        return getMoveModifications(t);
    }

    /**
     * This function ensures the fleet to keep submarinesRelativePositions.
     * @return The MoveModification for each registered ship to stay in submarinesRelativePositions
     */
    private Map<Integer, MoveModification> getMoveModifications(Coordinate target) {
        Map<Integer, MoveModification> moveModifications = new HashMap<>();
        // TODO Check for dangerous torpedoes
        // TODO Add rotation of the formation
        // TODO CRITICAL Robi said he did something to make the ships start with velocity, investigate it may have a bug, one of the ship's speed was jumping between 0 and 5... But only MAY, no it worked correctly
        // For each registered ship
        for (Submarine submarine : submarines) {
            int isInDanger = -1;
            Entity dangerousTorpedo = null;
            for (Entity entity: visibleEntities) {
                System.out.println("domis " + entity.getType());
                if (entity.getType().equals("Torpedo")) {
                    int dangerTemp = ShootingComputer.isTorpedoDangerous(submarine, entity, 6);
                    if (dangerTemp != -1) {
                        if (isInDanger == -1 || dangerTemp < isInDanger) {
                            isInDanger = dangerTemp;
                            dangerousTorpedo = entity;
                        }
                    }
                    
                }
            }
            if(isInDanger >= 0){               
                moveModifications.put(submarine.getId(), calculateEvadingModification(dangerousTorpedo, submarine));
            }else {
                if (target != null) {
                    if (submarines.indexOf(submarine) == 0) {
                        // FLAGSHIP
                        MoveModification flagshipMM = NavigationComputer.getMoveModification(submarine.getPosition(), target, submarine.getVelocity(), submarine.getAngle());
                        System.out.println("QWER " + flagshipMM);
                        if (submarine.getVelocity() + flagshipMM.getSpeed() > fleetSpeed) flagshipMM.setSpeed(0);
                        moveModifications.put(submarine.getId(), flagshipMM);
                    } else {
                        // Other MoveModification
                        Coordinate relativePosition = new Coordinate();
                        // Calculate target
                        if (submarinesRelativePositions.containsKey(submarine.getId())) {
                            relativePosition = submarinesRelativePositions.get(submarine.getId());
                        }
                        Coordinate flagshipCoordinate = submarines.get(0).getPosition();
                        //Rotate targetCoordinate with the angle between FLAGSHIP and target
                        double rotation = Angle.angle(flagshipCoordinate, target) - Math.PI / 2; // Don't know why PI/2 needs to subtracted but it works which is nice
                        Coordinate originalTargetCoordinate = new Coordinate(flagshipCoordinate.x + relativePosition.x, flagshipCoordinate.y + relativePosition.y);

                        // This is some wierd magic I did last night
                        Vector2D rotatedTarget = new Vector2D(flagshipCoordinate, originalTargetCoordinate).rotate(rotation).add(new Vector2D(flagshipCoordinate));

                        Coordinate targetCoordinate = new Coordinate(rotatedTarget.getX(), rotatedTarget.getY());
                        MoveModification asd = NavigationComputer.getMoveModification(submarine.getPosition(), targetCoordinate, submarine.getVelocity(), submarine.getAngle());
                        System.out.println("QWER " + asd);
                        moveModifications.put(submarine.getId(), asd);
                    }
                } else if (submarine.getVelocity() > 0) {
                    moveModifications.put(submarine.getId(), NavigationComputer.getSlowerMoveModification());
                }
            }
        }
        return moveModifications;
    }

    private MoveModification calculateEvadingModification(Entity torpedo, Submarine subinDanger) {
        Integer currentSpeed = getFlagship().getVelocity();
        Integer currentAngle = subinDanger.getAngle();
        Integer maxSpeed = mapConfiguration.getMaxSpeed();
        
        Integer modificationAngle;
        Integer modificationSpeed;
        
        Integer maxModSpeed = mapConfiguration.getMaxAccelerationPerRound();
        Integer maxModAngle = mapConfiguration.getMaxSteeringPerRound();
        
        Integer torpedoAngle = torpedo.getAngle();
        Integer torpedoSpeed = torpedo.getVelocity();
        
        Integer bestAngleMod = toParellel(torpedoAngle, currentAngle);
        
        // we are faster than half we should slow down
        if (currentSpeed > (maxSpeed / 2)) {
            modificationSpeed = -maxModSpeed;
        } else {
            modificationSpeed = maxModSpeed;
        }
        
        
        
        // TODO what else?
        modificationAngle = maxModAngle;
        
        MoveModification evadingModification = new MoveModification(modificationSpeed, modificationAngle);
        
        // TODO leads to outofmap?
        
        return evadingModification;
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
    private boolean hasReachedTarget(Coordinate target) {
        if (target == null) {
            return true;
        }

//        for (Submarine sub : submarines) {
//            Coordinate relPos = submarinesRelativePositions.get(sub.getId());
//            Coordinate targetPos = new Coordinate(target.x + relPos.x, target.y + relPos.y);
//            if(sub.getPosition().distance(targetPos) > TARGET_REACHING_THRESHOLD) return false;
//        }
        if (getFlagshipPosition().distance(target) > TARGET_REACHING_THRESHOLD) return false;
        return true;
    }

    private Coordinate getNextTarget() {
//        if (intermediateTarget != null) {
//            return intermediateTarget;
//        }

//        if (isInDangerZone()) {
//            Submarine inDanger = submarines.get(0);
//            for (Submarine s : submarines) {
//                if (NavigationComputer.isInDangerZone(s.getPosition())) {
//                    inDanger = s;
//                    break;
//                }
//            }
//            intermediateTarget = NavigationComputer.getIntermediateTarget(inDanger.getPosition(), target);
//            return intermediateTarget;
//        } else {
            // TODO: navigationComputer.getNextTarget()
            return target; //!= null ? target : new Coordinate(1,1);
//        }
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
        coordinates.add(new Coordinate(-100, 100));
        coordinates.add(new Coordinate(100, 100));
        
        
        // Low hp goes back
        /*subs.sort(new Comparator<Submarine>() {
            @Override
            public int compare(Submarine o1, Submarine o2) {
                return -o1.getHp().compareTo(o2.getHp());
            }
        });*/

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

    public Submarine getFlagship() {
        return submarines.get(0);
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

    public List<Submarine> getSubmarines() {
        return submarines;
    }

    public void setMapConfiguration(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
    }

    private Integer toParellel(Integer torpedoAngle, Integer currentAngle) {
        Integer difference1 = torpedoAngle - currentAngle;
        Integer difference2 = (360 - torpedoAngle) - currentAngle;
                
        if (difference1 < difference2) {
            // torpedoAngle-hez húzunk
            return pullToAngle(torpedoAngle, currentAngle);            
        } else {
            // torpedoAngle ellentétéhez húzunk            
            return pullToAngle(360 - torpedoAngle, currentAngle);            
        }
    }

    private Integer pullToAngle(Integer targetAngle, Integer currentAngle) {
        Integer maxAngle = mapConfiguration.getMaxSteeringPerRound();
        Integer diff = (targetAngle - currentAngle);
        if (Math.abs(diff) < maxAngle) {
            return diff;
        } else {
            if (diff > 0) {
                return maxAngle;
            } else {
                return -maxAngle;
            }
        }        
    }
}
