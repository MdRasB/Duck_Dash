package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.animation.PauseTransition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Duck {

    private final Group duckGroup;
    private final ImageView duckView;
    private final ImageView duckShadow;
    private final Rectangle debugHitbox;

    private final Image runningImage;
    private final Image runningMidPointImage;
    private final Image duckingImage;
    private final Image duckingMidPointImage;
    private final Image jumpingImage;
    private final Image normalShadowImage;
    private final Image jumpShadowImage;

    private final double groundLine;

    public static boolean jumping = false;
    private boolean goingUp = false;
    private boolean comingDown = false;

    private double jumpHeight = 250;
    private double jumpSpeed = 15;
    private double fallSpeed = 2.75;

    private double effectDuration = 0.5;
    private double hitIntensity = 3; // Default hit intensity
    private double powerUpIntensity = 3   ; // Default power-up intensity

    private double maxY;
    private boolean crouching = false;

    private int frameCounter = 0;
    private boolean toggleFrame = false;

    private final double DISPLAY_HEIGHT = 90;

    public Duck(double x, double groundLine) {

        this.groundLine = groundLine;

        runningImage = AssetLoader.getImage("/images/duck/running.png");
        runningMidPointImage = AssetLoader.getImage("/images/duck/running_mid_point.png");
        duckingImage = AssetLoader.getImage("/images/duck/ducking.png");
        duckingMidPointImage = AssetLoader.getImage("/images/duck/ducking_mid_point.png");
        jumpingImage = AssetLoader.getImage("/images/duck/jumping.png");

        normalShadowImage = AssetLoader.getImage("/images/shadow/Shadow(normal).png");
        jumpShadowImage = AssetLoader.getImage("/images/shadow/Shadow(small).png");

        duckView = new ImageView(runningImage);
        duckView.setFitHeight(DISPLAY_HEIGHT);
        duckView.setPreserveRatio(true);
        duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);

        duckShadow = new ImageView(normalShadowImage);
        duckShadow.setFitHeight(55);
        duckShadow.setFitWidth(DISPLAY_HEIGHT);
        duckShadow.setPreserveRatio(false);
        duckShadow.setLayoutY(groundLine - DISPLAY_HEIGHT);

        debugHitbox = new Rectangle();
        debugHitbox.setFill(Color.TRANSPARENT);
        debugHitbox.setStroke(Color.TRANSPARENT);
        debugHitbox.setStrokeWidth(0);

        duckGroup = new Group(duckShadow, duckView, debugHitbox);
        duckGroup.setLayoutX(x);
    }

    public void update() {

        duckShadow.setLayoutY(groundLine - DISPLAY_HEIGHT + 40);

        if (jumping) {
            duckShadow.setImage(jumpShadowImage);
            duckShadow.setLayoutY(groundLine - DISPLAY_HEIGHT + 52.5);
            duckShadow.setLayoutX(20);
            duckShadow.setOpacity(0.6);
            duckShadow.setFitHeight(30);
            duckShadow.setFitWidth(30);
        } else {
            duckShadow.setImage(normalShadowImage);
            duckShadow.setOpacity(1.0);
            duckShadow.setFitHeight(60);
            duckShadow.setFitWidth(80);
            duckShadow.setLayoutX(5);
            duckShadow.setOpacity(0.8);
        }

        if (goingUp) {
            duckView.setImage(runningImage);
            duckView.setLayoutY(duckView.getLayoutY() - jumpSpeed);
            if (duckView.getLayoutY() <= maxY) {
                goingUp = false;
                comingDown = true;
                duckView.setImage(jumpingImage);
            }
        } else if (comingDown) {
            duckView.setLayoutY(duckView.getLayoutY() + fallSpeed);
            if (duckView.getLayoutY() >= groundLine - DISPLAY_HEIGHT) {
                duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
                comingDown = false;
                jumping = false;
            }
        }

        animate();
        updateDebugHitbox();
    }

    private void animate() {

        frameCounter++;
        if (frameCounter >= 12) {
            toggleFrame = !toggleFrame;
            frameCounter = 0;
        }

        if (crouching && !jumping) {
            duckView.setImage(toggleFrame ? duckingImage : duckingMidPointImage);
            duckView.setLayoutY(groundLine - DISPLAY_HEIGHT + 20);
        } else if (!jumping) {
            duckView.setImage(toggleFrame ? runningImage : runningMidPointImage);
            duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
        }
    }

    private void updateDebugHitbox() {
        Bounds localBounds = getLocalHitbox();
        debugHitbox.setX(localBounds.getMinX());
        debugHitbox.setY(localBounds.getMinY());
        debugHitbox.setWidth(localBounds.getWidth());
        debugHitbox.setHeight(localBounds.getHeight());
    }

    public void jump() {
        if (!jumping && !crouching) {
            jumping = true;
            goingUp = true;
            maxY = duckView.getLayoutY() - jumpHeight;
        }
    }

    public void setCrouching(boolean crouch) {
        if (!jumping) {
            this.crouching = crouch;
        }
    }

    public void hit() {
        applyEffect(Color.RED, hitIntensity);
    }
    
    public void powerUp() {
        applyEffect(Color.LIMEGREEN, powerUpIntensity);
    }
    
    private void applyEffect(Color color, double intensity) {
        Lighting lighting = new Lighting();
        lighting.setSurfaceScale(0.0);
        lighting.setSpecularConstant(intensity); // Use intensity
        lighting.setDiffuseConstant(intensity);  // Use intensity

        Light.Distant light = new Light.Distant();
        light.setColor(color);
        lighting.setLight(light);
        
        duckView.setEffect(lighting);

        PauseTransition pause = new PauseTransition(Duration.seconds(effectDuration));
        pause.setOnFinished(e -> duckView.setEffect(null)); // Remove effect completely
        pause.play();
    }

    public void resetState() {
        jumping = false;
        crouching = false;
        goingUp = false;
        comingDown = false;
        duckGroup.setLayoutX(200);
        duckView.setLayoutY(groundLine - DISPLAY_HEIGHT);
        duckView.setEffect(null);
    }

    public Node getNode() {
        return duckGroup;
    }

    private Bounds getLocalHitbox() {
        Bounds b = duckView.getBoundsInParent();

        double shrinkX = b.getWidth() * 0.3;
        double shrinkY;

        if (jumping) {
            shrinkY = b.getHeight() * 0.5;
        } else {
            shrinkY = b.getHeight() * 0.3;
        }

        return new BoundingBox(
                b.getMinX() + shrinkX,
                b.getMinY() + shrinkY,
                b.getWidth() - shrinkX * 2,
                b.getHeight() - shrinkY
        );
    }

    public Bounds getHitBox() {
        return debugHitbox.localToScene(debugHitbox.getBoundsInLocal());
    }

    public void setEffectDuration(double seconds) {
        this.effectDuration = seconds;
    }
    
    public void setHitIntensity(double intensity) {
        this.hitIntensity = intensity;
    }
    
    public void setPowerUpIntensity(double intensity) {
        this.powerUpIntensity = intensity;
    }
}
