package com.torpedogame.v1.model.utility;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class MoveModification {
    private double speed;
    private double turn;

    public MoveModification(double speed, double turn) {
        this.speed = speed;
        this.turn = turn;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getTurn() {
        return turn;
    }

    public void setTurn(double turn) {
        this.turn = turn;
    }
}
