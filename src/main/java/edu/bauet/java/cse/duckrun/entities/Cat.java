package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class Cat extends Enemy {

    public Cat(double startX, double groundY, double worldSpeed) {

        super(startX,
                groundY - 50,
                worldSpeed,
                1.0,   // slightly faster than map
                70);

        state1 = AssetLoader.getImage("/images/enemies/Cat_state_1.png");
        state2 = AssetLoader.getImage("/images/enemies/Cat_state_2.png");

        view.setImage(state1);
    }

    @Override
    protected double getHitboxShrinkY() {
        return 0.25;
    }
}