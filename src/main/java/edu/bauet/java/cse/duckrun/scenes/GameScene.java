package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;

/**
 * GameScene
 * This class handles the main gameplay screen.
 */
public class GameScene {

    private Pane root;
    private Scene scene;

    // Temporary duck representation (we will replace with Duck class later)
    private Rectangle duck;

    // Movement variables
    private double duckSpeed = 5;
    private double jumpForce = -15;
    private double gravity = 0.8;
    private double velocityY = 0;
    private boolean isOnGround = true;

    public GameScene() {
        initialize();
    }

    private void initialize() {

        // Create root pane
        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        // Background color (temporary)
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #E0F7FA);");

        // Create temporary duck
        duck = new Rectangle(60, 60);
        duck.setFill(Color.YELLOW);
        duck.setLayoutX(200);
        duck.setLayoutY(MainApp.WINDOW_HEIGHT - 150);

        root.getChildren().add(duck);

        scene = new Scene(root);

        setupKeyControls();
        startGameLoop();
    }

    private void setupKeyControls() {

        scene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.LEFT) {
                duck.setLayoutX(duck.getLayoutX() - duckSpeed);
            }

            if (event.getCode() == KeyCode.RIGHT) {
                duck.setLayoutX(duck.getLayoutX() + duckSpeed);
            }

            if (event.getCode() == KeyCode.SPACE && isOnGround) {
                velocityY = jumpForce;
                isOnGround = false;
            }
        });
    }

    private void startGameLoop() {

        javafx.animation.AnimationTimer gameLoop = new javafx.animation.AnimationTimer() {

            @Override
            public void handle(long now) {

                // Apply gravity
                velocityY += gravity;
                duck.setLayoutY(duck.getLayoutY() + velocityY);

                // Ground collision
                if (duck.getLayoutY() >= MainApp.WINDOW_HEIGHT - 150) {
                    duck.setLayoutY(MainApp.WINDOW_HEIGHT - 150);
                    velocityY = 0;
                    isOnGround = true;
                }
            }
        };

        gameLoop.start();
    }

    public Scene getScene() {
        return scene;
    }
}
