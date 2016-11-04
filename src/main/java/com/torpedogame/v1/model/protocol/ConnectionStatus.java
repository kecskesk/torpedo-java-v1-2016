/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torpedogame.v1.model.protocol;

import java.util.Map;

/**
 *
 * @author kkrisz
 */
class ConnectionStatus {
    private Map<String, Boolean> connected;

    public ConnectionStatus(Map<String, Boolean> scores) {
        this.connected = scores;
    }

    public ConnectionStatus() {
    }

    public Map<String, Boolean> getConnected() {
        return connected;
    }

    public void setConnected(Map<String, Boolean> connected) {
        this.connected = connected;
    }

    @Override
    public String toString() {
        String scoresStr = "";
        
        scoresStr = connected.entrySet().stream()
                .map((entry) -> ", " + entry.getKey() + ":" + entry.getValue())
                .reduce(scoresStr, String::concat);
        
        return "ConnectionStatus: " + scoresStr;
    }
}
