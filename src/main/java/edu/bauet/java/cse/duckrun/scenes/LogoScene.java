package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LogoScene {

    // ── Timing constants (seconds) ────────────────────────────────────────────
    private static final double BLACK_SCREEN_SEC = 1.5;
    private static final double FADE_IN_SEC  = 1.2;
    private static final double HOLD_SEC     = 2.5;
    private static final double FADE_OUT_SEC = 0.8;

    // How much the logo scales up during the hold (1.0 = no zoom, 1.06 = subtle)
    private static final double ZOOM_FACTOR  = 1.06;

    // Base display size for the logo image (px). Adjust to taste.
    private static final double LOGO_WIDTH   = 480;

    private volatile boolean hasSkipped = false;

    public Scene createScene(Stage stage) {
        // ── Root ──────────────────────────────────────────────────────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");

        // ── Logo image ────────────────────────────────────────────────────────
        Image logo = AssetLoader.getImage("/images/logo/team_logo2.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(LOGO_WIDTH);
        logoView.setPreserveRatio(true);
        logoView.setSmooth(true);

        // Start fully transparent and at normal scale
        logoView.setOpacity(0);
        logoView.setScaleX(1.0);
        logoView.setScaleY(1.0);

        root.getChildren().add(logoView);
        StackPane.setMargin(logoView, new Insets(0, 0, 0, 0));

        Scene scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        // ── Animation sequence (2 s black → logo animation) ──────────────────
        PauseTransition blackScreen = new PauseTransition(Duration.seconds(BLACK_SCREEN_SEC));
        blackScreen.setOnFinished(e -> playLogoAnimation(logoView, stage));
        blackScreen.play();

        return scene;
    }

    private void playLogoAnimation(ImageView logoView, Stage stage) {

        // 1) Fade IN
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(FADE_IN_SEC), logoView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.9);

        // 2) Hold + zoom (runs during the hold period)
        Timeline holdZoom = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(logoView.scaleXProperty(), 1.0),
                        new KeyValue(logoView.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.seconds(HOLD_SEC),
                        new KeyValue(logoView.scaleXProperty(), ZOOM_FACTOR),
                        new KeyValue(logoView.scaleYProperty(), ZOOM_FACTOR))
        );

        // 3) Fade OUT
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(FADE_OUT_SEC), logoView);
        fadeOut.setFromValue(0.9);
        fadeOut.setToValue(0.0);

        // Chain: (fadeIn + holdZoom simultaneously) → fadeOut → story
        holdZoom.setOnFinished(he -> {
            fadeOut.setOnFinished(fe -> skipToStory(stage));
            fadeOut.play();
        });

        fadeIn.play();
        holdZoom.play();
    }

    private void skipToStory(Stage stage) {
        if (hasSkipped) return;
        hasSkipped = true;

        javafx.application.Platform.runLater(() -> {
            StoryScene storyScene = new StoryScene();
            MainApp.switchScene(storyScene.createScene(stage));
        });
    }
}