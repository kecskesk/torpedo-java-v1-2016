package com.torpedogame.v1;

import com.torpedogame.v1.gui.GuiInfoMessage;
import com.torpedogame.v1.gui.GuiMoveRequest;
import com.torpedogame.v1.gui.SparkServer;
import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.torpedogame.v1.model.protocol.*;
import com.torpedogame.v1.model.strategy.Fleet;
import com.torpedogame.v1.model.utility.MoveModification;
import com.torpedogame.v1.service.GameAPI;
import com.torpedogame.v1.service.GameApiImpl;

import com.torpedogame.v1.utility.NavigationComputer;
import com.torpedogame.v1.utility.ShootingComputer;
import com.vividsolutions.jts.geom.Coordinate;

import java.io.InputStream;
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
    //TODO Add ability to connect to ongoin game
    //TODO unexpected exceptions prevents us from continuing a game
    private static GameAPI gameEngine;
    private static CreateGameResponse createdGame = null;
    private static JoinGameResponse joinedGame = null;
    private static GameInfoResponse gameInfoResponse = null;
    // TODO maybe create a class for these stores and
    // initialize them in the main function
    private static GuiInfoMessage guiInfoMessage = new GuiInfoMessage();
    private static final SparkServer sparkServer = new SparkServer();
    private static int selectedGameId = -1;

    private static Fleet fleet = new Fleet();

    public static void main( String[] args )
    {
        sparkServer.start();
        
        gameEngine = new GameApiImpl();

        // SETTING GAME ENGINE URL FROM ARGUMENT LIST
        GameApiImpl.setServerUrl(args[0]);

        System.out.println( "The torpedo program is starting..." );
        
        // Create a game
        createdGame = gameEngine.createGame();
        System.out.println(createdGame.toString());

        // Check games
        GameListResponse gameList = gameEngine.gameList();
        System.out.println(gameList.toString());

        List<Integer> createdGameIds = gameList.getGames();
        if (createdGameIds != null && createdGameIds.size() > 0) {
            selectedGameId = createdGameIds.get(0);
        }

        if(selectedGameId > -1) {
            // Join to the created game
            joinedGame = gameEngine.joinGame(selectedGameId);
            System.out.println(joinedGame.toString());

            // Get game-info
            gameInfoResponse = gameEngine.gameInfo(selectedGameId);
            MapConfiguration mapConfiguration = gameInfoResponse.getGame().getMapConfiguration();

            // Setup configuration
            NavigationComputer.setMaxSteeringPerRound(mapConfiguration.getMaxSteeringPerRound());
            NavigationComputer.setMaxAccelerationPerRound(mapConfiguration.getMaxAccelerationPerRound());
            NavigationComputer.setMaxSpeed(mapConfiguration.getMaxSpeed());
            // MinSpeed is not originated from the GameInfo, 0 was measured by hand.
            // By increasing this, it may be used to avoid slowing down in high (e.g. 180) degree turns.
            NavigationComputer.setMinSpeed(0);//(mapConfiguration.getMaxAccelerationPerRound()); //(2 * mapConfiguration.getMaxAccelerationPerRound());

            // Load map into NavComp
            NavigationComputer.setHeight(mapConfiguration.getHeight());
            NavigationComputer.setWidth(mapConfiguration.getWidth());
            NavigationComputer.setIslandPositions(mapConfiguration.getIslandPositions());
            NavigationComputer.setIslandSize(mapConfiguration.getIslandSize());
            NavigationComputer.setSonarRange(mapConfiguration.getSonarRange());
            NavigationComputer.setDangerZoneThreshold(100);

            ShootingComputer.setTorpedoRange(mapConfiguration.getTorpedoRange());
            ShootingComputer.setTorpedoSpeed(mapConfiguration.getTorpedoSpeed());
            ShootingComputer.setTorpedoExplosionRadius(mapConfiguration.getTorpedoExplosionRadius());
            // Start executing the steps for each round
            // This will start the execution immediately, if a little delay is needed,
            // increase the second parameter of the schedule function.
            Timer timer = new Timer();
            timer.schedule(new App(), 0, mapConfiguration.getRoundLength());
        }
    }

    /**
     * This function will be called periodically.
     */
    @Override
    public void run() {
        // Get the current game informations
        gameInfoResponse = gameEngine.gameInfo(selectedGameId);
        GameInfoResponse.Game currentGame = gameInfoResponse.getGame();
        MapConfiguration mapConfiguration = currentGame.getMapConfiguration();

        System.out.println("####################  ROUND " + currentGame.getRound() + "  ####################");
        System.out.println("SCORE : " + currentGame.getScores().getScores());

        // Query the submarines
        SubmarinesResponse submarinesResponse = gameEngine.submarines(selectedGameId);
        List<Submarine> submarineList = submarinesResponse.getSubmarines();
        if (submarineList == null || submarineList.isEmpty()) {
            System.out.println("Submarine list is empty!");
            return;
        }
        fleet.setSubmarines(submarineList);

        // Set new target if needed
        GuiMoveRequest moveRequest = sparkServer.getGuiMoveRequest();
        // first check if gui move request is sent.
        if (moveRequest != null) {
            fleet.setTarget(new Coordinate(moveRequest.getX(), moveRequest.getY()));
            sparkServer.clearMoveRequest();
        }

        if(!fleet.hasTarget()) {
            fleet.setTarget(new Coordinate(900, 400));
        }

        if (fleet.hasReachedTarget()) {
            fleet.setTarget(null);
        }



        // Gather sonar information for fleet
        List<Entity> visibleEntities = new ArrayList<>();
        for (Submarine submarine : submarineList) {
            // TODO Extend sonars somehow for fleet
//            if (currentGame.getRound() != 0 && (currentGame.getRound() % mapConfiguration.getExtendedSonarCooldown() == 0)) {
//                System.out.println("Sonar extended!");
//                gameEngine.extendSonar(selectedGameId, submarine.getId());
//            }
            SonarResponse sonarResponse = gameEngine.sonar(selectedGameId, submarine.getId());
            List<Entity> entityList = sonarResponse.getEntities();
            if (entityList == null) {
                System.out.println("Entity list is null, continue with next submarine.");
            } else {
                visibleEntities.addAll(entityList);
            }
        }
        fleet.setVisibleEntities(visibleEntities);

        // Move the fleet
        if (fleet.isInDangerZone()) {

        }
        Map<Integer, MoveModification> moveModifications = fleet.getMoveModifications();
        for (Integer shipId : moveModifications.keySet()) {
            MoveModification moveModification = moveModifications.get(shipId);
            gameEngine.move(selectedGameId, shipId, moveModification.getSpeed(), moveModification.getTurn());
        }

        // Fire torpedoes
        Map<Integer, Double> shootingAngles = fleet.getShootingAngles();
        for (Integer shipId: shootingAngles.keySet()) {
            Double shootingAngle = shootingAngles.get(shipId);
            gameEngine.shoot(selectedGameId, shipId,shootingAngle);
        }

        fleet.printFleetInfo();

        guiInfoMessage.setEntities(visibleEntities);
        guiInfoMessage.setSubmarines(submarineList);
        guiInfoMessage.setGame(gameInfoResponse.getGame());

        // Update spark server with new informations
        sparkServer.updateMessage(guiInfoMessage);
    }



    private void printEntityInformation(Entity e) {
        System.out.println("\t" + e.getType() + " " + e.getId());
        System.out.println("\t\towner: " + e.getOwner());
        System.out.println("\t\tposition: " + e.getPosition());
        System.out.println("\t\tangle: " + e.getAngle());
        System.out.println("\t\tvelocity: " + e.getVelocity());
    }
}