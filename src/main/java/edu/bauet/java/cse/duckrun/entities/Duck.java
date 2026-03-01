package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Duck {

    private ImageView duckView;
    private Image runningImage;
    private Image runningMidPointImage;
    private Image duckingImage;
    private Image duckingMidPointImage;
    private Image jumpingImage;

    private double groundLine;

    // Jump system (constant speed, no acceleration)
    private boolean jumping = false;
    private boolean goingUp = false;
    private boolean comingDown = false;

    private double jumpHeight = 250;
    private double jumpSpeed= 15 ;
    private double fallSpeed = 3;
    
    // Configurable size for jumping image
    private double jumpImageWidth = 100;
    private double jumpImageHeight = 100;

    private double maxY;

    private boolean crouching = false;
    
    // Animation
    private int frameCounter = 0;
    private boolean toggleFrame = false;

    // SAME visual height always (no distortion)
    private final double DISPLAY_HEIGHT = 90;

    public Duck(double x, double groundLine) {

        this.groundLine = groundLine;

        runningImage = AssetLoader.getImage("/images/duck/running.png");
        runningMidPointImage = AssetLoader.getImage("/images/duck/running_mid_point.png");
        duckingImage = AssetLoader.getImage("/images/duck/ducking.png");
        duckingMidPointImage = AssetLoader.getImage("/images/duck/ducking_mid_point.png");
        jumpingImage = AssetLoader.getImage("/images/duck/jumping.png");

        duckView = new ImageView(runningImage);
        duckView.setFitHeight(DISPLAY_HEIGHT);
        duckView.setPreserveRatio(true);
        duckView.setLayoutX(x);

        // Lock feet to ground
        duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
    }

    public void update() {
        // Handle jumping physics
        if (goingUp) {
            duckView.setLayoutY(duckView.getLayoutY() - jumpSpeed);

            if (duckView.getLayoutY() <= maxY) {
                goingUp = false;
                comingDown = true;
                // Switch to falling image and apply custom size
                duckView.setImage(jumpingImage);
                duckView.setFitWidth(jumpImageWidth);
                duckView.setFitHeight(jumpImageHeight);
            }
        } else if (comingDown) {
            duckView.setLayoutY(duckView.getLayoutY() + fallSpeed);

            if (duckView.getLayoutY() >= groundLine - DISPLAY_HEIGHT) {
                duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
                comingDown = false;
                jumping = false;
                // Reset to default size
                duckView.setFitWidth(0); // Reset to use preserve ratio
                duckView.setFitHeight(DISPLAY_HEIGHT);
            }
        }
        
        // Handle animation
        animate();
    }
    
    private void animate() {
        frameCounter++;
        if (frameCounter >= 13) { // Animation speed
            toggleFrame = !toggleFrame;
            frameCounter = 0;
        }
        
        if (crouching) {
            duckView.setImage(toggleFrame ? duckingImage : duckingMidPointImage);
            // Adjust Y position for crouching if needed
            duckView.setLayoutY(groundLine - duckView.getFitHeight() + 15);
        } else if (!jumping) {
            // Animate running only when on the ground
            duckView.setImage(toggleFrame ? runningImage : runningMidPointImage);
            // Reset to normal ground line
            duckView.setLayoutY(groundLine - duckView.getFitHeight());
        }
    }

    public void jump() {
        if (!jumping && !comingDown) {
            jumping = true;
            goingUp = true;
            maxY = duckView.getLayoutY() - jumpHeight;
            // You might want a specific "going up" image here too
            // For now, it will use the last running frame until it starts falling
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
    
    public void setJumpImageSize(double width, double height) {
        this.jumpImageWidth = width;
        this.jumpImageHeight = height;
    }

    //Collision Behavior:::
    //Hitbox updates:::
    public javafx.geometry.Bounds getHitBox() {

        javafx.geometry.Bounds bounds = duckView.getBoundsInParent();

        double shrinkX = bounds.getWidth() * 0.2;
        double shrinkY = bounds.getHeight() * 0.15;

        return new javafx.geometry.BoundingBox(
                bounds.getMinX() + shrinkX,
                bounds.getMinY() + shrinkY,
                bounds.getWidth() - shrinkX * 2,
                bounds.getHeight() - shrinkY * 2
        );
    }

    public Node getNode() {
        return duckView;
    }
}
