package com.torpedogame.v1.model.protocol;

/**
 * Created by Rob on 2016.11.03..
 */
public class MoveRequest {

    public MoveRequest(double speed, double turn){
        this.speed = speed;
        this.turn = turn;
    }

    private double speed;
    private double turn;
}
