package edu.bauet.java.cse.duckrun.entities;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Duck {

    private ImageView duckView;

    private double velocityY = 0;
    private double groundLine;

    // Configurable physics
    private double gravityUp = 0.6;
    private double gravityDown = 1.4;
    private double jumpForce = -15;

    private boolean crouching = false;

    public Duck(double x, double groundLine) {

        this.groundLine = groundLine;

        Image duckImage = new Image(
                getClass().getResource("/images/duck/running.png").toExternalForm()
        );

        duckView = new ImageView(duckImage);
        duckView.setLayoutX(x);
        duckView.setLayoutY(groundLine);
    }

    public void update() {

        // Apply gravity only if not on ground
        if (!isOnGround()) {

            if (velocityY < 0) {
                velocityY += gravityUp;      // Going up
            } else {
                velocityY += gravityDown;    // Falling down
            }
        }

        duckView.setLayoutY(duckView.getLayoutY() + velocityY);

        // Ground collision
        if (duckView.getLayoutY() >= groundLine) {
            duckView.setLayoutY(groundLine);
            velocityY = 0;
        }
    }

    public void jump() {
        if (isOnGround()) {
            velocityY = jumpForce;
        }
    }

    private boolean isOnGround() {
        return duckView.getLayoutY() >= groundLine;
    }

    public void setCrouching(boolean crouch) {
        this.crouching = crouch;
        duckView.setScaleY(crouch ? 0.7 : 1.0);
    }

    // 🔥 Physics setters (very important)
    public void setGravity(double gravityUp, double gravityDown) {
        this.gravityUp = gravityUp;
        this.gravityDown = gravityDown;
    }

    public void setJumpForce(double jumpForce) {
        this.jumpForce = jumpForce;
    }

    public Node getNode() {
        return duckView;
    }
}
