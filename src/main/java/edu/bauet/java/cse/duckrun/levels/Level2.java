package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;
import java.util.Random;

public class Level2 extends Level {

    // --- Level-specific configuration ---
    private static final double LEVEL_SPEED            = 7.0;
    private static final double BACKGROUND_SCROLL_SPEED = LEVEL_SPEED * 60; // 420 px/sec
    private static final double DUCK_JUMP_SPEED        = 820;               // px/sec — moderate pace
    private static final double DUCK_FALL_SPEED        = 420;               // px/sec
    private static final String BACKGROUND_PATH        = "/images/backgrounds/level2.png";

    private static final Random RANDOM = new Random();

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
        // Level 2 — Cat and Eagle spawn with equal probability
        if (RANDOM.nextBoolean()) {
            return new Cat(spawnX, groundY, getWorldSpeed());
        } else {
            return new Eagle(spawnX, groundY, getWorldSpeed());
        }
    }

    @Override
    public Food spawnFood(double spawnX) {
        return new Worm(spawnX, groundY, getWorldSpeed());
    }

    @Override
    public Obstacle spawnObstacle(double spawnX) {
        return new Treeout(spawnX, groundY, getWorldSpeed());
    }
}