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
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuScene {

    private Stage stage;

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

        StackPane root = new StackPane(); //root layout

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


        //organize buttons
        VBox menuBox = new VBox(10); //spacing between buttons
        menuBox.getChildren().addAll(titleView, btnNewGame, btnLevels, btnScore, btnSettings, btnExit);

        //alignment of buttons
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setStyle("-fx-padding: 80 0 0 160;");

        //final assembly
        root.getChildren().addAll(background, menuBox);

        //create scene and link CSS
        Scene scene = new Scene(root,MainApp.WINDOW_WIDTH,MainApp.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/main_menu.css").toExternalForm());

        return scene;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        return btn;
    }
}
