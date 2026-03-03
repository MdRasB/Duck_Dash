package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class Eagle extends Enemy {

    public Eagle(double startX, double startY, double worldSpeed) {

        super(startX,
                startY,
                worldSpeed,
                1.5,
                80);

        state1 = AssetLoader.getImage("/images/enemies/Eagle_state_1.png");
        state2 = AssetLoader.getImage("/images/enemies/Eagle_state_2.png");

        view.setImage(state1);
    }

    @Override
    protected double getHitboxShrinkX() {
        return 0.25;
    }

    protected double getHitboxShrinkY() {
        return 0.2;
    }

    @Override
    protected int getAnimationSpeed() {
        return 10;
    }
}