package com.torpedogame.v1.model.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.torpedogame.v1.model.game_control.MapConfiguration;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class GameInfoResponse extends Response {
    private Game game;

    public GameInfoResponse(){
        super();
    }

    public GameInfoResponse(Game game, String message, int code) {
        super(message, code);
        this.game = game;
    }
    
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public class Game {
        public Game(){}

        private int round;
        private Scores scores;
        private int id;
        private ConnectionStatus connectionStatus;
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

        public Scores getScores() {
            return scores;
        }

        public void setScores(Scores scores) {
            this.scores = scores;
        }

        public ConnectionStatus getConnectionStatus() {
            return connectionStatus;
        }

        public void setConnectionStatus(ConnectionStatus connectionStatus) {
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

        @Override
        public String toString() {
            return "Game{" + "round=" + round + ", scores=" + scores + ", id=" + id + ", connectionStatus=" + connectionStatus + ", mapConfiguration=" + mapConfiguration + ", status=" + status + ", createdTime=" + createdTime + '}';
        }
    }
}