package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class Cat extends Enemy {

    public Cat(double startX, double groundLine, double worldSpeed) {
        super(startX, groundLine - 80, worldSpeed, 4, 80);

        state1 = AssetLoader.getImage("/images/enemies/Cat_state_1.png");
        state2 = AssetLoader.getImage("/images/enemies/Cat_state_2.png");

        view.setImage(state1);
    }

    @Override
    protected double getHitboxShrinkX() {
        return 0.2; // Shrink width slightly
    }

    @Override
    protected double getHitboxShrinkYTop() {
        return 0.6; // Cut off the top 60% of the hitbox (makes it easy to jump over)
    }

    @Override
    protected double getHitboxShrinkYBottom() {
        return 0.0; // Keep the bottom flush with the ground
    }
}
