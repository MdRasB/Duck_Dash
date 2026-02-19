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

    // Background
    private ImageView bg1;
    private ImageView bg2;

    private static final double WORLD_SPEED = 3;

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

        bg1 = new ImageView(bgImage);
        bg2 = new ImageView(bgImage);

        bg1.setPreserveRatio(false);
        bg2.setPreserveRatio(false);

        bg1.setFitWidth(1280*2.5);
        bg1.setFitHeight(720);

        bg2.setFitWidth(1280*2.5);
        bg2.setFitHeight(720);

        bg1.setLayoutX(0);
        bg2.setLayoutX(MainApp.WINDOW_WIDTH);
    }

    private void createPlayer() {

        double groundLine = MainApp.WINDOW_HEIGHT - 180;

        duck = new Duck(200, groundLine);

        // 🔥 Example gravity tuning (change per level)
        duck.setGravity(0.6, 1.5);
        duck.setJumpForce(-15);
    }

    private void setupControls() {

        scene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.SPACE ||
                    event.getCode() == KeyCode.W ||
                    event.getCode() == KeyCode.UP) {

                duck.jump();
            }

            if (event.getCode() == KeyCode.S ||
                    event.getCode() == KeyCode.DOWN) {

                duck.setCrouching(true);
            }
        });

        scene.setOnKeyReleased(event -> {

            if (event.getCode() == KeyCode.S ||
                    event.getCode() == KeyCode.DOWN) {

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

    private void updateBackground() {

        bg1.setLayoutX(bg1.getLayoutX() - WORLD_SPEED);
        bg2.setLayoutX(bg2.getLayoutX() - WORLD_SPEED);

        if (bg1.getLayoutX() + MainApp.WINDOW_WIDTH <= 0) {
            bg1.setLayoutX(bg2.getLayoutX() + MainApp.WINDOW_WIDTH);
        }

        if (bg2.getLayoutX() + MainApp.WINDOW_WIDTH <= 0) {
            bg2.setLayoutX(bg1.getLayoutX() + MainApp.WINDOW_WIDTH);
        }
    }

    public Scene getScene() {
        return scene;
    }
}
