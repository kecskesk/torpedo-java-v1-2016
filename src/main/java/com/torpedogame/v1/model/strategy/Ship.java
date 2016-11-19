package com.torpedogame.v1.model.strategy;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Created by Dombi Soma on 19/11/2016.
 */
public class Ship {
    Integer id;
    Double velocity;
    Double angle;
    Coordinate position;

    public Ship(Integer id, Double velocity, Double angle, Coordinate position) {
        this.angle = angle;
        this.id = id;
        this.position = position;
        this.velocity = velocity;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
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

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        return id != null ? id.equals(ship.id) : ship.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
