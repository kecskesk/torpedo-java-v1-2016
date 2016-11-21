package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.torpedogame.v1.model.strategy.Fleet;
import com.torpedogame.v1.service.GameAPI;
import java.util.Map;

/**
 *
 * @author kkrisz
 */
public class SimpleSonarComputer implements ISonarComputer {
    
    @Override
    public void calculateAndExtendFleetSonars(Fleet fleet, GameAPI gameEngine, Integer selectedGameId, MapConfiguration mapConfiguration) {
        fleet.updateCooldowns();
        Map<Integer, Integer> sonarCooldowns = fleet.getSonarCooldowns();
        for (Map.Entry<Integer, Integer> entry : sonarCooldowns.entrySet()) {
            Integer subId = entry.getKey();
            Integer cooldown = entry.getValue();
            
            // TODO implement more efficient ways to do this
            if (cooldown == 0) {
                gameEngine.extendSonar(selectedGameId, subId);
            }
        }
    }
}
