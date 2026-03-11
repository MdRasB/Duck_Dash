package edu.bauet.java.cse.duckrun.entities;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Obstacle {

    protected Group root;
    protected ImageView view;
    protected Rectangle hitbox;

    protected double speed;
    protected boolean active = true;
    protected boolean hasCollided = false; // --- ADDED: Flag to track collision ---

    public Obstacle(double startX,
                    double startY,
                    double worldSpeed,
                    double extraSpeed,
                    double displayHeight) {

        this.speed = worldSpeed + extraSpeed;

        view = new ImageView();
        view.setFitHeight(displayHeight);
        view.setPreserveRatio(true);

        hitbox = new Rectangle();
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.TRANSPARENT); // Set to a visible color for debugging if needed

        root = new Group(view, hitbox);

        root.setLayoutX(startX);
        root.setLayoutY(startY);
    }

    public void update() {
        if (!active) return;

        root.setLayoutX(root.getLayoutX() - speed);
        updateHitbox();

        if (root.getLayoutX() + view.getBoundsInParent().getWidth() < 0) {
            active = false;
        }
    }

    private void updateHitbox() {
        Bounds viewBounds = view.getBoundsInParent();

        double shrinkX = viewBounds.getWidth() * getHitboxShrinkX();
        double shrinkYTop = viewBounds.getHeight() * getHitboxShrinkYTop();
        double shrinkYBottom = viewBounds.getHeight() * getHitboxShrinkYBottom();

        hitbox.setX(viewBounds.getMinX() + shrinkX);
        hitbox.setY(viewBounds.getMinY() + shrinkYTop);
        hitbox.setWidth(viewBounds.getWidth() - shrinkX * 2);
        hitbox.setHeight(viewBounds.getHeight() - shrinkYTop - shrinkYBottom);
    }

    protected double getHitboxShrinkX() { return 0.1; }
    protected double getHitboxShrinkYTop() { return 0.1; }
    protected double getHitboxShrinkYBottom() { return 0.1; }

    public Bounds getHitBox() {
        return hitbox.localToScene(hitbox.getBoundsInLocal());
    }

    public boolean isActive() { return active; }

    // --- ADDED: Methods to control collision state ---
    public boolean hasCollided() { return hasCollided; }
    public void markCollided() { hasCollided = true; }

    public Node getNode() { return root; }
}
