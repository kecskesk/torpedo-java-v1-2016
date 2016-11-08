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
    // TODO maybe create a class for these stores and
    // initialize them in the main function
    private Map<Integer, Coordinate> targetStore;
    private static GuiInfoMessage guiInfoMessage = new GuiInfoMessage();
    private static final SparkServer sparkServer = new SparkServer();
    private Map<Integer, Integer> cooldownStore;

    public static void main( String[] args )
    {
        sparkServer.start();
        
        gameEngine = new GameApiImpl();

        // SETTING GAME ENGINE URL FROM ARGUMENT LIST
        GameApiImpl.setServerUrl(args[0]);

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
        NavigationComputer.setMinSpeed(2 * gameInfoResponse.getGame().getMapConfiguration().getMaxAccelerationPerRound());
        
        // Load map into NavComp
        NavigationComputer.setHeight(gameInfoResponse.getGame().getMapConfiguration().getHeight());
        NavigationComputer.setWidth(gameInfoResponse.getGame().getMapConfiguration().getWidth());
        NavigationComputer.setIslandPositions(gameInfoResponse.getGame().getMapConfiguration().getIslandPositions());
        NavigationComputer.setIslandSize(gameInfoResponse.getGame().getMapConfiguration().getIslandSize());
        NavigationComputer.setSonarRange(gameInfoResponse.getGame().getMapConfiguration().getSonarRange());

        ShootingComputer.setTorpedoRange(gameInfoResponse.getGame().getMapConfiguration().getTorpedoRange());
        ShootingComputer.setTorpedoSpeed(gameInfoResponse.getGame().getMapConfiguration().getTorpedoSpeed());
        ShootingComputer.setTorpedoExplosionRadius(gameInfoResponse.getGame().getMapConfiguration().getTorpedoExplosionRadius());
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

        // Query the submarines
        SubmarinesResponse submarinesResponse = gameEngine.submarines(createdGame.getId());
        List<Submarine> submarineList = submarinesResponse.getSubmarines();
        if (submarineList == null || submarineList.isEmpty()) {
            System.out.println("Submarine list is empty!");
            return;
        }
        

        // Initialize stores
        if (targetStore == null) {
            targetStore = new HashMap<>(submarineList.size());

            for (Submarine s: submarineList) targetStore.put(s.getId(), (s.getId() % 2 == 0)?new Coordinate(gameInfoResponse.getGame().getMapConfiguration().getSonarRange(), gameInfoResponse.getGame().getMapConfiguration().getHeight()/2) : new Coordinate(gameInfoResponse.getGame().getMapConfiguration().getWidth() - 600, gameInfoResponse.getGame().getMapConfiguration().getSonarRange()));
        }
        if (cooldownStore == null) {
            cooldownStore = new HashMap<>(submarineList.size());
            for (Submarine s: submarineList) cooldownStore.put(s.getId(), 0);
        }



        // Give orders for each submarine
        for (Submarine submarine : submarineList) {
            // Extend sonar whenever we can
            if (gameInfoResponse.getGame().getRound() != 0 && gameInfoResponse.getGame().getRound() % gameInfoResponse.getGame().getMapConfiguration().getExtendedSonarCooldown() == 0) {
                System.out.println("Sonar extended!");
                gameEngine.extendSonar(createdGame.getId(), submarine.getId());
            }




            // Try to get previous target from target store.
            Coordinate target = null;
            if (targetStore.containsKey(submarine.getId())) {
                target = targetStore.get(submarine.getId());
            }

            TargetComputer.setMapConfiguration(gameInfoResponse.getGame().getMapConfiguration());
            boolean shouldBeOnLeftSide = (submarine.getId() % 2 == 0);
            target = NavigationComputer.getNextTarget(submarine.getPosition(), target, shouldBeOnLeftSide);
            targetStore.put(submarine.getId(), target);

            System.out.println("SHIP " + submarine.getId());
            System.out.println("position: " + submarine.getPosition());
            System.out.println("angle: " + submarine.getAngle());
            System.out.println("speed: " + submarine.getVelocity());
            System.out.println("current target: " + target.toString());

            // Calculate move modification value and move the submarine
            MoveModification moveModification = NavigationComputer.getMoveModification(submarine.getPosition(), target, submarine.getVelocity(), submarine.getAngle());
            gameEngine.move(createdGame.getId(), submarine.getId(), moveModification.getSpeed(), moveModification.getTurn());

            SonarResponse sonarResponse = gameEngine.sonar(createdGame.getId(), submarine.getId());
            List<Entity> entityList = sonarResponse.getEntities();

            if (entityList == null) {
                System.out.println("Entity list is null, continue with next submarine.");
                continue;
            }
            
            guiEntities.addAll(entityList);

            int cooldownLeft = cooldownStore.get(submarine.getId());
            cooldownLeft = cooldownLeft > 0? cooldownLeft - 1: 0;
            cooldownStore.put(submarine.getId(), cooldownLeft);

            System.out.println("visible entities:");
            for (Entity e : entityList) {
                System.out.println("\t" + e.getType() + " " + e.getId());
                System.out.println("\t\towner: " + e.getOwner());
                System.out.println("\t\tposition: " + e.getPosition());
                System.out.println("\t\tangle: " + e.getAngle());
                System.out.println("\t\tvelocity: " + e.getVelocity());
                if(e.getOwner().getName().equals("BOT")) { // && IT IS A SHIP!
//                    targetStore.put(submarine.getId(), e.getPosition());
                    if (cooldownLeft == 0) {
                        // Red Alert
                        // TODO Check for torpedo cooldown!
                        try {
                            double shootingAngle = ShootingComputer.getShootingAngle(submarine.getPosition(), e.getPosition(), e.getVelocity(), e.getAngle());
                            System.out.println("Firing!");
                            gameEngine.shoot(createdGame.getId(), submarine.getId(), shootingAngle);
                            cooldownStore.put(submarine.getId(), gameInfoResponse.getGame().getMapConfiguration().getTorpedoCooldown());
                        } catch (Exception ise) {
                            System.out.println(ise.getMessage());
                        }

                        // Intercept course
                        // Han: "Keep your distance Chewie but don't look like you're trying to keep your distance."
                        //
                        // Chewbacca: "Ngyargh yargh."
                        //
                        // Han: "I don't know...fly casual."

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
            sparkServer.updateMessage(guiInfoMessage);
        }
    }
}
