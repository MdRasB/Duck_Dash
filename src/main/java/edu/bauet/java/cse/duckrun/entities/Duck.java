package edu.bauet.java.cse.duckrun.entities;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Duck {

    private ImageView duckView;
    private Image runningImage;
    private Image crouchingImage;

    private double groundLine;

    // Jump system (constant speed, no acceleration)
    private boolean jumping = false;
    private boolean goingUp = false;
    private boolean comingDown = false;

    private double jumpHeight = 200;
    private double jumpSpeed = 20;
    private double fallSpeed = 4;

    private double maxY;

    private boolean crouching = false;

    // SAME visual height always (no distortion)
    private final double DISPLAY_HEIGHT = 80;

    public Duck(double x, double groundLine) {

        this.groundLine = groundLine;

        runningImage = new Image(
                getClass().getResource("/images/duck/running.png").toExternalForm()
        );

        crouchingImage = new Image(
                getClass().getResource("/images/duck/ducking.png").toExternalForm()
        );

        duckView = new ImageView(runningImage);
        duckView.setFitHeight(DISPLAY_HEIGHT);
        duckView.setPreserveRatio(true);
        duckView.setLayoutX(x);


        // Lock feet to ground
        duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
    }

    public void update() {

        if (goingUp) {

            duckView.setLayoutY(duckView.getLayoutY() - jumpSpeed);

            if (duckView.getLayoutY() <= maxY) {
                goingUp = false;
                comingDown = true;
            }
        }

        else if (comingDown) {

            duckView.setLayoutY(duckView.getLayoutY() + fallSpeed);

            if (duckView.getLayoutY() >= groundLine - DISPLAY_HEIGHT) {
                duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
                comingDown = false;
                jumping = false;
            }
        }
    }

    public void jump() {

        if (!jumping) {

            jumping = true;
            goingUp = true;

            maxY = duckView.getLayoutY() - jumpHeight;
        }
    }

    public void setCrouching(boolean crouch) {

        if (this.crouching == crouch) return;

        this.crouching = crouch;

        if (crouch) {
            duckView.setImage(crouchingImage);
        } else {
            duckView.setImage(runningImage);
        }

        // Keep same height always
        duckView.setFitHeight(DISPLAY_HEIGHT);
        duckView.setPreserveRatio(true);

        // Always force bottom alignment
        duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
    }

    // ---- TUNING ----

    public void setJumpHeight(double height) {
        this.jumpHeight = height;
    }

    public void setJumpSpeed(double speed) {
        this.jumpSpeed = speed;
    }

    public void setFallSpeed(double speed) {
        this.fallSpeed = speed;
    }

    public Node getNode() {
        return duckView;
    }
}
