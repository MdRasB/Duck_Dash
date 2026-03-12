package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.entities.Enemy;
import edu.bauet.java.cse.duckrun.entities.Food;
import edu.bauet.java.cse.duckrun.entities.Obstacle;

public abstract class Level {

    protected double groundY;

    public Level(double groundY) {
        this.groundY = groundY;
    }

    // --- Abstract methods to be implemented by each level ---
    
    public abstract String getBackgroundPath();
    
    public abstract double getWorldSpeed();

    public abstract Enemy spawnEnemy(double spawnX);

    public abstract Food spawnFood(double spawnX);

    public abstract Obstacle spawnObstacle(double spawnX);

}
