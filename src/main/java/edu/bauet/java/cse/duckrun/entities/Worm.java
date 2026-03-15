package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class Worm extends Food {

    public Worm(double startX, double groundLine, double worldSpeed) {

        super(
                startX,
                groundLine - 57,
                worldSpeed,
                -1.5,
                60
        );

        view.setImage(AssetLoader.getImage("/images/enemies/Worm.png"));
        view.setPreserveRatio(true);
        view.setFitHeight(40);
        view.setLayoutY(+10);
    }
}