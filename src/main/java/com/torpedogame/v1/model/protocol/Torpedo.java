/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torpedogame.v1.model.protocol;

import com.vividsolutions.jts.geom.Coordinate;

/**
 *
 * @author kkrisz
 */
public class Torpedo extends Entity {    
    private Integer roundsMoved;    

    public Torpedo(Integer roundsMoved, String type, Integer id, Coordinate position, Owner owner, Integer velocity, Integer angle, Integer hp) {
        super(type, id, position, owner, velocity, angle, hp);
        this.roundsMoved = roundsMoved;
    }

    public Torpedo() {
    }

    public Integer getRoundsMoved() {
        return roundsMoved;
    }

    public void setRoundsMoved(Integer roundsMoved) {
        this.roundsMoved = roundsMoved;
    }
}
