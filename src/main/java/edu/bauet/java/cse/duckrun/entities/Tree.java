package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader; // Import AssetLoader

public class Tree extends Obstacle {

    public Tree(double startX, double startY, double worldSpeed) {
        super(startX, startY, worldSpeed, 1.0, 100); // extraSpeed and displayHeight are passed to Obstacle constructor

        // Load and set the image for the tree
        view.setImage(AssetLoader.getImage("/images/obstacles/tree.png"));
    }

    // You can override hitbox shrinking methods here if needed for specific tree hitbox
    @Override
    protected double getHitboxShrinkX() {
        return 0.1; // Example: shrink 10% from each side
    }

    @Override
    protected double getHitboxShrinkYTop() {
        return 0.05; // Example: shrink 5% from top
    }

    @Override
    protected double getHitboxShrinkYBottom() {
        return 0.05; // Example: shrink 5% from bottom
    }
}
