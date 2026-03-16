package edu.bauet.java.cse.duckrun.ui;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.levels.Level1;
import edu.bauet.java.cse.duckrun.levels.Level2;
import edu.bauet.java.cse.duckrun.levels.Level3;
import edu.bauet.java.cse.duckrun.scenes.GameScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LevelMenu extends StackPane {
    private Runnable onClose;
    private Runnable onEndless;

    public LevelMenu(Runnable onCloseAction) {
        this(onCloseAction, null);
    }

    public LevelMenu(Runnable onCloseAction, Runnable onEndlessAction) {
        this.onClose   = onCloseAction;
        this.onEndless = onEndlessAction;
        initialize();
    }

    private void initialize() {
        // Initially hidden
        this.setVisible(false);
        this.getStyleClass().add("level-box");

        this.setPrefSize(925, 546);
        this.setMaxSize(925, 546);

        // Link with CSS file
        this.getStylesheets().add(getClass().getResource("/styles/level_menu.css").toExternalForm());

        // Frame image
        Image frameImg = new Image(getClass().getResourceAsStream("/images/pause_menu/settings_menu_frame.png"));
        ImageView frameView = new ImageView(frameImg);

        // --- Content Layout ---
        VBox contentLayout = new VBox(20);
        contentLayout.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("LEVELS");
        titleLabel.getStyleClass().add("level-title");
        VBox.setMargin(titleLabel, new Insets(55, 0, 20, 0));

        // --- Level Buttons Column ---
        VBox levelButtonsBox = new VBox(16);
        levelButtonsBox.setAlignment(Pos.CENTER);

        Button btnLevel1 = createLevelButton("LEVEL 1", "Hall Corridor");
        Button btnLevel2 = createLevelButton("LEVEL 2", "The Street");
        Button btnLevel3 = createLevelButton("LEVEL 3", "Academic Building");

        // Level 1 → Level1.java
        btnLevel1.setOnAction(e -> {
            Level1 level = new Level1(MainApp.WINDOW_HEIGHT - 130);
            GameScene gameScene = new GameScene(level);
            MainApp.switchScene(gameScene.getScene());
        });

        // Level 2 → Level2.java
        btnLevel2.setOnAction(e -> {
            Level2 level = new Level2(MainApp.WINDOW_HEIGHT - 130);
            GameScene gameScene = new GameScene(level);
            MainApp.switchScene(gameScene.getScene());
        });

        // Level 3 → Level3.java
        btnLevel3.setOnAction(e -> {
            Level3 level = new Level3(MainApp.WINDOW_HEIGHT - 130);
            GameScene gameScene = new GameScene(level);
            MainApp.switchScene(gameScene.getScene());
        });

        levelButtonsBox.getChildren().addAll(btnLevel1, btnLevel2, btnLevel3);
        contentLayout.getChildren().addAll(titleLabel, levelButtonsBox);

        // --- Close Button (X) ---
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("level-close");
        closeButton.setOnAction(e -> onClose.run());

        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(45, 65, 0, 0));

        // Final assembly (Order: Bottom to Top)
        this.getChildren().addAll(frameView, contentLayout, closeButton);
    }

    /**
     * Creates a styled level button with a title and subtitle.
     */
    private Button createLevelButton(String title, String subtitle) {
        // Main label
        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("level-btn-title");

        // Subtitle label
        Label subLbl = new Label(subtitle);
        subLbl.getStyleClass().add("level-btn-sub");

        VBox btnContent = new VBox(4, titleLbl, subLbl);
        btnContent.setAlignment(Pos.CENTER);

        Button btn = new Button();
        btn.setGraphic(btnContent);
        btn.getStyleClass().add("level-select-button");
        return btn;
    }
}