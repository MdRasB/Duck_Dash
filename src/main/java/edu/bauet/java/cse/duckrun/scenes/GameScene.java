package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.Duck;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

public class GameScene {

    private Pane root;
    private Scene scene;
    private Duck duck;

    public GameScene() {
        initialize();
    }

    private void initialize() {

        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #E0F7FA);");

        duck = new Duck(200, MainApp.WINDOW_HEIGHT - 150);

        root.getChildren().add(duck.getNode());

        scene = new Scene(root);

        setupKeyControls();
        startGameLoop();
    }

    private void setupKeyControls() {

        scene.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.A || event.getCode() ==  KeyCode.LEFT) {
                duck.moveLeft();
            }

            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                duck.moveRight();
            }

            if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.UP) {
                duck.jump();
            }

            if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S){
                duck.knee();
            }
        });
    }

    private void startGameLoop() {

        AnimationTimer gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                duck.update();
            }
        };

        gameLoop.start();
    }

    public Scene getScene() {
        return scene;
    }
}
