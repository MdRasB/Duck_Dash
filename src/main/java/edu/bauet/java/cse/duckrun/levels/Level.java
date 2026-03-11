package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.Enemy;
import edu.bauet.java.cse.duckrun.entities.Food;
import edu.bauet.java.cse.duckrun.entities.Obstacle;

public abstract class Level {

    protected double worldSpeed;
    protected double groundY;

    public Level(double worldSpeed, double groundY) {
        this.worldSpeed = worldSpeed;
        this.groundY = groundY;
    }

    public abstract Enemy spawnEnemy(double spawnX);

    public abstract Food spawnFood(double spawnX);

    // --- ADDED: Abstract method for spawning obstacles ---
    public abstract Obstacle spawnObstacle(double spawnX);

}
