package com.torpedogame.v1.gui;

import java.util.List;

/**
 * Created by Rob on 2016.11.16..
 */
public class GuiMoveRequest {
    private double x;
    private double y;

    public GuiMoveRequest() {
    }

    public GuiMoveRequest(double x, double y) {
        this.x = x;
        this.y = y;
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