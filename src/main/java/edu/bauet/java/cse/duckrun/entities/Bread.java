package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class Bread extends Food {

    public Bread(double startX, double groundLine, double worldSpeed) {

        super(
                startX,
                groundLine - 50,
                worldSpeed,
                0,
                60
        );

        view.setImage(AssetLoader.getImage("/images/enemies/Bread.png"));
    }
}