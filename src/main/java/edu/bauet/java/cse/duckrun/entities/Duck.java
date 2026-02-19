package edu.bauet.java.cse.duckrun.entities;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Duck {

    private StackPane container;
    private ImageView sprite;
    private Rectangle hitbox;

    // Images
    private final Image normalImage;
    private final Image crouchImage;

    // --- UPDATED: Renamed physics variables for clarity ---
    private double jumpGravity = 0.6; // Gravity applied when moving UP (weaker for higher jump)
    private double fallGravity = 1.4; // Gravity applied when moving DOWN (stronger for faster fall)
    private double jumpForce = -15;   // Initial upward velocity

    private double velocityY = 0;
    private boolean onGround = true;
    private boolean crouching = false;

    // Dimensions
    public static final double NORMAL_WIDTH = 80;
    public static final double NORMAL_HEIGHT = 80;

    public static final double CROUCH_WIDTH = 90;
    public static final double CROUCH_HEIGHT = 50;

    private final double groundLevel;


    public Duck(double startX, double groundLevel) {

        this.groundLevel = groundLevel;

        var runRes = getClass().getResource("/images/duck/running.png");
        var crouchRes = getClass().getResource("/images/duck/ducking.png");

        if (runRes == null || crouchRes == null) {
            throw new RuntimeException("Duck images not found. Check /images/duck/ folder.");
        }

        normalImage = new Image(runRes.toExternalForm());
        crouchImage = new Image(crouchRes.toExternalForm());

        sprite = new ImageView(normalImage);
        sprite.setFitWidth(NORMAL_WIDTH);
        sprite.setFitHeight(NORMAL_HEIGHT);

        hitbox = new Rectangle(NORMAL_WIDTH, NORMAL_HEIGHT - 5);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.RED);

        container = new StackPane(sprite, hitbox);

        StackPane.setAlignment(sprite, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(hitbox, Pos.BOTTOM_CENTER);

        container.setLayoutX(startX);
        container.setLayoutY(groundLevel - NORMAL_HEIGHT);
    }


    // =======================================
    // Jump
    // =======================================
    public void jump() {
        // Can only jump if on the ground and not crouching
        if (onGround && !crouching) {
            velocityY = jumpForce; // Apply initial upward force
            onGround = false;
        }
    }

    // =======================================
    // Crouch
    // =======================================
    public void setCrouching(boolean state) {
        if (!onGround) return;

        double previousHeight = crouching ? CROUCH_HEIGHT : NORMAL_HEIGHT;

        if (state && !crouching) {
            crouching = true;
            sprite.setImage(crouchImage);

            sprite.setFitWidth(CROUCH_WIDTH);
            sprite.setFitHeight(CROUCH_HEIGHT);

            hitbox.setWidth(CROUCH_WIDTH);
            hitbox.setHeight(CROUCH_HEIGHT - 10);

        } else if (!state && crouching) {
            crouching = false;
            sprite.setImage(normalImage);

            sprite.setFitWidth(NORMAL_WIDTH);
            sprite.setFitHeight(NORMAL_HEIGHT);

            hitbox.setWidth(NORMAL_WIDTH);
            hitbox.setHeight(NORMAL_HEIGHT - 5);
        }

        // Adjust container Y to keep bottom on ground
        double newHeight = crouching ? CROUCH_HEIGHT : NORMAL_HEIGHT;
        container.setLayoutY(groundLevel - newHeight);
    }



    // =======================================
    // Physics Update
    // =======================================
    public void update() {

        // Apply gravity only when in the air
        if (!onGround) {

            // --- UPDATED: Use new variable names for clarity ---
            if (velocityY < 0) {
                // Duck is moving UP
                velocityY += jumpGravity;
            } else {
                // Duck is moving DOWN
                velocityY += fallGravity;
            }
        }

        // Apply velocity to position
        container.setLayoutY(container.getLayoutY() + velocityY);

        // Check for ground collision
        double currentHeight = crouching ? CROUCH_HEIGHT : NORMAL_HEIGHT;
        double groundTop = groundLevel - currentHeight;

        if (container.getLayoutY() >= groundTop) {
            container.setLayoutY(groundTop);
            velocityY = 0;
            onGround = true;
        }
    }


    // =======================================
    // Gravity Controls
    // =======================================
    public void setGravity(double jumpGravity, double fallGravity) {
        this.jumpGravity = jumpGravity;
        this.fallGravity = fallGravity;
    }

    public void setJumpForce(double force) {
        this.jumpForce = force;
    }


    // =======================================
    // Getters
    // =======================================
    public StackPane getNode() {
        return container;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
