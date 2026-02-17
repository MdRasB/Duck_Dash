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

    // Physics
    private static final double GRAVITY = 0.8;
    private static final double JUMP_FORCE = -15;

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

        if (onGround && !crouching) {
            velocityY = JUMP_FORCE;
            onGround = false;
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


    // =========================
    // Physics Update
    // =========================
    public void update() {

        velocityY += GRAVITY;
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
    // Getters
    // =========================
    public StackPane getNode() {
        return container;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
