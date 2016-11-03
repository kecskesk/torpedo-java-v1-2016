package com.torpedogame.v1.model.protocol;

import com.vividsolutions.jts.geom.Coordinate;

/**
 *
 * @author kkrisz
 */
public class Submarine extends Entity{
    private Integer sonarCooldown;
    private Integer torpedoCooldown;
    private Integer sonarExtended;

    public Submarine(Integer sonarCooldown, Integer torpedoCooldown, Integer sonarExtended, String type, Integer id, Coordinate position, Owner owner, Integer velocity, Integer angle, Integer hp) {
        super(type, id, position, owner, velocity, angle, hp);
        this.sonarCooldown = sonarCooldown;
        this.torpedoCooldown = torpedoCooldown;
        this.sonarExtended = sonarExtended;
    }

    public Submarine() {
        super();
    }

    public Integer getSonarCooldown() {
        return sonarCooldown;
    }

    public void setSonarCooldown(Integer sonarCooldown) {
        this.sonarCooldown = sonarCooldown;
    }

    public Integer getTorpedoCooldown() {
        return torpedoCooldown;
    }

    public void setTorpedoCooldown(Integer torpedoCooldown) {
        this.torpedoCooldown = torpedoCooldown;
    }

    public Integer getSonarExtended() {
        return sonarExtended;
    }

    public void setSonarExtended(Integer sonarExtended) {
        this.sonarExtended = sonarExtended;
    }
}
