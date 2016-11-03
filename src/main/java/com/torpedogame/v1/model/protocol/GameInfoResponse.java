package com.torpedogame.v1.model.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.torpedogame.v1.model.game_control.MapConfiguration;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class GameInfoResponse {
    private Game game;
    private String message;
    private int code;

    public GameInfoResponse(){}

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public class Game {
        public Game(){}

        private int round;
        @JsonIgnore
        private String scores;
        private int id;
        @JsonIgnore
        private String connectionStatus;
        private MapConfiguration mapConfiguration;
        private String status;
        private String createdTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRound() {
            return round;
        }

        public void setRound(int round) {
            this.round = round;
        }

        public String getScores() {
            return scores;
        }

        public void setScores(String scores) {
            this.scores = scores;
        }

        public String getConnectionStatus() {
            return connectionStatus;
        }

        public void setConnectionStatus(String connectionStatus) {
            this.connectionStatus = connectionStatus;
        }

        public MapConfiguration getMapConfiguration() {
            return mapConfiguration;
        }

        public void setMapConfiguration(MapConfiguration mapConfiguration) {
            this.mapConfiguration = mapConfiguration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }
    }
}