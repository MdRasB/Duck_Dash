package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.*;

public class Level1 extends Level {

    public Level1(double worldSpeed, double groundY) {
        super(worldSpeed, groundY);
    }

    @Override
    public Enemy spawnEnemy(double spawnX) {
        // For Level 1, only Cat will be spawned.
        return new Cat(spawnX, groundY, worldSpeed);
    }

    @Override
    public Food spawnFood(double spawnX) {
        // For Level 1, only Bread will be spawned.
        return new Bread(spawnX, groundY, worldSpeed);
    }

    @Override
    public Obstacle spawnObstacle(double spawnX) {
        // For Level 1, only Bottle will be spawned.
        return new Bottle(spawnX, groundY, worldSpeed);
    }
}
