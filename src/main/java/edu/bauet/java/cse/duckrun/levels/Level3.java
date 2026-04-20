package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;

import java.util.Random;

public class Level3 extends Level {

    private static final double LEVEL_SPEED            = 11.2;
    private static final double BACKGROUND_SCROLL_SPEED = LEVEL_SPEED * 60;
    private static final double DUCK_JUMP_SPEED        = 1050;
    private static final double DUCK_FALL_SPEED        = 580;
    private static final String BACKGROUND_PATH        = "/images/backgrounds/level3.png";

    private final Random random = new Random();

    public Level3(double groundY) {
        super(groundY);
    }

    @Override public String getBackgroundPath()        { return BACKGROUND_PATH; }
    @Override public double getWorldSpeed()            { return LEVEL_SPEED; }
    @Override public double getBackgroundScrollSpeed() { return BACKGROUND_SCROLL_SPEED; }
    @Override public double getDuckJumpSpeed()         { return DUCK_JUMP_SPEED; }
    @Override public double getDuckFallSpeed()         { return DUCK_FALL_SPEED; }

    /**
     * Level 3's main enemy is the Boy — a tall, lethal character that the duck
     * MUST crouch under. One hit from the Boy drains all health instantly.
     */
    @Override
    public Enemy spawnEnemy(double spawnX) {
        if (random.nextBoolean()) {
            return new Boy(spawnX, groundY, getWorldSpeed());
        } else {
            return new CatBrown(spawnX, groundY, getWorldSpeed());
        }
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

    @Override
    public Enemy spawnEnemy(double spawnX, double worldSpeed) {
        if (random.nextBoolean()) {
            return new Boy(spawnX, groundY, worldSpeed);
        } else {
            return new CatBrown(spawnX, groundY, worldSpeed);
        }
    }

    @Override
    public Food spawnFood(double spawnX, double worldSpeed) {
        return new Bread(spawnX, groundY, worldSpeed);
    }

    @Override
    public Obstacle spawnObstacle(double spawnX, double worldSpeed) {
        switch (random.nextInt(3)) {
            case 0:  return new Bottle(spawnX, groundY, worldSpeed);
            case 1:  return new Treein(spawnX, groundY, worldSpeed);
            default: return new ChairW(spawnX, groundY, worldSpeed);
        }
    }

    @Override
    public int getLoopsToComplete() {
        return 25; // 24 normal + 1 transition
    }

    @Override
    public String getTransitionImagePath() {
        return "/images/backgrounds/level3_transition.png";
    }
}