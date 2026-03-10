package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StoryScene {

    private Label skipLabel;

    public Scene createScene(Stage stage) {

        MediaPlayer mp;

        try {

            Media video = AssetLoader.loadVideo("/Story/opening_fixed.mp4");

            mp = new MediaPlayer(video);

            mp.setOnError(() -> {
                System.out.println("Media error: " + mp.getError());
                navigateToMenu(stage, mp);
            });

        } catch (Exception e) {

            System.out.println("Video failed to load: " + e.getMessage());

            // Immediately go to Menu
            MenuScene menuScene = new MenuScene(stage);
            return menuScene.createScene();
        }

        mp.setAutoPlay(true);

        MediaView mv = new MediaView(mp);
        mv.setFitWidth(MainApp.WINDOW_WIDTH);
        mv.setPreserveRatio(true);

        skipLabel = new Label("Press SPACE to Skip");

        Font pixelFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"),
                12
        );

        if (pixelFont != null) {
            skipLabel.setFont(pixelFont);
        }

        skipLabel.setTextFill(Color.WHITE);
        skipLabel.setStyle(
                "-fx-background-color: rgba(0,0,0,1);" +
                        "-fx-padding: 5 10 5 10;" +
                        "-fx-background-radius: 5;"
        );

        skipLabel.setVisible(false);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> skipLabel.setVisible(true));
        delay.play();

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(mv, skipLabel);

        StackPane.setAlignment(skipLabel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(skipLabel, new Insets(0,20,20,0));

        mp.setOnEndOfMedia(() -> navigateToMenu(stage, mp));

        Scene scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                navigateToMenu(stage, mp);
            }
        });

        return scene;
    }

    private void navigateToMenu(Stage stage, MediaPlayer mp) {
        mp.stop();
        MenuScene menuScene = new MenuScene(stage);
        MainApp.switchScene(menuScene.createScene());
    }
}
