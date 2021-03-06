package com.torpedogame.v1.service;

import com.torpedogame.v1.model.protocol.*;

/**
 * This interface describes what methods can be called on the GameEngine.
 * Created by Dombi Soma on 03/11/2016.
 */
public interface GameAPI {

    CreateGameResponse createGame();

    GameListResponse gameList();

    JoinGameResponse joinGame(int gameId);

    GameInfoResponse gameInfo(int gameId);

    SubmarinesResponse submarines(int gameId);

    MoveResponse move(int gameId, int submarineId, double speed, double turn);

    ShootResponse shoot(int gameId, int submarineId, double angle);

    SonarResponse sonar(int gameId, int submarineId);

    ExtendSonarResponse extendSonar(int gameId, int submarineId);

}
