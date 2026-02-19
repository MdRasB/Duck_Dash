package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;

public class StoryScene {

    public Scene createScene(Stage stage) {
        // Load image
        Image i = loadImage("/Story/startstory.png");
        ImageView iv = new ImageView(i);
        iv.setFitHeight(MainApp.WINDOW_HEIGHT);
        iv.setFitWidth(MainApp.WINDOW_WIDTH);
        iv.setPreserveRatio(false);

        // Create scene
        StackPane root = new StackPane(iv);
        Scene scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        // Add key event handler
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                // Transition to MenuScene
                MenuScene menuSceneObj = new MenuScene(stage);
                MainApp.switchScene(menuSceneObj.createScene());
            }
        });

        return scene;
    }

    private Image loadImage(String path) {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.err.println("Could not load image: " + path);
            // Return a placeholder to prevent crash
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
        }
        return new Image(is);
    }
}
