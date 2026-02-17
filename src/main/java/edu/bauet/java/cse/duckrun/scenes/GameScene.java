package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.Duck;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class GameScene {

    private Pane root;
    private Scene scene;

    private Duck duck;

    // World container (this will move)
    private Pane world;

    private double worldSpeed = 5;

    public GameScene() {
        initialize();
    }

    private void initialize() {

        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #E0F7FA);");

        // World pane (everything inside moves)
        world = new Pane();

        // Simple ground (temporary)
        Rectangle ground = new Rectangle(
                MainApp.WINDOW_WIDTH * 2,
                100,
                Color.DARKGREEN
        );
        ground.setLayoutY(MainApp.WINDOW_HEIGHT - 100);

        world.getChildren().add(ground);

        // Create duck (fixed X position)
        duck = new Duck(200, MainApp.WINDOW_HEIGHT - 150);

        root.getChildren().addAll(world, duck.getNode());

        scene = new Scene(root);

        setupKeyControls();
        startGameLoop();
    }

    private void setupKeyControls() {

        scene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
                System.out.println("Jumping...");
                duck.jump();
            }

            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                System.out.println("Crouching...");
                duck.crouch(true);
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                System.out.println("Crouching Ending");
                duck.crouch(false);
            }
        });
    }

    private void startGameLoop() {

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                // Move world left
                world.setLayoutX(world.getLayoutX() - worldSpeed);

                // Update duck physics
                duck.update();
            }
        };

        gameLoop.start();
    }

    public Scene getScene() {
        return scene;
    }
}
