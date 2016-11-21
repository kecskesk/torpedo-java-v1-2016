package com.torpedogame.v1.utility;

import com.torpedogame.v1.model.game_control.MapConfiguration;
import com.torpedogame.v1.model.strategy.Fleet;
import com.torpedogame.v1.service.GameAPI;

/**
 *
 * @author kkrisz
 */
public interface ISonarComputer {
    void calculateAndExtendFleetSonars(Fleet fleet, GameAPI gameEngine, Integer selectedGameId, MapConfiguration mapConfiguration);
}
