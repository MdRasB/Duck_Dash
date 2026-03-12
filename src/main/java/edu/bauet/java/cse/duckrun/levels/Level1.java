package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;

public class Level1 extends Level {

    // --- Level-specific configuration ---
    private static final double LEVEL_SPEED = 4.0;
    private static final String BACKGROUND_PATH = "/images/backgrounds/level1.png";

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
    public Enemy spawnEnemy(double spawnX) {
        // For Level 1, only Cat will be spawned.
        return new Cat(spawnX, groundY, getWorldSpeed());
    }

    @Override
    public Food spawnFood(double spawnX) {
        // For Level 1, only Bread will be spawned.
        return new Bread(spawnX, groundY, getWorldSpeed());
    }

    @Override
    public Obstacle spawnObstacle(double spawnX) {
        // For Level 1, only Bottle will be spawned.
        return new Bottle(spawnX, groundY, getWorldSpeed());
    }
}
