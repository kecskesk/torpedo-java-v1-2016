package com.torpedogame.v1;

import com.torpedogame.v1.service.GameAPI;
import com.torpedogame.v1.service.GameApiImpl;

import com.torpedogame.v1.model.protocol.CreateGameResponse;
import com.torpedogame.v1.model.protocol.GameInfoResponse;
import com.torpedogame.v1.model.protocol.GameListResponse;
import com.torpedogame.v1.model.protocol.JoinGameResponse;
import com.torpedogame.v1.service.GameAPI;
import com.torpedogame.v1.utility.NavigationComputer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 *
 */
public class App extends TimerTask
{
    private static GameAPI gameEngine;
    private static CreateGameResponse createdGame = null;
    private static JoinGameResponse joinedGame = null;
    private static GameInfoResponse gameInfoResponse = null;

    public static void main( String[] args )
    {
        gameEngine = new GameApiImpl();
        System.out.println( "The torpedo program is starting..." );

        // Check games
        GameListResponse gameList = gameEngine.gameList();
        System.out.println(gameList.toString());
        
        // Create a game
        createdGame = gameEngine.createGame();
        System.out.println(createdGame.toString());

        // Join to the created game
        joinedGame = gameEngine.joinGame(createdGame.getId());
        System.out.println(joinedGame.toString());

        // Get game-info
        gameInfoResponse = gameEngine.gameInfo(createdGame.getId());

        // Setup configuration
        NavigationComputer.setMaxSteeringPerRound(gameInfoResponse.getGame().getMapConfiguration().getMaxSteeringPerRound());
        NavigationComputer.setMaxAccelerationPerRound(gameInfoResponse.getGame().getMapConfiguration().getMaxAccelerationPerRound());
        NavigationComputer.setMaxSpeed(gameInfoResponse.getGame().getMapConfiguration().getMaxSpeed());
        // MinSpeed is not originated from the GameInfo, 0 was measured by hand.
        // By increasing this, it may be used to avoid slowing down in high(e.g. 180) degree turns.
        NavigationComputer.setMinSpeed(0.0);

        // Start executing the steps for each round
        // This will start the execution immediately, if a little delay is needed,
        // increase the second parameter of the schedule function.
        Timer timer = new Timer();
        timer.schedule(new App(), 0, gameInfoResponse.getGame().getMapConfiguration().getRoundLength());
    }

    /**
     * This function will be called periodically.
     */
    @Override
    public void run() {
        // TODO implement
        System.out.println("Starting round {insert round number here}");
        
        // update game info
        gameInfoResponse = gameEngine.gameInfo(createdGame.getId());
        System.out.println(gameInfoResponse.getGame().toString());
        
    }
}
