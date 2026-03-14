package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class Cockroach extends Food {

    public Cockroach(double startX, double groundLine, double worldSpeed) {

        super(
                startX,
                groundLine - 57,
                worldSpeed,
                -3,
                60
        );

        view.setImage(AssetLoader.getImage("/images/enemies/Cockroach.png"));
    }
}