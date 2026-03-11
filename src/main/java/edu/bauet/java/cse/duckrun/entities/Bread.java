package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bread {

    private final Group root;
    private final ImageView view;
    private final Rectangle debugHitbox;

    private final double speed;
    private boolean active = true;

    public Bread(double startX, double groundLine, double worldSpeed) {
        this.speed = worldSpeed;

        view = new ImageView(AssetLoader.getImage("/images/enemies/Bread.png"));
        view.setFitHeight(40);
        view.setPreserveRatio(true);

        debugHitbox = new Rectangle();
        debugHitbox.setFill(Color.TRANSPARENT);
        debugHitbox.setStroke(Color.TRANSPARENT); // Invisible by default
        debugHitbox.setStrokeWidth(0);

        root = new Group(view, debugHitbox);
        root.setLayoutX(startX);
        root.setLayoutY(groundLine - 40); // Position on the ground
    }

    public void update() {
        if (!active) return;

        root.setLayoutX(root.getLayoutX() - speed);
        updateDebugHitbox();

        // Deactivate if off-screen
        if (root.getLayoutX() + view.getBoundsInParent().getWidth() < 0) {
            active = false;
        }
    }

    private void updateDebugHitbox() {
        Bounds viewBounds = view.getBoundsInParent();
        debugHitbox.setX(viewBounds.getMinX());
        debugHitbox.setY(viewBounds.getMinY());
        debugHitbox.setWidth(viewBounds.getWidth());
        debugHitbox.setHeight(viewBounds.getHeight());
    }

    public Bounds getHitBox() {
        return debugHitbox.localToScene(debugHitbox.getBoundsInLocal());
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public Node getNode() {
        return root;
    }
}
