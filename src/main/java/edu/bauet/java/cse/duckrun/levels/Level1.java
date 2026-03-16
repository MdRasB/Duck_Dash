package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;

import java.util.Random;

public class Level1 extends Level {

    // --- Level-specific configuration ---
    private static final double LEVEL_SPEED            = 6.5;
    private static final double BACKGROUND_SCROLL_SPEED = LEVEL_SPEED * 60; // 330 px/sec
    private static final double DUCK_JUMP_SPEED        = 700;               // px/sec — relaxed pace
    private static final double DUCK_FALL_SPEED        = 350;               // px/sec
    private static final String BACKGROUND_PATH        = "/images/backgrounds/level1.png";

    private final Random random = new Random();

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
        return new Cockroach(spawnX, groundY, getWorldSpeed());
    }

    /**
     * Randomly spawns either a Bottle or a ChairB as the obstacle.
     * - Bottle:  duck can jump over it (short obstacle).
     * - ChairB:  duck can jump over OR crouch under it (tall obstacle).
     */
    @Override
    public Obstacle spawnObstacle(double spawnX) {
        if (random.nextBoolean()) {
            return new Bottle(spawnX, groundY, getWorldSpeed());
        } else {
            return new ChairB(spawnX, groundY, getWorldSpeed());
        }
    }

    @Override
    public int getLoopsToComplete() {
        return 3; // 10 normal + 1 transition
    }

    @Override
    public String getTransitionImagePath() {
        return "/images/backgrounds/level1_transition.png";
    }
}