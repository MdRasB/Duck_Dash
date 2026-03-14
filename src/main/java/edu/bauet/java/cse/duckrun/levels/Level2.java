package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;

public class Level2 extends Level {

    // --- Level-specific configuration ---
    private static final double LEVEL_SPEED            = 7.0;
    private static final double BACKGROUND_SCROLL_SPEED = LEVEL_SPEED * 60; // 420 px/sec
    private static final double DUCK_JUMP_SPEED        = 820;               // px/sec — moderate pace
    private static final double DUCK_FALL_SPEED        = 420;               // px/sec
    private static final String BACKGROUND_PATH        = "/images/backgrounds/level2.png";

    public Level2(double groundY) {
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

    @Override
    public Obstacle spawnObstacle(double spawnX) {
        return new Bottle(spawnX, groundY, getWorldSpeed());
    }
}
