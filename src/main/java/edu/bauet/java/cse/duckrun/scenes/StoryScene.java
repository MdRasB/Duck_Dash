package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StoryScene {

    private Label skipLabel;

    public Scene createScene(Stage stage) {

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");

        MediaPlayer mp = null;

        try {
            // Load video using assetLoader
            Media video = AssetLoader.loadVideo("/Story/opening.mp4");

            if (video == null) {
                throw new RuntimeException("Video not found");
            }

            mp = new MediaPlayer(video);
            MediaView mv = new MediaView(mp);

            // Scaling and Smoothing
            mv.setFitWidth(MainApp.WINDOW_WIDTH);
            mv.setFitHeight(MainApp.WINDOW_HEIGHT);
            mv.setPreserveRatio(true);
            mv.setSmooth(true);

            root.getChildren().add(0, mv); // Add to the back of the StackPane

            // Standard playback logic
            MediaPlayer finalMp = mp;
            mp.setOnReady(() -> {
                finalMp.play();
            });

            mp.setOnEndOfMedia(() -> navigateToMenu(stage, finalMp));

        } catch (Exception e) {
            System.out.println("Video engine error. Skipping intro.");
            navigateToMenu(stage, mp);
        }

        skipLabel = new Label("Press SPACE to Skip");

        Font pixelFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12
        );

        if (pixelFont != null) {
            skipLabel.setFont(pixelFont);
        }

        skipLabel.setTextFill(Color.WHITE);
        skipLabel.setStyle("-fx-background-color: rgba(0,0,0,1); -fx-padding:5 10 5 10; -fx-background-radius:5;");
        skipLabel.setVisible(false);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> skipLabel.setVisible(true));
        delay.play();

        root.getChildren().add(skipLabel);

        StackPane.setAlignment(skipLabel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(skipLabel, new Insets(0, 20, 20, 0));

        Scene scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        MediaPlayer finalMp = mp;

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                navigateToMenu(stage, finalMp);
            }
        });

        return scene;
    }

    private void navigateToMenu(Stage stage, MediaPlayer mp) {

        if (mp != null) {
            mp.stop();
        }

        MenuScene menuScene = new MenuScene(stage);
        MainApp.switchScene(menuScene.createScene());
    }
}