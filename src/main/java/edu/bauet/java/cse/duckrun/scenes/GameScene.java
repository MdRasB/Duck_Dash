package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.Duck;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

public class GameScene {

    private Pane root;
    private Scene scene;
    private Duck duck;
    private AnimationTimer gameLoop;

    private ImageView bg1;
    private ImageView bg2;

    // This must match your actual image width (1280 * 2 = 2560)
    private double bgImageWidth;
    private static final double WORLD_SPEED = 5;

    public GameScene(String backgroundPath) {
        initialize(backgroundPath);
    }

    private void initialize(String backgroundPath) {
        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        createBackground(backgroundPath);
        createPlayer();

        root.getChildren().addAll(bg1, bg2, duck.getNode());
        scene = new Scene(root);

        setupControls();
        startGameLoop();
    }

    private void createBackground(String path) {
        var resource = getClass().getResource(path);
        if (resource == null) {
            throw new RuntimeException("Background not found: " + path);
        }

        Image bgImage = new Image(resource.toExternalForm());

        // 1. Get the actual width (2560)
        bgImageWidth = bgImage.getWidth();

        bg1 = new ImageView(bgImage);
        bg2 = new ImageView(bgImage);

        // 2. ONLY set height. Do NOT set FitWidth to WINDOW_WIDTH.
        // Let the width stay at 2560 so the image isn't squashed.
        bg1.setFitHeight(MainApp.WINDOW_HEIGHT);
        bg1.setPreserveRatio(true);

        bg2.setFitHeight(MainApp.WINDOW_HEIGHT);
        bg2.setPreserveRatio(true);

        // 3. Position the second image at the end of the first one (2560px away)
        bg1.setLayoutX(0);
        bg2.setLayoutX(bgImageWidth);
    }

    private void updateBackground() {
        // Move both images
        bg1.setLayoutX(bg1.getLayoutX() - WORLD_SPEED);
        bg2.setLayoutX(bg2.getLayoutX() - WORLD_SPEED);

        // 4. Reset logic using bgImageWidth (2560) instead of Window Width (1280)
        if (bg1.getLayoutX() + bgImageWidth <= 0) {
            bg1.setLayoutX(bg2.getLayoutX() + bgImageWidth);
        }

        if (bg2.getLayoutX() + bgImageWidth <= 0) {
            bg2.setLayoutX(bg1.getLayoutX() + bgImageWidth);
        }
    }

    // ... (rest of your methods: createPlayer, setupControls, startGameLoop, etc.)
    private void createPlayer() {
        double groundLine = MainApp.WINDOW_HEIGHT - 120;
        duck = new Duck(200, groundLine);
    }

    private void setupControls() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) {
                duck.jump();
            }
            if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.C) {
                duck.setCrouching(true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.C) {
                duck.setCrouching(false);
            }
        });
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
                duck.update();
            }
        };
        gameLoop.start();
    }

    public Scene getScene() {
        return scene;
    }
}