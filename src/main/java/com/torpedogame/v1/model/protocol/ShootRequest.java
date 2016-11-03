package com.torpedogame.v1.model.protocol;

/**
 * @author kecskesk
 */
public class ShootRequest {
    private double angle;

    public ShootRequest(double angle){
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
