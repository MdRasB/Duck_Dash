package edu.bauet.java.cse.duckrun.entities;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Duck {

    private ImageView duckView;
    private Image runningImage;
    private Image crouchingImage;

    private double velocityY = 0;
    private double groundLine;

    // Configurable physics
    private double gravityUp = 1.2;
    private double gravityDown = 0.05; // Adjusted for slower fall
    private double jumpForce = -16;

    private boolean crouching = false;

    public Duck(double x, double groundLine) {

        this.groundLine = groundLine;

        runningImage = new Image(
                getClass().getResource("/images/duck/running.png").toExternalForm()
        );
        
        crouchingImage = new Image(
                getClass().getResource("/images/duck/ducking.png").toExternalForm()
        );

        duckView = new ImageView(runningImage);
        duckView.setFitHeight(80);
        duckView.setFitWidth(80);
        duckView.setPreserveRatio(false);
        duckView.setLayoutX(x);
        duckView.setLayoutY(groundLine);
    }

    public void update() {

        // Apply gravity only if not on ground
        if (!isOnGround()) {

            if (velocityY <= 0) {
                velocityY += gravityUp;      // Going up
            } else {
                velocityY = gravityDown;    // Falling down
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
        if (crouch) {
            duckView.setImage(crouchingImage);
            // Adjust height if needed, or keep it same but change image
            // duckView.setFitHeight(60); // Example if crouching image is shorter
        } else {
            duckView.setImage(runningImage);
            // duckView.setFitHeight(80);
        }
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
