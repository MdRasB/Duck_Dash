package edu.bauet.java.cse.duckrun.entities;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Food {

    protected Group root;
    protected ImageView view;
    protected Rectangle hitbox;

    protected double speed;
    protected boolean active = true;

    public Food(double startX,
                double startY,
                double worldSpeed,
                double extraSpeed,
                double displayHeight) {

        this.speed = worldSpeed + extraSpeed; // Ignore extraSpeed for stationary items

        view = new ImageView();
        view.setFitHeight(displayHeight);
        view.setPreserveRatio(true);

        hitbox = new Rectangle();
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.TRANSPARENT);

        root = new Group(view, hitbox);

        root.setLayoutX(startX);
        root.setLayoutY(startY);
    }

    public void update(double deltaTime) {
        if (!active) return;

        double effectiveSpeed = speed * 60;
        root.setLayoutX(root.getLayoutX() - effectiveSpeed * deltaTime);
        updateHitbox();

        if (root.getLayoutX() + view.getBoundsInParent().getWidth() < 0) {
            active = false;
        }
    }

    private void updateHitbox() {
        Bounds bounds = view.getBoundsInParent();

        hitbox.setX(bounds.getMinX());
        hitbox.setY(bounds.getMinY());
        hitbox.setWidth(bounds.getWidth());
        hitbox.setHeight(bounds.getHeight());
    }

    public Bounds getHitBox() {
        return hitbox.localToScene(hitbox.getBoundsInLocal());
    }

    public boolean isActive() { return active; }

    public void deactivate() { active = false; }

    public Node getNode() { return root; }
}
