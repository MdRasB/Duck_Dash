package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.Enemy;
import edu.bauet.java.cse.duckrun.entities.Food;
import edu.bauet.java.cse.duckrun.entities.Obstacle;

public abstract class Level {

    protected double groundY;

    public Level(double groundY) {
        this.groundY = groundY;
    }

    // --- Abstract methods to be implemented by each level ---

    public abstract String getBackgroundPath();

    public abstract double getWorldSpeed();

    /**
     * Controls how fast the background scrolls (pixels per second).
     * Defined per-level so each level can fine-tune the feel independently.
     */
    public abstract double getBackgroundScrollSpeed();

    /**
     * Controls how fast the duck rises during a jump (pixels per second).
     * Higher levels should feel snappier, so each level sets its own value.
     */
    public abstract double getDuckJumpSpeed();

    /**
     * Controls how fast the duck falls back down after a jump (pixels per second).
     * Higher levels should feel snappier, so each level sets its own value.
     */
    public abstract double getDuckFallSpeed();

    public abstract Enemy spawnEnemy(double spawnX);
    public abstract Food spawnFood(double spawnX);
    public abstract Obstacle spawnObstacle(double spawnX);

    /**
     * Spawn overloads that accept a custom world speed — used by EndlessGameScene
     * so escalating speed is applied to newly spawned entities without touching Level subclasses.
     * Default implementations temporarily override groundY-based speed via reflection-free pattern:
     * subclasses that want custom behaviour can override these too.
     */
    public Enemy    spawnEnemy(double spawnX, double worldSpeed)    { return spawnEnemy(spawnX); }
    public Food     spawnFood(double spawnX, double worldSpeed)     { return spawnFood(spawnX); }
    public Obstacle spawnObstacle(double spawnX, double worldSpeed) { return spawnObstacle(spawnX); }

    /**
     * Number of normal background loops before the transition image appears.
     */
    public abstract int getLoopsToComplete();

    /**
     * Path to the transition image shown as the final background loop.
     * Return null if the level has no transition (e.g. Level 3).
     */
    public abstract String getTransitionImagePath();

}