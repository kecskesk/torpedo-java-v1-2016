package com.torpedogame.v1.model.protocol;

import java.util.List;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class GameListResponse {
    private List<Integer> games;
    private String message;
    private int code;

    public GameListResponse(int code, List<Integer> games, String message) {
        this.code = code;
        this.games = games;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Integer> getGames() {
        return games;
    }

    public void setGames(List<Integer> games) {
        this.games = games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
