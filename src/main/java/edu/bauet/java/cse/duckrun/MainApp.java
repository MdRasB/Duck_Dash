package edu.bauet.java.cse.duckrun;

import edu.bauet.java.cse.duckrun.scenes.MenuScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Entry point of the game...
public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        stage.setTitle("Duck Dash");
        stage.setResizable(false);


        // Create the instance of your MenuScene
        MenuScene menuSceneObj = new MenuScene(stage);
        //build StackPane
        Scene scene = menuSceneObj.createScene();

        //show the specific scene
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Allows scene switching later (Menu, Game, GameOver, etc.)
     * No Scene at this point
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
