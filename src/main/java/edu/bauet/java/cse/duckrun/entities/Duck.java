package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.MainApp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;

public class Duck {

    private StackPane container;      // Holds sprite + hitbox
    private ImageView duckView;
    private Rectangle hitbox;

    // Images
    private Image normalImage;
    private Image crouchImage;

    // Physics
    private double jumpForce = -15;
    private double gravity = 0.8;
    private double velocityY = 0;

    private boolean isOnGround = true;
    private boolean isCrouching = false;

    private final double groundLevel = MainApp.WINDOW_HEIGHT - 150;

    // Hitbox dimensions
    private final double normalWidth = 60;
    private final double normalHeight = 60;

    private final double crouchWidth = 60;
    private final double crouchHeight = 30;

    public Duck(double startX, double startY) {

        // Load images
        normalImage = new Image(
                getClass().getResource("/images/duck/base_duck.png").toExternalForm()
        );

        crouchImage = new Image(
                getClass().getResource("/images/duck/ducking.png").toExternalForm()
        );

        duckView = new ImageView(normalImage);
        duckView.setFitWidth(80);
        duckView.setFitHeight(80);

        // Hitbox (for collision detection)
        hitbox = new Rectangle(normalWidth, normalHeight);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.RED); // DEBUG: remove later

        container = new StackPane(duckView, hitbox);

        container.setLayoutX(startX);
        container.setLayoutY(startY);
    }

    // ========================
    // Jump
    // ========================
    public void jump() {
        if (isOnGround && !isCrouching) {
            velocityY = jumpForce;
            isOnGround = false;
        }
    }

    // ========================
    // Crouch
    // ========================
    public void crouch(boolean state) {

        if (state && isOnGround) {

            isCrouching = true;

            // Change sprite
            duckView.setImage(crouchImage);

            // Change hitbox size
            hitbox.setWidth(crouchWidth);
            hitbox.setHeight(crouchHeight);

            // Adjust Y so duck doesn't float
            container.setLayoutY(container.getLayoutY()+30);

        } else if (!state && isCrouching) {

            isCrouching = false;

            // Back to normal sprite
            duckView.setImage(normalImage);

            // Restore hitbox
            hitbox.setWidth(normalWidth);
            hitbox.setHeight(normalHeight);

            // Reset Y
            container.setLayoutY(container.getLayoutY() - 30);
        }
    }

    // ========================
    // Update Physics
    // ========================
    public void update() {

        velocityY += gravity;
        container.setLayoutY(container.getLayoutY() + velocityY);

        // Ground collision
        if (container.getLayoutY() >= groundLevel) {
            container.setLayoutY(groundLevel);
            velocityY = 0;
            isOnGround = true;
        }
    }

    // ========================
    // Getters
    // ========================
    public StackPane getNode() {
        return container;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
