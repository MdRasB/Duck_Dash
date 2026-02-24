package edu.bauet.java.cse.duckrun.entities;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Duck {

    private ImageView duckView;
    private Image runningImage;
    private Image crouchingImage;

    private double groundLine;

    // Jump system (constant speed, no acceleration)
    private boolean jumping = false;
    private boolean goingUp = false;
    private boolean comingDown = false;

    private double jumpHeight = 250;
    private double jumpSpeed= 15 ;
    private double fallSpeed = 3;

    private double maxY;

    private boolean crouching = false;

    // SAME visual height always (no distortion)
    private final double DISPLAY_HEIGHT = 90;

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
        } else if (comingDown) {
            duckView.setLayoutY(duckView.getLayoutY() + fallSpeed);

            if (duckView.getLayoutY() >= groundLine - DISPLAY_HEIGHT) {
                duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
                comingDown = false;
                jumping = false;
            }
        }
    }

    public void jump() {
        if (!jumping && !comingDown) {
            jumping = true;
            goingUp = true;
            maxY = duckView.getLayoutY() - jumpHeight;
        }
    }

    public void forceLand() {
        // Force the duck to land immediately (for debug purposes)
        goingUp = false;
        comingDown = false;
        jumping = false;
        duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
    }

    public void setCrouching(boolean crouch) {
        this.crouching = crouch;
        if (crouch) {
            duckView.setImage(crouchingImage);
            // Move down slightly when crouching
            duckView.setLayoutY(groundLine - duckView.getFitHeight() + 15);
        } else {
            duckView.setImage(runningImage);
            // Reset to normal ground line
            duckView.setLayoutY(groundLine - duckView.getFitHeight());
        }
    }

    // ---- GETTERS (for debug) ----

    public double getX() {
        return duckView.getLayoutX();
    }

    public double getY() {
        return duckView.getLayoutY();
    }

    public boolean isJumping() {
        return jumping;
    }

    public boolean isGoingUp() {
        return goingUp;
    }

    public boolean isComingDown() {
        return comingDown;
    }

    public boolean isCrouching() {
        return crouching;
    }

    public double getJumpHeight() {
        return jumpHeight;
    }

    public double getJumpSpeed() {
        return jumpSpeed;
    }

    public double getFallSpeed() {
        return fallSpeed;
    }

    // ---- SETTERS (for tuning) ----

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