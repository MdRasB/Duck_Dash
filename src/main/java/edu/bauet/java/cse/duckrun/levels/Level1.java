package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;

public class Level1 extends Level {

    // --- Level-specific configuration ---
    private static final double LEVEL_SPEED            = 5.5;
    private static final double BACKGROUND_SCROLL_SPEED = LEVEL_SPEED * 60; // 330 px/sec
    private static final double DUCK_JUMP_SPEED        = 700;               // px/sec — relaxed pace
    private static final double DUCK_FALL_SPEED        = 350;               // px/sec
    private static final String BACKGROUND_PATH        = "/images/backgrounds/level1.png";

    public Level1(double groundY) {
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
