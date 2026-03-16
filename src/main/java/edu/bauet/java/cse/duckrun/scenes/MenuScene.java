package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.levels.Level1;
import edu.bauet.java.cse.duckrun.ui.LevelMenu;
import edu.bauet.java.cse.duckrun.ui.HighScoreMenu;
import edu.bauet.java.cse.duckrun.ui.SettingsMenu;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.GaussianBlur;

public class MenuScene {

    private Stage stage;
    private StackPane root;
    private ImageView background;
    private VBox menuBox;
    private Rectangle overlay;
    private SettingsMenu settingsMenu;
    private LevelMenu levelMenu;
    private HighScoreMenu highScoreMenu;

    public MenuScene(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 14);

        root = new StackPane();

        Image bgImage = AssetLoader.getImage("/images/ui/menu/menu_bg.png");
        background = new ImageView(bgImage);
        background.setFitWidth(MainApp.WINDOW_WIDTH);
        background.setFitHeight(MainApp.WINDOW_HEIGHT);
        background.setPreserveRatio(false);

        overlay = new Rectangle(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        overlay.setStyle("-fx-fill: rgba(0, 0, 0, 0.5);");
        overlay.setVisible(false);

        Image gameTitle = new Image(getClass().getResourceAsStream("/images/ui/menu/title2.png"));
        ImageView titleView = new ImageView(gameTitle);
        titleView.setFitWidth(472);
        titleView.setPreserveRatio(true);
        VBox.setMargin(titleView, new javafx.geometry.Insets(-175, 0, 0, -105));

        Button btnNewGame = createMenuButton("New Game");
        Button btnLevels = createMenuButton("Levels");
        Button btnScore = createMenuButton("High Score");
        Button btnSettings = createMenuButton("Settings");
        Button btnExit = createMenuButton("Exit");

        levelMenu = new LevelMenu(this::closeMenu);
        highScoreMenu = new HighScoreMenu(this::closeMenu);
        settingsMenu = new SettingsMenu(this::closeMenu, highScoreMenu);

        btnNewGame.setOnAction(e -> {
            // Create a Level1 object and pass it to the GameScene
            Level1 level1 = new Level1(MainApp.WINDOW_HEIGHT - 130);
            GameScene gameScene = new GameScene(level1);
            MainApp.switchScene(gameScene.getScene());
        });

        btnLevels.setOnAction(e -> showMenu(levelMenu));
        btnScore.setOnAction(e -> showMenu(highScoreMenu));
        btnSettings.setOnAction(e -> showMenu(settingsMenu));
        btnExit.setOnAction(e -> stage.close());

        menuBox = new VBox(10);
        menuBox.getChildren().addAll(titleView, btnNewGame, btnLevels, btnScore, btnSettings, btnExit);
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setStyle("-fx-padding: 80 0 0 160;");

        root.getChildren().addAll(background, overlay, menuBox, levelMenu, highScoreMenu, settingsMenu);

        Scene scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/main_menu.css").toExternalForm());

        return scene;
    }

    private void showMenu(StackPane menuToShow) {
        levelMenu.setVisible(false);
        highScoreMenu.setVisible(false);
        settingsMenu.setVisible(false);
        menuToShow.setVisible(true);
        overlay.setVisible(true);
        menuBox.setDisable(true);
        menuBox.setVisible(false);
        background.setEffect(new GaussianBlur(10));
    }

    private void closeMenu() {
        levelMenu.setVisible(false);
        highScoreMenu.setVisible(false);
        settingsMenu.setVisible(false);
        overlay.setVisible(false);
        menuBox.setDisable(false);
        menuBox.setVisible(true);
        background.setEffect(null);
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        return btn;
    }
}