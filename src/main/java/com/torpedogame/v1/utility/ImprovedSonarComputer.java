/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.torpedogame.v1.model.strategy.Fleet;
import com.torpedogame.v1.service.GameAPI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author kkrisz
 */
public class ImprovedSonarComputer implements ISonarComputer {
    
    private static final Map<Integer, Integer> waitMap = new HashMap<>();
    
    // Rebound
    private static final SimpleSonarComputer sc = new SimpleSonarComputer();
    
    @Override
    public void calculateAndExtendFleetSonars(Fleet fleet, GameAPI gameEngine, Integer selectedGameId, MapConfiguration mapConfiguration) {
        fleet.updateCooldowns();
        Map<Integer, Integer> sonarCooldowns = fleet.getSonarCooldowns();
        
        /**
         * Az aktív szonár bekapcsolása után ennyi kört kell várni a hajónak mielõtt újra aktiválhatja
         */
        Integer extendedSonarCooldown = mapConfiguration.getExtendedSonarCooldown();
        
        
        Integer submarineCount = fleet.getSubmarines().size();
        List<Integer> subIds = fleet.getSubmarines().stream().map((submarine) -> submarine.getId()).collect(Collectors.toList());
        
        if (waitMap.size() != submarineCount) {
            // someone died, empty all
            waitMap.clear();
        }
                
        if (submarineCount == 0) {
            // What are we doing here
        } else if (submarineCount == 1) {
            sc.calculateAndExtendFleetSonars(fleet, gameEngine, selectedGameId, mapConfiguration);            
        } else if (submarineCount == 2 || submarineCount == 3) {            
            if (waitMap.isEmpty()) {
                // has to setup ship waitlist
                for (int subIdx = 0; subIdx < submarineCount; subIdx++) {
                    Integer subId = subIds.get(subIdx);
                    waitMap.put(subId, (subIdx * (extendedSonarCooldown/submarineCount)));
                }
            }
            
            System.out.println("DEBUG: WM: ");
            for (Map.Entry<Integer, Integer> entry : waitMap.entrySet()) {
                System.out.println("e: " + entry.getKey() +" "+ entry.getValue());                
            }
            
            for (Integer subId : subIds) {  
                Integer wait = waitMap.get(subId);
                
                if (wait == 0 && sonarCooldowns.get(subId) == 0){
                    gameEngine.extendSonar(selectedGameId, subId);
                    waitMap.replace(subId, extendedSonarCooldown);
                } else if (wait != 0) {
                    if (wait > 0) {
                        waitMap.replace(subId, waitMap.get(subId)-1);                    
                    }
                }
            }
        } else if (submarineCount >= 4) {
            sc.calculateAndExtendFleetSonars(fleet, gameEngine, selectedGameId, mapConfiguration);
        }
    }
    
    private static boolean oneSonarAtLeastActive(Map<Integer, Integer> sonarCooldowns) {
        boolean result = false;
        for (Integer cd  :sonarCooldowns.values()) {
            if (cd != 0) {
                result = true;
            }            
        }
        
        return result;
    }
}
