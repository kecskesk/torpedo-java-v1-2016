package com.torpedogame.v1.model.game_control;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.List;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class MapConfiguration {
    private int width;
    private int height;
    private List<Coordinate> islandPositions;
    private int teamCount;
    private int submarinesPerTeam;
    private int torpedoDamage;
    private int torpedoHitScore;
    private int torpedoDestroyScore;
    private int torpedoHitPenalty;
    private int torpedoCooldown;
    private int sonarRange;
    private int extendedSonarRange;
    private int extendedSonarRounds;
    private int extendedSonarCooldown;
    private int torpedoSpeed;
    private int torpedoExplosionRadius;
    private int roundLength;
    private int islandSize;
    private int submarineSize;
    private int rounds;
    private int maxSteeringPerRound;
    private int maxAccelerationPerRound;
    private int maxSpeed;
    private int torpedoRange;
    private int rateLimitedPenalty;

    public MapConfiguration(int extendedSonarCooldown, int extendedSonarRange, int extendedSonarRounds, int height, List<Coordinate> islandPositions, int islandSize, int maxAccelerationPerRound, int maxSpeed, int maxSteeringPerRound, int rateLimitedPenalty, int roundLength, int rounds, int sonarRange, int submarineSize, int submarinesPerTeam, int teamCount, int torpedoCooldown, int torpedoDamage, int torpedoDestroyScore, int torpedoExplosionRadius, int torpedoHitPenalty, int torpedoHitScore, int torpedoRange, int torpedoSpeed, int width) {
        this.extendedSonarCooldown = extendedSonarCooldown;
        this.extendedSonarRange = extendedSonarRange;
        this.extendedSonarRounds = extendedSonarRounds;
        this.height = height;
        this.islandPositions = islandPositions;
        this.islandSize = islandSize;
        this.maxAccelerationPerRound = maxAccelerationPerRound;
        this.maxSpeed = maxSpeed;
        this.maxSteeringPerRound = maxSteeringPerRound;
        this.rateLimitedPenalty = rateLimitedPenalty;
        this.roundLength = roundLength;
        this.rounds = rounds;
        this.sonarRange = sonarRange;
        this.submarineSize = submarineSize;
        this.submarinesPerTeam = submarinesPerTeam;
        this.teamCount = teamCount;
        this.torpedoCooldown = torpedoCooldown;
        this.torpedoDamage = torpedoDamage;
        this.torpedoDestroyScore = torpedoDestroyScore;
        this.torpedoExplosionRadius = torpedoExplosionRadius;
        this.torpedoHitPenalty = torpedoHitPenalty;
        this.torpedoHitScore = torpedoHitScore;
        this.torpedoRange = torpedoRange;
        this.torpedoSpeed = torpedoSpeed;
        this.width = width;
    }

    public int getExtendedSonarCooldown() {
        return extendedSonarCooldown;
    }

    public void setExtendedSonarCooldown(int extendedSonarCooldown) {
        this.extendedSonarCooldown = extendedSonarCooldown;
    }

    public int getExtendedSonarRange() {
        return extendedSonarRange;
    }

    public void setExtendedSonarRange(int extendedSonarRange) {
        this.extendedSonarRange = extendedSonarRange;
    }

    public int getExtendedSonarRounds() {
        return extendedSonarRounds;
    }

    public void setExtendedSonarRounds(int extendedSonarRounds) {
        this.extendedSonarRounds = extendedSonarRounds;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Coordinate> getIslandPositions() {
        return islandPositions;
    }

    public void setIslandPositions(List<Coordinate> islandPositions) {
        this.islandPositions = islandPositions;
    }

    public int getIslandSize() {
        return islandSize;
    }

    public void setIslandSize(int islandSize) {
        this.islandSize = islandSize;
    }

    public int getMaxAccelerationPerRound() {
        return maxAccelerationPerRound;
    }

    public void setMaxAccelerationPerRound(int maxAccelerationPerRound) {
        this.maxAccelerationPerRound = maxAccelerationPerRound;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getMaxSteeringPerRound() {
        return maxSteeringPerRound;
    }

    public void setMaxSteeringPerRound(int maxSteeringPerRound) {
        this.maxSteeringPerRound = maxSteeringPerRound;
    }

    public int getRateLimitedPenalty() {
        return rateLimitedPenalty;
    }

    public void setRateLimitedPenalty(int rateLimitedPenalty) {
        this.rateLimitedPenalty = rateLimitedPenalty;
    }

    public int getRoundLength() {
        return roundLength;
    }

    public void setRoundLength(int roundLength) {
        this.roundLength = roundLength;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getSonarRange() {
        return sonarRange;
    }

    public void setSonarRange(int sonarRange) {
        this.sonarRange = sonarRange;
    }

    public int getSubmarineSize() {
        return submarineSize;
    }

    public void setSubmarineSize(int submarineSize) {
        this.submarineSize = submarineSize;
    }

    public int getSubmarinesPerTeam() {
        return submarinesPerTeam;
    }

    public void setSubmarinesPerTeam(int submarinesPerTeam) {
        this.submarinesPerTeam = submarinesPerTeam;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public int getTorpedoCooldown() {
        return torpedoCooldown;
    }

    public void setTorpedoCooldown(int torpedoCooldown) {
        this.torpedoCooldown = torpedoCooldown;
    }

    public int getTorpedoDamage() {
        return torpedoDamage;
    }

    public void setTorpedoDamage(int torpedoDamage) {
        this.torpedoDamage = torpedoDamage;
    }

    public int getTorpedoDestroyScore() {
        return torpedoDestroyScore;
    }

    public void setTorpedoDestroyScore(int torpedoDestroyScore) {
        this.torpedoDestroyScore = torpedoDestroyScore;
    }

    public int getTorpedoExplosionRadius() {
        return torpedoExplosionRadius;
    }

    public void setTorpedoExplosionRadius(int torpedoExplosionRadius) {
        this.torpedoExplosionRadius = torpedoExplosionRadius;
    }

    public int getTorpedoHitPenalty() {
        return torpedoHitPenalty;
    }

    public void setTorpedoHitPenalty(int torpedoHitPenalty) {
        this.torpedoHitPenalty = torpedoHitPenalty;
    }

    public int getTorpedoHitScore() {
        return torpedoHitScore;
    }

    public void setTorpedoHitScore(int torpedoHitScore) {
        this.torpedoHitScore = torpedoHitScore;
    }

    public int getTorpedoRange() {
        return torpedoRange;
    }

    public void setTorpedoRange(int torpedoRange) {
        this.torpedoRange = torpedoRange;
    }

    public int getTorpedoSpeed() {
        return torpedoSpeed;
    }

    public void setTorpedoSpeed(int torpedoSpeed) {
        this.torpedoSpeed = torpedoSpeed;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
