package com.torpedogame.v1;

import com.torpedogame.v1.gui.GuiInfoMessage;
import com.torpedogame.v1.gui.SparkServer;
import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.torpedogame.v1.model.protocol.*;
import com.torpedogame.v1.model.utility.MoveModification;
import com.torpedogame.v1.service.GameAPI;
import com.torpedogame.v1.service.GameApiImpl;

import com.torpedogame.v1.utility.NavigationComputer;
import com.torpedogame.v1.utility.ShootingComputer;
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
    // TODO maybe create a class for these stores and
    // initialize them in the main function
    private Map<Integer, Coordinate> targetStore;
    private static GuiInfoMessage guiInfoMessage = new GuiInfoMessage();
    private static final SparkServer sparkServer = new SparkServer();
    private Map<Integer, Integer> cooldownStore;
    private static int selectedGameId = -1;

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
            NavigationComputer.setMinSpeed(2 * mapConfiguration.getMaxAccelerationPerRound());

            // Load map into NavComp
            NavigationComputer.setHeight(mapConfiguration.getHeight());
            NavigationComputer.setWidth(mapConfiguration.getWidth());
            NavigationComputer.setIslandPositions(mapConfiguration.getIslandPositions());
            NavigationComputer.setIslandSize(mapConfiguration.getIslandSize());
            NavigationComputer.setSonarRange(mapConfiguration.getSonarRange());

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
        List<Entity> guiEntities = new ArrayList<>();
        
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

        // Initialize the target store
        if (targetStore == null) {
            targetStore = new HashMap<>(submarineList.size());

            for (Submarine s: submarineList) {
                Coordinate newTarget = null;
                if (s.getId() % 2 == 0) {
                    newTarget = new Coordinate(mapConfiguration.getSonarRange(), mapConfiguration.getHeight() / 2);
                } else {
                    newTarget = new Coordinate(mapConfiguration.getWidth() - 600, mapConfiguration.getSonarRange());
                }
                targetStore.put(s.getId(), newTarget);
            }
        }

        // Initialize the cooldown store
        if (cooldownStore == null) {
            cooldownStore = new HashMap<>(submarineList.size());
            for (Submarine s: submarineList) {
                cooldownStore.put(s.getId(), 0);
            }
        }

        // Give orders to each submarine
        for (Submarine submarine : submarineList) {
            // Use extended sonar whenever we can
            if (currentGame.getRound() != 0 && (currentGame.getRound() % mapConfiguration.getExtendedSonarCooldown() == 0)) {
                System.out.println("Sonar extended!");
                gameEngine.extendSonar(selectedGameId, submarine.getId());
            }

            // Try to get previous target from target store.
            Coordinate target = null;
            if (targetStore.containsKey(submarine.getId())) {
                target = targetStore.get(submarine.getId());
            }

            boolean submarineShouldBeOnLeftSide = (submarine.getId() % 2 == 0);
            target = NavigationComputer.getNextTarget(submarine.getPosition(), target, submarineShouldBeOnLeftSide);
            targetStore.put(submarine.getId(), target);

            printSubmarineInformation(submarine, target);

            // Calculate move modification value and move the submarine
            MoveModification moveModification = NavigationComputer.getMoveModification(submarine.getPosition(), target, submarine.getVelocity(), submarine.getAngle());
            gameEngine.move(selectedGameId, submarine.getId(), moveModification.getSpeed(), moveModification.getTurn());

            // Get the sonar information
            SonarResponse sonarResponse = gameEngine.sonar(selectedGameId, submarine.getId());
            List<Entity> entityList = sonarResponse.getEntities();

            if (entityList == null) {
                System.out.println("Entity list is null, continue with next submarine.");
                continue;
            }
            
            guiEntities.addAll(entityList);

            int cooldownLeft = cooldownStore.get(submarine.getId());
            cooldownLeft = cooldownLeft > 0 ? cooldownLeft - 1 : 0;
            cooldownStore.put(submarine.getId(), cooldownLeft);

            System.out.println("visible entities:");

            // Loop through each entity the sonar is seeing.
            for (Entity e : entityList) {
                printEntityInformation(e);

                if(e.getOwner().getName().equals("BOT")) { // && IT IS A SHIP!
                    if (cooldownLeft == 0) {
                        // Red Alert
                        // TODO Check for torpedo cooldown!
                        try {
                            double shootingAngle = ShootingComputer.getShootingAngle(submarine.getPosition(), e.getPosition(), e.getVelocity(), e.getAngle());
                            System.out.println("Firing!");
                            gameEngine.shoot(selectedGameId, submarine.getId(), shootingAngle);
                            cooldownStore.put(submarine.getId(), mapConfiguration.getTorpedoCooldown());
                        } catch (Exception ise) {
                            System.out.println(ise.getMessage());
                        }
                    } else {
                        System.out.println("Reload is complete in " + cooldownLeft + " rounds.");
                    }
                }
            }

            System.out.println("---------------------------------------------------------------------------------");
            
            guiInfoMessage.setEntities(guiEntities);
            guiInfoMessage.setSubmarines(submarineList);
            guiInfoMessage.setGame(gameInfoResponse.getGame());
            guiInfoMessage.setTargetStore(targetStore);

            // Update spark server with new informations
            sparkServer.updateMessage(guiInfoMessage);
        }
    }

    private void printSubmarineInformation(Submarine submarine, Coordinate target) {
        System.out.println("SHIP " + submarine.getId());
        System.out.println("position: " + submarine.getPosition());
        System.out.println("angle: " + submarine.getAngle());
        System.out.println("speed: " + submarine.getVelocity());
        System.out.println("current target: " + target.toString());
    }

    private void printEntityInformation(Entity e) {
        System.out.println("\t" + e.getType() + " " + e.getId());
        System.out.println("\t\towner: " + e.getOwner());
        System.out.println("\t\tposition: " + e.getPosition());
        System.out.println("\t\tangle: " + e.getAngle());
        System.out.println("\t\tvelocity: " + e.getVelocity());
    }
}