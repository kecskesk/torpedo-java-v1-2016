package com.torpedogame.v1.model.protocol;

/**
 *
 * @author kkrisz
 */
public class Owner {
    private String name;

    public Owner() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Owner{" + "name=" + name + '}';
    }
}
