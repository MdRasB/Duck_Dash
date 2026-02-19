package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.Duck;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;

public class GameScene {

    private Pane root;
    private Pane world;
    private Scene scene;

    private Duck duck;

    private AnimationTimer gameLoop;

    // World settings
    private static final double WORLD_SPEED = 10;
    private double worldOffset = 0;

    // Background
    private ImageView bg1;
    private ImageView bg2;

    // 🔥 Constructor now accepts background image path
    public GameScene(String backgroundPath) {
        initialize(backgroundPath);
    }

    private void initialize(String backgroundPath) {

        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        world = new Pane();

        createBackground(backgroundPath);
        createGround();
        createPlayer();

        root.getChildren().addAll(bg1, bg2, world, duck.getNode());

        scene = new Scene(root);

        setupControls();
        startGameLoop();
    }

    private void createBackground(String path) {

        var resource = getClass().getResource(path);

        if (resource == null) {
            throw new RuntimeException("Background image not found: " + path);
        }

        Image bgImage = new Image(resource.toExternalForm());

        bg1 = new ImageView(bgImage);
        bg2 = new ImageView(bgImage);

        bg1.setFitWidth(MainApp.WINDOW_WIDTH);
        bg1.setFitHeight(MainApp.WINDOW_HEIGHT);

        bg2.setFitWidth(MainApp.WINDOW_WIDTH);
        bg2.setFitHeight(MainApp.WINDOW_HEIGHT);

        bg1.setLayoutX(0);
        bg2.setLayoutX(MainApp.WINDOW_WIDTH);
    }

    private void createGround() {

        Rectangle ground = new Rectangle(
                MainApp.WINDOW_WIDTH * 5,
                100,
                Color.DARKGREEN
        );

        ground.setLayoutY(MainApp.WINDOW_HEIGHT - 100);
        world.getChildren().add(ground);
    }

    private void createPlayer() {

        double groundLine = MainApp.WINDOW_HEIGHT - 100;

        duck = new Duck(200, groundLine + 15);

        // Gravity tuning
        duck.setGravity(0.6, 1.5);
        duck.setJumpForce(-15);
    }

    private void setupControls() {

        scene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.SPACE
                    || event.getCode() == KeyCode.W
                    || event.getCode() == KeyCode.UP) {

                duck.jump();
            }

            if (event.getCode() == KeyCode.S
                    || event.getCode() == KeyCode.DOWN) {

                duck.setCrouching(true);
            }

            if (event.getCode() == KeyCode.ESCAPE) {
                stop();
                MainApp.switchScene(
                        new MenuScene(MainApp.getPrimaryStage()).createScene()
                );
            }
        });

        scene.setOnKeyReleased(event -> {

            if (event.getCode() == KeyCode.S
                    || event.getCode() == KeyCode.DOWN) {

                duck.setCrouching(false);
            }
        });
    }

    private void startGameLoop() {

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                updateBackground();
                updateWorld();
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

    private void updateWorld() {
        worldOffset -= WORLD_SPEED;
        world.setLayoutX(worldOffset);
    }

    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public Scene getScene() {
        return scene;
    }
}
