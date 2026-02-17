package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.Duck;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
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
    private static final double WORLD_SPEED = 5;
    private double worldOffset = 0;

    public GameScene() {
        initialize();
    }

    private void initialize() {

        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        world = new Pane();

        createBackground();
        createGround();
        createPlayer();

        root.getChildren().addAll(world, duck.getNode());

        scene = new Scene(root);

        setupControls();
        startGameLoop();
    }

    private void createBackground() {
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #E0F7FA);");
    }

    private void createGround() {

        Rectangle ground = new Rectangle(
                MainApp.WINDOW_WIDTH * 2,
                100,
                Color.DARKGREEN
        );

        ground.setLayoutY(MainApp.WINDOW_HEIGHT - 100);
        world.getChildren().add(ground);
    }

    private void createPlayer() {
        duck = new Duck(
                200,
                MainApp.WINDOW_HEIGHT - Duck.NORMAL_HEIGHT - 100
        );
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

                updateWorld();
                duck.update();
            }
        };

        gameLoop.start();
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
