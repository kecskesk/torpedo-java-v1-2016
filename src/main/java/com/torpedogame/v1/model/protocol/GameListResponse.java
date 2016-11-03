package com.torpedogame.v1.model.protocol;

import java.util.List;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class GameListResponse extends Response {

    private List<Integer> games;
    
    public GameListResponse(){
        super();
    }

    public GameListResponse(String message, int code) {
        super(message, code);
    }


    public List<Integer> getGames() {
        return games;
    }

    public void setGames(List<Integer> games) {
        this.games = games;
    }

    @Override
    public String toString(){
        return getCode() + " " + getMessage();
    }
}
