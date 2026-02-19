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

    // Physics (NOW configurable)
    private double gravityUp = 0.6;
    private double gravityDown = 1.4;
    private double jumpForce = -15;

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

        StackPane.setAlignment(sprite, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setAlignment(hitbox, javafx.geometry.Pos.BOTTOM_CENTER);

        container.setLayoutX(startX);
        container.setLayoutY(groundLevel - NORMAL_HEIGHT);
    }


    // =========================
    // Jump
    // =========================
    public void jump() {

        if (onGround && !crouching) {
            velocityY = jumpForce;
            onGround = false;
        }
    }

    // =========================
    // Crouch
    // =========================
    public void setCrouching(boolean state) {

        if (!onGround) return;

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
    }


    // =========================
    // Physics Update
    // =========================
    public void update() {

        // Different gravity for up and down
        if (!onGround) {

            if (velocityY < 0) {
                velocityY += gravityUp;      // Going UP
            } else {
                velocityY += gravityDown;    // Falling DOWN
            }
        }

        container.setLayoutY(container.getLayoutY() + velocityY);

        double currentHeight = crouching ? CROUCH_HEIGHT : NORMAL_HEIGHT;
        double groundTop = groundLevel - currentHeight;

        if (container.getLayoutY() >= groundTop) {
            container.setLayoutY(groundTop);
            velocityY = 0;
            onGround = true;
        }
    }


    // =========================
    // Gravity Controls (NEW)
    // =========================
    public void setGravity(double up, double down) {
        this.gravityUp = up;
        this.gravityDown = down;
    }

    public void setJumpForce(double force) {
        this.jumpForce = force;
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
