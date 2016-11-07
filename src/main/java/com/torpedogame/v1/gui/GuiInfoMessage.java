package com.torpedogame.v1.gui;

import com.torpedogame.v1.model.protocol.Entity;
import com.torpedogame.v1.model.protocol.GameInfoResponse.Game;
import com.torpedogame.v1.model.protocol.Submarine;
import com.vividsolutions.jts.geom.Coordinate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kkrisz
 */
public class GuiInfoMessage {
    private Game game;
    private List<Entity> entities;
    private List<Submarine> submarines;
    private Map<Integer, Coordinate> targetStore;

    public GuiInfoMessage() {
    }

    public List<Submarine> getSubmarines() {
        return submarines;
    }

    public void setSubmarines(List<Submarine> submarines) {
        this.submarines = submarines;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public Map<Integer, Coordinate> getTargetStore() {
        return targetStore;
    }

    public void setTargetStore(Map<Integer, Coordinate> targetStore) {
        this.targetStore = targetStore;
    }
}
