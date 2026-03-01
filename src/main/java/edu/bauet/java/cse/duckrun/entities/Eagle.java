package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Eagle {

    private ImageView eagleView;

    private Image state1;
    private Image state2;

    private double speed = 6;

    private int frameCounter = 0;
    private boolean toggleFrame = false;

    private boolean active = true;
    private boolean hasCollided = false;

    private static final double DISPLAY_HEIGHT = 100;

    public Eagle(double startX, double startY) {

        state1 = AssetLoader.getImage("/images/enemies/Eagle_state_1.png");
        state2 = AssetLoader.getImage("/images/enemies/Eagle_state_2.png");

        eagleView = new ImageView(state1);
        eagleView.setFitHeight(DISPLAY_HEIGHT);
        eagleView.setPreserveRatio(true);

        eagleView.setLayoutX(startX);
        eagleView.setLayoutY(startY-25);
    }

    public void update() {
        if (!active) return;

        eagleView.setLayoutX(eagleView.getLayoutX() - speed);
        animate();

        if (eagleView.getLayoutX() + eagleView.getBoundsInParent().getWidth() < 0) {
            active = false;
        }
    }

    private void animate() {
        frameCounter++;
        if (frameCounter >= 10) {
            toggleFrame = !toggleFrame;
            frameCounter = 0;
        }
        eagleView.setImage(toggleFrame ? state1 : state2);
    }

    // 🔥 CUSTOM HITBOX (smaller than image bounds)
    public Bounds getHitBox() {

        Bounds bounds = eagleView.getBoundsInParent();

        double shrinkX = bounds.getWidth() * 0.25;
        double shrinkY = bounds.getHeight() * 0.2;

        return new javafx.geometry.BoundingBox(
                bounds.getMinX() + shrinkX,
                bounds.getMinY() + shrinkY,
                bounds.getWidth() - shrinkX * 2,
                bounds.getHeight() - shrinkY * 2
        );
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasCollided() {
        return hasCollided;
    }

    public void markCollided() {
        this.hasCollided = true;
    }

    public Node getNode() {
        return eagleView;
    }
}