package com.torpedogame.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.torpedogame.v1.model.protocol.*;

public class GameApiImpl implements GameAPI {
    private static final String SERVER_URL = "http://195.228.45.100:8080/jc16-srv/";
    private static final String TOKEN_HEADER_NAME = "TEAMTOKEN";
    private static final String TOKEN_HEADER_VALUE = "355CCC4899499A19FB06D319744CB785";

    public CreateGameResponse createGame() {
        String response = HttpRequest.post(SERVER_URL + "game")
                                .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        ObjectMapper mapper = new ObjectMapper();
        CreateGameResponse createGame = null;
        try {
            createGame = mapper.readValue(response, CreateGameResponse.class);
        } catch (Exception e){
            System.out.println("Error creating game: " + e.getMessage());
        }

        return createGame;
    }

    public GameListResponse gameList() {
        String response = HttpRequest.get(SERVER_URL + "game")
                .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        ObjectMapper mapper = new ObjectMapper();
        GameListResponse gameList = null;
        try {
            gameList = mapper.readValue(response, GameListResponse.class);
        } catch (Exception e) {
            System.out.println("Error retrieving game list: " + e.getMessage());
        }

        return gameList;
    }

    public JoinGameResponse joinGame(int gameId) {
        String response = HttpRequest.post(SERVER_URL + "game/" + gameId)
                .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        ObjectMapper mapper = new ObjectMapper();
        JoinGameResponse joinGame = null;
        try {
            joinGame = mapper.readValue(response, JoinGameResponse.class);
        } catch (Exception e) {
            System.out.println("Error joining game " + gameId + ", Exception: " + e.getMessage());
        }

        return joinGame;
    }

    public GameInfoResponse gameInfo(int gameId) {
        String response = HttpRequest.get(SERVER_URL + "game/" + gameId)
                .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        ObjectMapper mapper = new ObjectMapper();
        GameInfoResponse gameInfo = null;
        try {
            gameInfo = mapper.readValue(response, GameInfoResponse.class);
        } catch (Exception e) {
            System.out.println("Error retrieving game info for game: " + gameId + ", Exception: " + e.getMessage());
        }

        return gameInfo;
    }

    public void submarines() {

    }

    public MoveResponse move(int gameId, int submarineId, double speed, double turn) {
        return null;
    }

    public ShootResponse shoot(int gameId, int submarineId, double angle) {
        return null;
    }

    public void sonar() {

    }

    public ExtendSonarResponse extendSonar(int gameId, int submarineId) {
        return null;
    }
}
