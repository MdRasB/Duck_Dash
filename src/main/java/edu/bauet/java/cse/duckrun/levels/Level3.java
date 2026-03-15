package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;

import java.util.Random;

public class Level3 extends Level {

    // --- Level-specific configuration ---
    private static final double LEVEL_SPEED            = 10.0;
    private static final double BACKGROUND_SCROLL_SPEED = LEVEL_SPEED * 60; // 1200 px/sec — very fast
    private static final double DUCK_JUMP_SPEED        = 1050;              // px/sec — snappy to keep up
    private static final double DUCK_FALL_SPEED        = 580;               // px/sec
    private static final String BACKGROUND_PATH        = "/images/backgrounds/level3.png";

    private final Random random = new Random();

    public Level3(double groundY) {
        super(groundY);
    }

    @Override
    public String getBackgroundPath() {
        return BACKGROUND_PATH;
    }

    @Override
    public double getWorldSpeed() {
        return LEVEL_SPEED;
    }

    @Override
    public double getBackgroundScrollSpeed() {
        return BACKGROUND_SCROLL_SPEED;
    }

    @Override
    public double getDuckJumpSpeed() {
        return DUCK_JUMP_SPEED;
    }

    @Override
    public double getDuckFallSpeed() {
        return DUCK_FALL_SPEED;
    }

    @Override
    public Enemy spawnEnemy(double spawnX) {
        return new Cat(spawnX, groundY, getWorldSpeed());
    }

    @Override
    public Food spawnFood(double spawnX) {
        return new Bread(spawnX, groundY, getWorldSpeed());
    }

    /**
     * Randomly spawns one of three obstacles:
     * - Bottle:  jump over it (short).
     * - Treein:  jump over it (medium).
     * - ChairW:  jump over OR crouch under it (tall).
     */
    @Override
    public Obstacle spawnObstacle(double spawnX) {
        switch (random.nextInt(3)) {
            case 0:  return new Bottle(spawnX, groundY, getWorldSpeed());
            case 1:  return new Treein(spawnX, groundY, getWorldSpeed());
            default: return new ChairW(spawnX, groundY, getWorldSpeed());
        }
    }
}