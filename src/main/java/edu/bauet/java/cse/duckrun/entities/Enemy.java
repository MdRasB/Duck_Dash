package edu.bauet.java.cse.duckrun.entities;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Enemy {

    protected Group root; // Changed from just ImageView to Group
    protected ImageView view;
    protected Rectangle debugHitbox;

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
        
        // Debug Hitbox (Invisible)
        debugHitbox = new Rectangle();
        debugHitbox.setFill(Color.TRANSPARENT);
        debugHitbox.setStroke(Color.TRANSPARENT); // Made invisible
        debugHitbox.setStrokeWidth(0);

        root = new Group();
        root.getChildren().addAll(view, debugHitbox);

        root.setLayoutX(startX);
        root.setLayoutY(startY);
    }

    public void update() {
        if (!active) return;

        root.setLayoutX(root.getLayoutX() - speed);
        animate();
        updateDebugHitbox();

        if (root.getLayoutX() + view.getBoundsInParent().getWidth() < 0) {
            active = false;
        }
    }
    
    private void updateDebugHitbox() {
        Bounds viewBounds = view.getBoundsInParent();
        
        double shrinkX = viewBounds.getWidth() * getHitboxShrinkX();
        double shrinkYTop = viewBounds.getHeight() * getHitboxShrinkYTop();
        double shrinkYBottom = viewBounds.getHeight() * getHitboxShrinkYBottom();
        
        debugHitbox.setX(viewBounds.getMinX() + shrinkX);
        debugHitbox.setY(viewBounds.getMinY() + shrinkYTop);
        debugHitbox.setWidth(viewBounds.getWidth() - shrinkX * 2);
        debugHitbox.setHeight(viewBounds.getHeight() - shrinkYTop - shrinkYBottom);
    }

    protected void animate() {
        frameCounter++;

        if (frameCounter >= 30) {
            toggleFrame = !toggleFrame;
            frameCounter = 0;
        }

        view.setImage(toggleFrame ? state1 : state2);
    }

    // Default hitbox shrinking values (can be overridden by subclasses)
    protected int getAnimationSpeed() { return 12; }
    protected double getHitboxShrinkX() { return 0.2; }
    protected double getHitboxShrinkYTop() { return 0.2; } // Default shrink from top
    protected double getHitboxShrinkYBottom() { return 0.2; } // Default shrink from bottom

    public Bounds getHitBox() {
        return debugHitbox.localToScene(debugHitbox.getBoundsInLocal());
    }

    public boolean isActive() { return active; }
    public boolean hasCollided() { return hasCollided; }
    public void markCollided() { hasCollided = true; }
    public Node getNode() { return root; }
}
