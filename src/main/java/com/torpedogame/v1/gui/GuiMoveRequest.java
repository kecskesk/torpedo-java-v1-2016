package com.torpedogame.v1.gui;

/**
 * Created by Rob on 2016.11.16..
 */
public class GuiMoveRequest {
    private int submarineId;
    private double x;
    private double y;

    public GuiMoveRequest(int submarineId, double x, double y) {
        this.submarineId = submarineId;
        this.x = x;
        this.y = y;
    }

    public int getSubmarineId() {
        return submarineId;
    }

    public void setSubmarineId(int submarineId) {
        this.submarineId = submarineId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
