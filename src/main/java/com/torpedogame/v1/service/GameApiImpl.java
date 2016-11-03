package com.torpedogame.v1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.torpedogame.v1.model.protocol.*;

public class GameApiImpl implements GameAPI {
    private static final String SERVER_URL = "http://195.228.45.100:8080/jc16-srv/";
    private static final String TOKEN_HEADER_NAME = "TEAMTOKEN";
    private static final String TOKEN_HEADER_VALUE = "355CCC4899499A19FB06D319744CB785";

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public SubmarinesResponse submarines(int gameId) {
        String response = HttpRequest.get(SERVER_URL + "game/" + gameId + "/submarine")
                .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        ObjectMapper mapper = new ObjectMapper();
        SubmarinesResponse submarines = null;
        try {
            submarines = mapper.readValue(response, SubmarinesResponse.class);
        } catch (Exception e) {
            System.out.println("Error retrieving submarine list for game: " + gameId + ", Exception: " + e.getMessage());
        }

        return submarines;
    }

    @Override
    public MoveResponse move(int gameId, int submarineId, double speed, double turn) {
        MoveRequest move = new MoveRequest(speed, turn);
        ObjectMapper mapper = new ObjectMapper();
        String jsonToSend = "";
        try {
            jsonToSend = mapper.writeValueAsString(move);
        } catch (Exception e){
            System.out.println("Error creating move request for game: " + gameId + " submarine: " + submarineId);
        }

        if ("".equals(jsonToSend)) {
            return null;
        }

        String response = HttpRequest.post(SERVER_URL + "game/" + gameId + "/submarine/" + submarineId + "/move")
                         .send(jsonToSend)
                         .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        MoveResponse moveResponse = null;
        try {
            moveResponse = mapper.readValue(response, MoveResponse.class);
        } catch (Exception e) {
            System.out.println("Error retrieving move response for game: " + gameId + ", Exception: " + e.getMessage());
        }

        return moveResponse;
    }

    @Override
    public ShootResponse shoot(int gameId, int submarineId, double angle) {
        ShootRequest shoot = new ShootRequest(angle);
        ObjectMapper mapper = new ObjectMapper();
        String jsonToSend = "";
        try {
            jsonToSend = mapper.writeValueAsString(shoot);
        } catch (Exception e){
            System.out.println("Error creating shoot request for game: " + gameId + " submarine: " + submarineId + ", Exception: " + e.getMessage());
        }

        if ("".equals(jsonToSend)) {
            return null;
        }

        String response = HttpRequest.post(SERVER_URL + "game/" + gameId + "/submarine/" + submarineId + "/shoot")
                         .send(jsonToSend)
                         .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        ShootResponse shootResponse = null;
        try {
            shootResponse = mapper.readValue(response, ShootResponse.class);
        } catch (Exception e) {
            System.out.println("Error retrieving shoot response for game: " + gameId + ", Exception: " + e.getMessage());
        }

        return shootResponse;
    }

    @Override
    public SonarResponse sonar(int gameId, int submarineId) {
        String response = HttpRequest.get(SERVER_URL + "game/" + gameId + "/submarine/" + submarineId + "/sonar")
                .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        ObjectMapper mapper = new ObjectMapper();
        SonarResponse sonar = null;
        try {
            sonar = mapper.readValue(response, SonarResponse.class);
        } catch (Exception e) {
            System.out.println("Error retrieving sonar for submarine: " + submarineId + ", Exception: " + e.getMessage());
        }

        return sonar;
    }

    @Override
    public ExtendSonarResponse extendSonar(int gameId, int submarineId) {
        String response = HttpRequest.post(SERVER_URL + "game/" + gameId + "/submarine/" + submarineId + "/sonar")
                .header(TOKEN_HEADER_NAME, TOKEN_HEADER_VALUE).body();

        ObjectMapper mapper = new ObjectMapper();
        ExtendSonarResponse extendSonar = null;
        try {
            extendSonar = mapper.readValue(response, ExtendSonarResponse.class);
        } catch (Exception e) {
            System.out.println("Error retrieving extend sonar for submarine: " + submarineId + ", Exception: " + e.getMessage());
        }

        return extendSonar;
    }
}
