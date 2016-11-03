package com.torpedogame.v1;

import com.torpedogame.v1.service.GameAPI;
import com.torpedogame.v1.service.GameApiImpl;

import com.torpedogame.v1.model.protocol.CreateGameResponse;
import com.torpedogame.v1.model.protocol.GameInfoResponse;
import com.torpedogame.v1.model.protocol.JoinGameResponse;
import com.torpedogame.v1.service.GameAPI;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 *
 */
public class App extends TimerTask
{
    private static GameAPI gameEngine;

    public static void main( String[] args )
    {
        System.out.println( "The torpedo program is starting..." );

        // Create a game
        CreateGameResponse currentGame = gameEngine.createGame();
        System.out.println(currentGame.getMessage());

        // Join to the created game
        JoinGameResponse joinGameResponse = gameEngine.joinGame(currentGame.getId());
        System.out.println(joinGameResponse.getMessage());

        // Get game-info
        GameInfoResponse gameInfoResponse = gameEngine.gameInfo(currentGame.getId());

        // Start executing the steps for each round
        // This will start the execution immediately, if a little delay is needed,
        // increase the second parameter of the schedule function.
        Timer timer = new Timer();
        timer.schedule(new App(), 0, gameInfoResponse.getMapConfiguration().getRoundLength());

        GameAPI gameAPI = new GameApiImpl();
        System.out.print(gameAPI.createGame().toString());
    }

    /**
     * This function will be called periodically.
     */
    @Override
    public void run() {
        // TODO implement
        System.out.println("Starting round {insert round number here}");
    }
}
