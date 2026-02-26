package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;

import edu.bauet.java.cse.duckrun.levels.Level1;
import edu.bauet.java.cse.duckrun.ui.LevelMenu;
import edu.bauet.java.cse.duckrun.ui.HighScoreMenu;
import edu.bauet.java.cse.duckrun.ui.SettingsMenu;

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
        //menu options text font
        Font pixelFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 14);

        root = new StackPane(); //root layout

        //background image
        Image bgImage = new Image(getClass().getResourceAsStream("/images/ui/menu/menu_bg.png"));
        background = new ImageView(bgImage);

        //background size
        background.setFitWidth(MainApp.WINDOW_WIDTH);
        background.setFitHeight(MainApp.WINDOW_HEIGHT);
        background.setPreserveRatio(false);

        // overlay for background effect
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

        //settings,level and high score menu initialize
        levelMenu = new LevelMenu(this::closeMenu);
        highScoreMenu = new HighScoreMenu(this::closeMenu);
        settingsMenu = new SettingsMenu(this::closeMenu);

        //button actions
        btnNewGame.setOnAction(e -> {
            Level1 level1 = new Level1();
            MainApp.switchScene(level1.createLevel());
        });

        btnLevels.setOnAction(e -> showMenu(levelMenu));
        btnScore.setOnAction(e -> showMenu(highScoreMenu));
        btnSettings.setOnAction(e -> showMenu(settingsMenu));
        btnExit.setOnAction(e -> stage.close());

        //organize buttons
        menuBox = new VBox(10); //spacing between buttons
        menuBox.getChildren().addAll(titleView, btnNewGame, btnLevels, btnScore, btnSettings, btnExit);

        //alignment of buttons
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setStyle("-fx-padding: 80 0 0 160;");

        //final assembly
        root.getChildren().addAll(background, overlay, menuBox, levelMenu, highScoreMenu, settingsMenu);

        //create scene and link CSS
        Scene scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/main_menu.css").toExternalForm());

        return scene;
    }

    private void showMenu(StackPane menuToShow) {
        //initially hidden
        levelMenu.setVisible(false);
        highScoreMenu.setVisible(false);
        settingsMenu.setVisible(false);

        //show menus when clicked
        menuToShow.setVisible(true);
        overlay.setVisible(true);

        //background
        menuBox.setDisable(true);
        menuBox.setOpacity(0.3);
        background.setEffect(new GaussianBlur(10));
    }

    private void closeMenu() {
        //close menus
        levelMenu.setVisible(false);
        highScoreMenu.setVisible(false);
        settingsMenu.setVisible(false);

        //reset background and buttons
        overlay.setVisible(false);
        menuBox.setDisable(false);
        menuBox.setOpacity(1.0);
        background.setEffect(null);
    }

    //create style class
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        return btn;
    }
}
