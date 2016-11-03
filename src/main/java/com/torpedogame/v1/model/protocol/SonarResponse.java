package com.torpedogame.v1.model.protocol;

import java.util.List;

/**
 *
 * @author kkrisz
 */
public class SonarResponse extends Response {
    private List<Entity> entities;    

    public SonarResponse(List<Entity> entities, String message, int code) {
        super(message, code);
        this.entities = entities;
    }

    public SonarResponse() {
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}
