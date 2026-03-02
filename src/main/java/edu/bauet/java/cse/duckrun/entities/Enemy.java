package edu.bauet.java.cse.duckrun.entities;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Enemy {

    protected ImageView view;

    protected Image state1;
    protected Image state2;

    protected double speed;
    protected int frameCounter = 0;
    protected boolean toggleFrame = false;

    protected boolean active = true;
    protected boolean hasCollided = false;

    public Enemy(double startX,
                 double startY,
                 double worldSpeed,
                 double extraSpeed,
                 double displayHeight) {

        this.speed = worldSpeed + extraSpeed;

        view = new ImageView();
        view.setFitHeight(displayHeight);
        view.setPreserveRatio(true);

        view.setLayoutX(startX);
        view.setLayoutY(startY);
    }

    public void update() {
        if (!active) return;

        view.setLayoutX(view.getLayoutX() - speed);
        animate();

        if (view.getLayoutX() + view.getBoundsInParent().getWidth() < 0) {
            active = false;
        }
    }

    protected void animate() {
        frameCounter++;

        if (frameCounter >= getAnimationSpeed()) {
            toggleFrame = !toggleFrame;
            frameCounter = 0;
        }

        view.setImage(toggleFrame ? state1 : state2);
    }

    protected int getAnimationSpeed() {
        return 12;
    }

    protected double getHitboxShrinkX() {
        return 0.2;
    }

    protected double getHitboxShrinkY() {
        return 0.2;
    }

    public Bounds getHitBox() {
        Bounds bounds = view.getBoundsInParent();

        double shrinkX = bounds.getWidth() * getHitboxShrinkX();
        double shrinkY = bounds.getHeight() * getHitboxShrinkY();

        return new BoundingBox(
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
        hasCollided = true;
    }

    public Node getNode() {
        return view;
    }
}