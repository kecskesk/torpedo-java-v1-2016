package com.torpedogame.v1.gui;

import java.util.List;

/**
 * Created by Rob on 2016.11.16..
 */
public class GuiMoveRequest {
    private List<Integer> submarineIds;
    private double x;
    private double y;

    public GuiMoveRequest() {
    }

    public GuiMoveRequest(List<Integer> submarineIds, double x, double y) {
        this.submarineIds = submarineIds;
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

    public void setSubmarineIds(List<Integer> submarineIds) {
        this.submarineIds = submarineIds;
    }

    public List<Integer> getSubmarineIds() {
        return submarineIds;
    }
}