package edu.bauet.java.cse.duckrun;

import edu.bauet.java.cse.duckrun.scenes.StoryScene;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Entry point of the game...
public class MainApp extends Application {

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        stage.setTitle("Duck Dash");
        stage.setResizable(false);

        AssetLoader.preloadAssets();

        // Show the story scene first
        StoryScene story = new StoryScene();
        Scene storyScene = story.createScene(stage);

        stage.setScene(storyScene);
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
