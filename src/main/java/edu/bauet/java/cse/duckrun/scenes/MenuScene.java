package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class MenuScene {

    private Stage stage;
    private boolean isMusicOn = true; // Default state
    private StackPane root;
    private VBox settingsBox;
    private Button musicToggle;

    public MenuScene(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        //menu options text font
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 14);
        if (pixelFont != null) {
            System.out.println("Font loaded: " + pixelFont.getFamily());
        } else {
            System.out.println("Font file not found at path!");
        }

        root = new StackPane(); //root layout

        //background image
        Image bgImage = new Image(getClass().getResourceAsStream("/images/ui/menu/menu_bg.png"));
        ImageView background = new ImageView(bgImage);

        //background size
        background.setFitWidth(MainApp.WINDOW_WIDTH);
        background.setFitHeight(MainApp.WINDOW_HEIGHT);
        background.setPreserveRatio(false);

        //title
        Image gameTitle = new Image(getClass().getResourceAsStream("/images/ui/menu/title2.png"));
        ImageView titleView = new ImageView(gameTitle);

        titleView.setFitWidth(472);
        titleView.setPreserveRatio(true);
        VBox.setMargin(titleView, new javafx.geometry.Insets(-175, 0, 0, -105));

        //buttons
        Button btnNewGame = createMenuButton("New Game");
        Button btnLevels = createMenuButton("Levels");
        Button btnScore = createMenuButton("High Score");
        Button btnSettings = createMenuButton("Settings");
        Button btnExit = createMenuButton("Exit");

        //button action
        btnExit.setOnAction(e -> stage.close());
        btnNewGame.setOnAction(e -> {
            GameScene gameScene = new GameScene();
            MainApp.switchScene(gameScene.getScene());
        });

        // Settings button action
        btnSettings.setOnAction(e -> toggleSettingsBox());

        //organize buttons
        VBox menuBox = new VBox(10); //spacing between buttons
        menuBox.getChildren().addAll(titleView, btnNewGame, btnLevels, btnScore, btnSettings, btnExit);

        //alignment of buttons
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setStyle("-fx-padding: 80 0 0 160;");

        // Create settings box (initially hidden)
        createSettingsBox();

        //final assembly
        root.getChildren().addAll(background, menuBox);

        // Add settings box to root (will be hidden initially)
        if (settingsBox != null) {
            root.getChildren().add(settingsBox);
            settingsBox.setVisible(false);
        }

        //create scene and link CSS
        Scene scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/main_menu.css").toExternalForm());

        return scene;
    }

    private void createSettingsBox() {
        // Create main settings container
        settingsBox = new VBox(15);
        settingsBox.setAlignment(Pos.TOP_CENTER);
        settingsBox.getStyleClass().add("settings-box");

        // Position the settings box in the center right area
        StackPane.setAlignment(settingsBox, Pos.CENTER_RIGHT);
        StackPane.setMargin(settingsBox, new Insets(0, 200, 0, 0));

        // Settings title
        Label titleLabel = new Label("SETTINGS");
        titleLabel.getStyleClass().add("settings-title");

        // Music control
        HBox musicBox = new HBox(20);
        musicBox.setAlignment(Pos.CENTER_LEFT);

        Label musicLabel = new Label("Music :");
        musicLabel.getStyleClass().add("settings-label");

        // Music toggle button
        musicToggle = new Button("ON");
        musicToggle.getStyleClass().addAll("music-toggle", "music-on");

        // Toggle functionality
        musicToggle.setOnAction(e -> {
            isMusicOn = !isMusicOn;
            updateMusicToggleStyle();
        });

        musicBox.getChildren().addAll(musicLabel, musicToggle);

        // Close button (small X in corner)
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("settings-close");

        closeButton.setOnAction(e -> settingsBox.setVisible(false));

        // Create a top bar for title and close button
        HBox topBar = new HBox(150);
        topBar.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(titleLabel, closeButton);

        // Add all elements to settings box
        settingsBox.getChildren().addAll(topBar, musicBox);
    }

    private void updateMusicToggleStyle() {
        musicToggle.getStyleClass().removeAll("music-on", "music-off");
        if (isMusicOn) {
            musicToggle.getStyleClass().add("music-on");
            musicToggle.setText("ON");
        } else {
            musicToggle.getStyleClass().add("music-off");
            musicToggle.setText("OFF");
        }
    }

    private void toggleSettingsBox() {
        if (settingsBox != null) {
            settingsBox.setVisible(!settingsBox.isVisible());
        }
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        return btn;
    }
}