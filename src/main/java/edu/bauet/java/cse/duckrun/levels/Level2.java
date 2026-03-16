package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;
import java.util.Random;

public class Level2 extends Level {

    // --- Level-specific configuration ---
    private static final double LEVEL_SPEED             = 8.635;
    private static final double BACKGROUND_SCROLL_SPEED = LEVEL_SPEED * 60; // 420 px/sec
    private static final double DUCK_JUMP_SPEED         = 820;              // px/sec — moderate pace
    private static final double DUCK_FALL_SPEED         = 420;              // px/sec
    private static final String BACKGROUND_PATH         = "/images/backgrounds/level2.png";

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
        // Level 2 — Dog and Eagle spawn with equal probability.
        // Dog stays on the ground like a Treeout (extraSpeed = 0).
        if (RANDOM.nextBoolean()) {
            return new Dog(spawnX, groundY, getWorldSpeed());
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

    @Override
    public Enemy spawnEnemy(double spawnX, double worldSpeed) {
        if (RANDOM.nextBoolean()) {
            return new Dog(spawnX, groundY, worldSpeed);
        } else {
            return new Eagle(spawnX, groundY, worldSpeed);
        }
    }

    @Override
    public Food spawnFood(double spawnX, double worldSpeed) {
        return new Worm(spawnX, groundY, worldSpeed);
    }

    @Override
    public Obstacle spawnObstacle(double spawnX, double worldSpeed) {
        return new Treeout(spawnX, groundY, worldSpeed);
    }

    @Override
    public int getLoopsToComplete() {
        return 22; // 15 normal + 1 transition
    }

    @Override
    public String getTransitionImagePath() {
        return "/images/backgrounds/level2_transition.png";
    }
}