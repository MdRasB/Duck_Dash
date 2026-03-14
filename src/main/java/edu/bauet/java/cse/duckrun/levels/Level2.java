package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;

public class Level2 extends Level {

    // --- Level-specific configuration ---
    private static final double LEVEL_SPEED = 7.0;
    private static final String BACKGROUND_PATH = "/images/backgrounds/level2.png";

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
    public Enemy spawnEnemy(double spawnX) {
        // For Level 1, only Cat will be spawned.
        return new Cat(spawnX, groundY, getWorldSpeed());
    }

    @Override
    public Food spawnFood(double spawnX) {
        // For Level 2, only Bread will be spawned.
        return new Bread(spawnX, groundY, getWorldSpeed());
    }

    @Override
    public Obstacle spawnObstacle(double spawnX) {
        // For Level 1, only Bottle will be spawned.
        return new Bottle(spawnX, groundY, getWorldSpeed());
    }
}
