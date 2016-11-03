package com.torpedogame.v1.model.protocol;

/**
 * Created by Rob on 2016.11.03..
 */
public class MoveRequest {
    private double speed;
    private double turn;

    public MoveRequest(double speed, double turn){
        this.speed = speed;
        this.turn = turn;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getTurn() {
        return turn;
    }

    public void setTurn(double turn) {
        this.turn = turn;
    }
}
