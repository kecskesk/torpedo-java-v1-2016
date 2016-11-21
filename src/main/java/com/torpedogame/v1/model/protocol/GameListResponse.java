package com.torpedogame.v1.model.protocol;

import java.util.List;
import java.util.stream.Collectors;

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
        String response = getCode() + " " + getMessage();
        List<CharSequence> gamecodes = games.stream()
                .map(game -> game.toString()).collect(Collectors.toList());
        String gamesString = "" + String.join(", ", gamecodes);
        if (games.isEmpty()) {
            gamesString = "None.";
        }
        return "Our yet not connected games: " + response + " " + gamesString;
    }
}
