package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.MainApp;
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
    //private final Image jumpImage;

    // Gravity settings (configurable)
    private double gravityUp;
    private double gravityDown;
    private double jumpForce;

    // Velocity
    private double velocityY = 0;

    private boolean onGround = true;
    private boolean crouching = false;

    // Dimensions
    public static final double NORMAL_WIDTH = 80;
    public static final double NORMAL_HEIGHT = 80;

    public static final double CROUCH_WIDTH = 90;
    public static final double CROUCH_HEIGHT = 50;

    private final double groundLevel; // Y position of the ground line


    public Duck(double startX, double groundLevel) {

        this.groundLevel = groundLevel;

        // Default gravity values (can be changed later)
        this.gravityUp = 1.2;
        this.gravityDown = 0.6;
        this.jumpForce = -16;


        normalImage = new Image(
                getClass().getResource("/images/duck/running.png").toExternalForm()
        );

        crouchImage = new Image(
                getClass().getResource("/images/duck/ducking.png").toExternalForm()
        );

        sprite = new ImageView(normalImage);
        sprite.setFitWidth(NORMAL_WIDTH);
        sprite.setFitHeight(NORMAL_HEIGHT);

        hitbox = new Rectangle(NORMAL_WIDTH, NORMAL_HEIGHT-5);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.RED);

        container = new StackPane(sprite, hitbox);

        StackPane.setAlignment(sprite, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setAlignment(hitbox, javafx.geometry.Pos.BOTTOM_CENTER);

        container.setLayoutX(startX);

        // IMPORTANT:
        container.setLayoutY(groundLevel - NORMAL_HEIGHT);
    }


    // =========================
    // Jump
    // =========================
    public void jump() {
        if (isOnGround()) {
            velocityY = jumpForce;
        }
    }


    // ========================
    // Crouch
    // ========================
    public void setCrouching(boolean state) {

        if (!onGround) return;

        if (state && !crouching) {

            crouching = true;
            sprite.setImage(crouchImage);

            sprite.setFitWidth(CROUCH_WIDTH);
            sprite.setFitHeight(CROUCH_HEIGHT);

            hitbox.setWidth(CROUCH_WIDTH);
            hitbox.setHeight(CROUCH_HEIGHT-10);

        } else if (!state && crouching) {

            crouching = false;
            sprite.setImage(normalImage);

            sprite.setFitWidth(NORMAL_WIDTH);
            sprite.setFitHeight(NORMAL_HEIGHT);

            hitbox.setWidth(NORMAL_WIDTH);
            hitbox.setHeight(NORMAL_HEIGHT-5);
        }
    }

    public void setGravity(double gravityUp, double gravityDown) {
        this.gravityUp = gravityUp;
        this.gravityDown = gravityDown;
    }

    public void setJumpForce(double jumpForce) {
        this.jumpForce = jumpForce;
    }


    // =========================
    // Physics Update
    // =========================
    public void update() {

        if (!isOnGround()) {

            if (velocityY < 0) {
                // Going UP
                velocityY += gravityUp;
            } else {
                // Falling DOWN
                velocityY += gravityDown;
            }
        }

        duckView.setLayoutY(duckView.getLayoutY() + velocityY);

        // Ground collision
        if (duckView.getLayoutY() >= groundLine) {
            duckView.setLayoutY(groundLine);
            velocityY = 0;
        }

        double currentHeight = crouching ? CROUCH_HEIGHT : NORMAL_HEIGHT;

        double groundTop = groundLevel - currentHeight;

        if (container.getLayoutY() >= groundTop) {
            container.setLayoutY(groundTop);
            velocityY = 0;
            onGround = true;
        }
    }


    // =========================
    // Getters
    // =========================
    public StackPane getNode() {
        return container;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
