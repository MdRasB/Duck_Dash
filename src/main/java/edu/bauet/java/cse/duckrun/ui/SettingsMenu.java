package edu.bauet.java.cse.duckrun.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SettingsMenu extends StackPane {

    private Button musicToggle;
    private boolean isMusicOn = true;
    private Runnable onClose;

    public SettingsMenu(Runnable onCloseAction) {
        this.onClose = onCloseAction;
        initialize();
    }

    private void initialize() {
        this.getStyleClass().add("settings-box");
        this.setPrefSize(925, 546);
        this.setMaxSize(925, 546);

        // Load CSS specifically for settings
        this.getStylesheets().add(getClass().getResource("/styles/settings_style.css").toExternalForm());

        // Frame Image
        Image frameImg = new Image(getClass().getResourceAsStream("/images/pause_menu/settings_menu_frame.png"));
        ImageView frameView = new ImageView(frameImg);

        // Content Layout
        VBox contentLayout = new VBox(20);
        contentLayout.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("SETTINGS");
        titleLabel.getStyleClass().add("settings-title");
        VBox.setMargin(titleLabel, new Insets(60, 0, 0, 0));

        // Music Control
        HBox musicBox = new HBox(30);
        musicBox.setAlignment(Pos.CENTER);

        Label musicLabel = new Label("Music :");
        musicLabel.getStyleClass().add("settings-label");

        musicToggle = new Button("ON");
        musicToggle.getStyleClass().addAll("music-toggle", "music-on");
        musicToggle.setOnAction(e -> toggleMusic());

        musicBox.getChildren().addAll(musicLabel, musicToggle);
        VBox.setMargin(musicBox, new Insets(80, 0, 0, 0));

        contentLayout.getChildren().addAll(titleLabel, musicBox);

        // Close Button
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("settings-close");
        closeButton.setOnAction(e -> onClose.run());

        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(45, 65, 0, 0));

        this.getChildren().addAll(frameView, contentLayout, closeButton);
    }

    private void toggleMusic() {
        isMusicOn = !isMusicOn;
        musicToggle.getStyleClass().removeAll("music-on", "music-off");
        if (isMusicOn) {
            musicToggle.getStyleClass().add("music-on");
            musicToggle.setText("ON");
        } else {
            musicToggle.getStyleClass().add("music-off");
            musicToggle.setText("OFF");
        }
    }
}