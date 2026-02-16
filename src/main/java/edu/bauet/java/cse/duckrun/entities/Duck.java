package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.MainApp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Duck Entity Class
 * Handles player movement, gravity, and rendering.
 */
public class Duck {

    private ImageView duckView;

    private double speed = 5;
    private double jumpForce = -15;
    private double gravity = 0.8;
    private double velocityY = 0;
    private boolean isOnGround = true;

    private final double groundLevel = MainApp.WINDOW_HEIGHT - 150;

    public Duck(double startX, double startY) {

        Image duckImage = new Image(
                getClass().getResource("/images/duck/base_duck.png").toExternalForm()
        );

        duckView = new ImageView(duckImage);
        duckView.setFitWidth(80);
        duckView.setFitHeight(80);
        duckView.setLayoutX(startX);
        duckView.setLayoutY(startY);
    }

    // Movement Methods
    public void moveLeft() {
        duckView.setLayoutX(duckView.getLayoutX() - speed);
    }

    public void moveRight() {
        duckView.setLayoutX(duckView.getLayoutX() + speed);
    }

    public void jump() {
        if (isOnGround) {
            velocityY = jumpForce;
            isOnGround = false;
        }
    }

    public void update() {

        // Apply gravity
        velocityY += gravity;
        duckView.setLayoutY(duckView.getLayoutY() + velocityY);

        // Ground collision
        if (duckView.getLayoutY() >= groundLevel) {
            duckView.setLayoutY(groundLevel);
            velocityY = 0;
            isOnGround = true;
        }
    }

    // Getter for adding to Pane
    public ImageView getNode() {
        return duckView;
    }
}
