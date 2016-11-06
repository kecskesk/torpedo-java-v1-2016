package com.torpedogame.v1.model.protocol;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Torpedo or a submarine
 * 
 * @author kkrisz
 */
public class Entity {
    protected String type;
    protected Integer id;
    protected Coordinate position;
    protected Owner owner;
    protected Integer velocity;
    protected Integer angle;
    protected Integer hp;
    protected Integer roundsMoved;


    public Entity() {
    }

    public Integer getRoundsMoved() {
        return roundsMoved;
    }

    public void setRoundsMoved(Integer roundsMoved) {
        this.roundsMoved = roundsMoved;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Integer getVelocity() {
        return velocity;
    }

    public void setVelocity(Integer velocity) {
        this.velocity = velocity;
    }

    public Integer getAngle() {
        return angle;
    }

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }
}
