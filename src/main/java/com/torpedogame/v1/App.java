package com.torpedogame.v1;

import com.torpedogame.v1.gui.GuiInfoMessage;
import com.torpedogame.v1.gui.SparkServer;
import com.torpedogame.v1.model.protocol.*;
import com.torpedogame.v1.model.utility.MoveModification;
import com.torpedogame.v1.service.GameAPI;
import com.torpedogame.v1.service.GameApiImpl;

import com.torpedogame.v1.utility.NavigationComputer;
import com.torpedogame.v1.utility.ShootingComputer;
import com.torpedogame.v1.utility.TargetComputer;
import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The main entry point.
 */
public class App extends TimerTask
{
    private static GameAPI gameEngine;
    private static CreateGameResponse createdGame = null;
    private static JoinGameResponse joinedGame = null;
    private static GameInfoResponse gameInfoResponse = null;
    private Map<Integer, Coordinate> targetStore;
    private static GuiInfoMessage guiInfoMessage = new GuiInfoMessage();
    private static final SparkServer sparkServer = new SparkServer();

    public static void main( String[] args )
    {
        sparkServer.start();
        
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
        
        // Load map into NavComp
        NavigationComputer.setHeight(gameInfoResponse.getGame().getMapConfiguration().getHeight());
        NavigationComputer.setWidth(gameInfoResponse.getGame().getMapConfiguration().getWidth());
        NavigationComputer.setIslandPositions(gameInfoResponse.getGame().getMapConfiguration().getIslandPositions());
        NavigationComputer.setIslandSize(gameInfoResponse.getGame().getMapConfiguration().getIslandSize());

        ShootingComputer.setTorpedoRange(gameInfoResponse.getGame().getMapConfiguration().getTorpedoRange());
        ShootingComputer.setTorpedoSpeed(gameInfoResponse.getGame().getMapConfiguration().getTorpedoSpeed());

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
        List<Entity> guiEntities = new ArrayList<>();
        
        // update game info
        gameInfoResponse = gameEngine.gameInfo(createdGame.getId());
        System.out.println("####################  ROUND " + gameInfoResponse.getGame().getRound() + "  ####################");
        System.out.println("SCORE : " + gameInfoResponse.getGame().getScores().getScores());
        SubmarinesResponse submarinesResponse = gameEngine.submarines(createdGame.getId());
        List<Submarine> submarineList = submarinesResponse.getSubmarines();
        if (submarineList == null || submarineList.isEmpty()) {
            System.out.println("Submarine list is empty!");
            return;
        }
        
        // initialize target store
        if (targetStore == null) {
            targetStore = new HashMap<>(submarineList.size());
        }

        for (Submarine submarine : submarineList) {
            System.out.println("SHIP " + submarine.getId());
            System.out.println("position: " + submarine.getPosition());
            System.out.println("angle: " + submarine.getAngle());
            System.out.println("speed: " + submarine.getVelocity());

            SonarResponse sonarResponse = gameEngine.sonar(createdGame.getId(), submarine.getId());
            List<Entity> entityList = sonarResponse.getEntities();

            System.out.println("visible entities:");
            if (entityList == null) {
                System.out.println("Entity list is null, continue with next submarine.");
                continue;
            }
            
            guiEntities.addAll(entityList);

            for (Entity e : entityList) {
                System.out.println("\t" + e.getType() + " " + e.getId());
                System.out.println("\t\towner: " + e.getOwner());
                System.out.println("\t\tposition: " + e.getPosition());
                System.out.println("\t\tangle: " + e.getAngle());
                System.out.println("\t\tvelocity: " + e.getVelocity());
                if(e.getOwner().getName().equals("BOT")) { // && IT IS A SHIP!
                    // Red Alert
                    // TODO Check for torpedo cooldown!
                    try {
                        double shootingAngle = ShootingComputer.getShootingAngle(submarine.getPosition(), e.getPosition(), e.getVelocity(), e.getAngle());
                        System.out.println("- Firing!");
                        gameEngine.shoot(createdGame.getId(), submarine.getId(), shootingAngle);
                    } catch (Exception ise) {
                        System.out.println("- Can not lock on target!" );
                        ise.printStackTrace();
                    }

                    // Intercept course
                    targetStore.put(submarine.getId(), e.getPosition());
                }
            }

            // Try to get previous target from target store.
            Coordinate target = null;
            if (targetStore.containsKey(submarine.getId())) {
                target = targetStore.get(submarine.getId());
            }

            TargetComputer.setMapConfiguration(gameInfoResponse.getGame().getMapConfiguration());
            boolean shouldBeOnLeftSide = (submarine.getId() % 2 == 0);
            target = TargetComputer.getNextTarget(submarine.getPosition(), target, shouldBeOnLeftSide);
            targetStore.put(submarine.getId(), target);

            System.out.println("\t\tcurrent target: " + target.toString());

            // Calculate move modification value and move the submarine
            MoveModification moveModification = NavigationComputer.getMoveModification(submarine.getPosition(), target, submarine.getVelocity(), submarine.getAngle());
            gameEngine.move(createdGame.getId(), submarine.getId(), moveModification.getSpeed(), moveModification.getTurn());

            System.out.println("---------------------------------------------------------------------------------");
            
            guiInfoMessage.setEntities(guiEntities);
            guiInfoMessage.setSubmarines(submarineList);
            guiInfoMessage.setGame(gameInfoResponse.getGame());
            guiInfoMessage.setTargetStore(targetStore);
            sparkServer.updateMessage(guiInfoMessage);
        }
    }
}
