package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class CatBrown extends Enemy {

    public CatBrown(double startX, double groundLine, double worldSpeed) {

        super(startX, groundLine - 80, worldSpeed, 3.2, 80);

        state1 = AssetLoader.getImage("/images/enemies/Cat_state_1.png");
        state2 = AssetLoader.getImage("/images/enemies/Cat_state_2.png");

        view.setImage(state1);
        view.setFitHeight(100);
        view.setPreserveRatio(true);
        view.setLayoutY(-21);
    }

    @Override
    protected double getHitboxShrinkX() {
        return 0.2;
    }

    @Override
    protected double getHitboxShrinkYTop() {
        return 0.6;
    }

    @Override
    protected double getHitboxShrinkYBottom() {
        return 0.0;
    }
}