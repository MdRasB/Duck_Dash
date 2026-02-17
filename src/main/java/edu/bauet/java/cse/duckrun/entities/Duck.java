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
    public static final double NORMAL_WIDTH = 60;
    public static final double NORMAL_HEIGHT = 60;

    public static final double CROUCH_WIDTH = 60;
    public static final double CROUCH_HEIGHT = 30;

    private final double groundY;

    public Duck(double startX, double startY) {

        groundY = startY;

        normalImage = new Image(
                getClass().getResource("/images/duck/running.png").toExternalForm()
        );

        crouchImage = new Image(
                getClass().getResource("/images/duck/ducking.png").toExternalForm()
        );

        //jumpImage = new Image(
                //getClass().getResource("images/duck/jumping.png").toExternalForm()
        //);

        sprite = new ImageView(normalImage);
        sprite.setFitWidth(80);
        sprite.setFitHeight(80);

        hitbox = new Rectangle(NORMAL_WIDTH, NORMAL_HEIGHT);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.RED); // DEBUG

        container = new StackPane(sprite, hitbox);
        container.setLayoutX(startX);
        container.setLayoutY(startY);
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

            hitbox.setWidth(CROUCH_WIDTH);
            hitbox.setHeight(CROUCH_HEIGHT);

        } else if (!state && crouching) {

            crouching = false;
            sprite.setImage(normalImage);

            hitbox.setWidth(NORMAL_WIDTH);
            hitbox.setHeight(NORMAL_HEIGHT);
        }
    }

    // =========================
    // Physics Update
    // =========================
    public void update() {

        velocityY += GRAVITY;
        container.setLayoutY(container.getLayoutY() + velocityY);

        if (container.getLayoutY() >= groundY) {
            container.setLayoutY(groundY);
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
