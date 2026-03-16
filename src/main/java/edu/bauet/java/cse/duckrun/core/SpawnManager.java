package edu.bauet.java.cse.duckrun.core;

import edu.bauet.java.cse.duckrun.entities.*;

import java.util.Random;

public class SpawnManager {

    private final Random random = new Random();

    private boolean spawnCat = true;
    private boolean spawnBread = true;

    public Enemy spawnEnemy(double spawnX, double groundY, double worldSpeed) {

        if (!spawnCat) return null;

        return new CatBrown(spawnX, groundY, worldSpeed);
    }

    public Food spawnFood(double spawnX, double groundY, double worldSpeed) {

        if (!spawnBread) return null;

        return new Bread(spawnX, groundY, worldSpeed);
    }

    public void setSpawnCat(boolean value) {
        spawnCat = value;
    }

    public void setSpawnBread(boolean value) {
        spawnBread = value;
    }
}