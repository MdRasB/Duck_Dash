package edu.bauet.java.cse.duckrun;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Entry point of the game...
public class MainApp extends Application {

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        //Failling to load image in the windows javafx terminal
        stage.setTitle("DuckRun 🦆 - Reach the Class!");
        stage.setResizable(false);

        // Temporary root until scenes are implemented
        // MainScene is not available right now
        StackPane root = new StackPane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // No scene due to the unavailability of Main Scene
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Allows scene switching later (Menu, Game, GameOver, etc.)
     */
    public static void switchScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
