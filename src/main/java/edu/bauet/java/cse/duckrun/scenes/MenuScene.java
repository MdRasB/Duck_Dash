package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;

import edu.bauet.java.cse.duckrun.levels.Level1;

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
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.GaussianBlur;

public class MenuScene {

    private Stage stage;
    private boolean isMusicOn = true; // Default state
    private StackPane root;
    private VBox settingsBox;
    private Button musicToggle;
    private ImageView background;
    private VBox menuBox;
    private Rectangle overlay;

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
        background = new ImageView(bgImage);

        //background size
        background.setFitWidth(MainApp.WINDOW_WIDTH);
        background.setFitHeight(MainApp.WINDOW_HEIGHT);
        background.setPreserveRatio(false);

        // Create overlay for dimming effect (initially transparent)
        overlay = new Rectangle(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        overlay.setStyle("-fx-fill: rgba(0, 0, 0, 0.5);");
        overlay.setVisible(false);

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

            Level1 level1 = new Level1();
            MainApp.switchScene(level1.createLevel());

        });

        // Settings button action
        btnSettings.setOnAction(e -> toggleSettingsBox());

        //organize buttons
        menuBox = new VBox(10); //spacing between buttons
        menuBox.getChildren().addAll(titleView, btnNewGame, btnLevels, btnScore, btnSettings, btnExit);

        //alignment of buttons
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setStyle("-fx-padding: 80 0 0 160;");

        // Create settings box (initially hidden)
        createSettingsBox();

        //final assembly
        root.getChildren().addAll(background, overlay, menuBox);

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
        settingsBox = new VBox();
        settingsBox.setAlignment(Pos.TOP_CENTER);
        settingsBox.getStyleClass().add("settings-box");
        settingsBox.setPrefSize(869, 469);
        settingsBox.setMaxSize(869, 469);

        // Position the settings box in the center of the screen
        StackPane.setAlignment(settingsBox, Pos.CENTER);

        // Settings title - at top center
        Label titleLabel = new Label("SETTINGS");
        titleLabel.getStyleClass().add("settings-title");
        VBox.setMargin(titleLabel, new Insets(30, 0, 0, 0));

        // Music control
        HBox musicBox = new HBox(30);
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

        // Close button (small X in corner) - positioned absolutely via CSS
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("settings-close");
        closeButton.setOnAction(e -> toggleSettingsBox());

        // Create a container for the close button to position it absolutely
        StackPane closeButtonContainer = new StackPane(closeButton);
        closeButtonContainer.setAlignment(Pos.TOP_RIGHT);
        closeButtonContainer.setPadding(new Insets(20, 30, 0, 0));

        // Main content area (for music control)
        VBox contentBox = new VBox(40);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setPadding(new Insets(80, 0, 0, 80));
        contentBox.getChildren().add(musicBox);

        // Use a StackPane to layer title and close button
        StackPane titleArea = new StackPane();
        titleArea.setAlignment(Pos.TOP_CENTER);
        titleArea.getChildren().addAll(titleLabel, closeButtonContainer);

        settingsBox.getChildren().addAll(titleArea, contentBox);
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
            boolean isVisible = !settingsBox.isVisible();
            settingsBox.setVisible(isVisible);
            overlay.setVisible(isVisible);

            // Optional: Disable menu buttons when settings is open
            menuBox.setDisable(isVisible);
            menuBox.setOpacity(isVisible ? 0.3 : 1.0);

            //bg blur when settings box on
            if (isVisible) {
                background.setEffect(new GaussianBlur(10));
            } else {
                background.setEffect(null);
            }
        }
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        return btn;
    }
}