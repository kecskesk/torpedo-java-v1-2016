package com.torpedogame.v1.model.protocol;

import com.torpedogame.v1.model.game_control.MapConfiguration;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class GameInfoResponse {
    private int id;
    private String message;
    private MapConfiguration mapConfiguration;
    private int code;

    public GameInfoResponse(int code, int id, MapConfiguration mapConfiguration, String message) {
        this.code = code;
        this.id = id;
        this.mapConfiguration = mapConfiguration;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MapConfiguration getMapConfiguration() {
        return mapConfiguration;
    }

    public void setMapConfiguration(MapConfiguration mapConfiguration) {
        this.mapConfiguration = mapConfiguration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
