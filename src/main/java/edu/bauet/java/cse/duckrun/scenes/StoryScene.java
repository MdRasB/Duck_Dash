package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
    private volatile boolean hasNavigated = false; // guard against double-navigation

    public Scene createScene(Stage stage) {

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");

        MediaPlayer mp = null;

        try {
            Media video = AssetLoader.loadVideo("/Story/opening.mp4");

            if (video == null) {
                throw new RuntimeException("Video not found");
            }

            mp = new MediaPlayer(video);
            MediaView mv = new MediaView(mp);

            // FIX 1: Bind dimensions AFTER the player is ready, not before
            MediaPlayer finalMp = mp;
            mp.setOnReady(() -> {
                // Size inside onReady so the MediaView has a valid surface
                mv.setFitWidth(MainApp.WINDOW_WIDTH);
                mv.setFitHeight(MainApp.WINDOW_HEIGHT);
                mv.setPreserveRatio(true);
                mv.setSmooth(true);
                finalMp.play();
            });

            mp.setOnEndOfMedia(() -> navigateToMenu(stage, finalMp));

            // FIX 2: Catch codec/renderer errors explicitly
            mp.setOnError(() -> {
                System.out.println("MediaPlayer error: " + finalMp.getError());
                navigateToMenu(stage, finalMp);
            });

            // FIX 3: Stall watchdog — if video hasn't started within 6 seconds, skip it
            PauseTransition stallWatchdog = new PauseTransition(Duration.seconds(6));
            stallWatchdog.setOnFinished(e -> {
                if (finalMp.getCurrentTime().lessThanOrEqualTo(Duration.millis(200))) {
                    System.out.println("Video stalled at start — skipping intro.");
                    navigateToMenu(stage, finalMp);
                }
            });

            // FIX 4: Also detect mid-video stalls using STALLED status
            finalMp.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                if (newStatus == MediaPlayer.Status.STALLED) {
                    // Give it 4 more seconds to recover, then skip
                    PauseTransition stallRecover = new PauseTransition(Duration.seconds(4));
                    stallRecover.setOnFinished(e -> {
                        if (finalMp.getStatus() == MediaPlayer.Status.STALLED) {
                            System.out.println("Video stalled mid-play — skipping intro.");
                            navigateToMenu(stage, finalMp);
                        }
                    });
                    stallRecover.play();
                }
            });

            stallWatchdog.play();

            root.getChildren().add(0, mv);

        } catch (Exception e) {
            System.out.println("Video engine error: " + e.getMessage() + " — Skipping intro.");
            navigateToMenu(stage, mp);
        }

        // Skip label setup (unchanged)
        skipLabel = new Label("Press SPACE to Skip");
        Font pixelFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12
        );
        if (pixelFont != null) skipLabel.setFont(pixelFont);

        skipLabel.setTextFill(Color.WHITE);
        skipLabel.setStyle("-fx-background-color: rgba(0,0,0,1); -fx-padding:5 10 5 10; -fx-background-radius:5;");
        skipLabel.setVisible(false);

        PauseTransition labelDelay = new PauseTransition(Duration.seconds(3));
        labelDelay.setOnFinished(e -> skipLabel.setVisible(true));
        labelDelay.play();

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
        // FIX 5: Guard against being called multiple times (e.g. stall + SPACE at same time)
        if (hasNavigated) return;
        hasNavigated = true;

        // FIX 6: Always dispatch UI changes on the JavaFX thread
        Platform.runLater(() -> {
            if (mp != null) {
                mp.stop();
                mp.dispose(); // release native resources immediately
            }
            MenuScene menuScene = new MenuScene(stage);
            MainApp.switchScene(menuScene.createScene());
        });
    }
}