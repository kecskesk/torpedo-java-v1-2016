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
public class Scores {
    private Map<String, Integer> scores;

    public Scores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public Scores() {
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        String scoresStr = "";
        
        scoresStr = scores.entrySet().stream()
                .map((entry) -> ", " + entry.getKey() + ":" + entry.getValue())
                .reduce(scoresStr, String::concat);
        
        return "Scores: " + scoresStr;
    }
}
