package edu.bauet.java.cse.duckrun.ui;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.levels.Level1;
import edu.bauet.java.cse.duckrun.levels.Level2;
import edu.bauet.java.cse.duckrun.levels.Level3;
import edu.bauet.java.cse.duckrun.scenes.EndlessGameScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EndlessLevelMenu extends StackPane {

    private final Runnable onClose;

    public EndlessLevelMenu(Runnable onCloseAction) {
        this.onClose = onCloseAction;
        initialize();
    }

    private void initialize() {
        this.setVisible(false);
        this.getStyleClass().add("level-box");
        this.setPrefSize(925, 546);
        this.setMaxSize(925, 546);
        this.getStylesheets().add(getClass().getResource("/styles/level_menu.css").toExternalForm());

        Image frameImg = new Image(getClass().getResourceAsStream("/images/pause_menu/settings_menu_frame.png"));
        ImageView frameView = new ImageView(frameImg);

        VBox contentLayout = new VBox(20);
        contentLayout.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("ENDLESS");
        titleLabel.getStyleClass().add("level-title");
        VBox.setMargin(titleLabel, new Insets(55, 0, 20, 0));

        VBox levelButtonsBox = new VBox(16);
        levelButtonsBox.setAlignment(Pos.CENTER);

        Button btnLevel1 = createLevelButton("LEVEL 1", "Hall Corridor");
        Button btnLevel2 = createLevelButton("LEVEL 2", "The Street");
        Button btnLevel3 = createLevelButton("LEVEL 3", "Academic Building");

        btnLevel1.setOnAction(e -> {
            EndlessGameScene gs = new EndlessGameScene(new Level1(MainApp.WINDOW_HEIGHT - 130));
            MainApp.switchScene(gs.getScene());
        });
        btnLevel2.setOnAction(e -> {
            EndlessGameScene gs = new EndlessGameScene(new Level2(MainApp.WINDOW_HEIGHT - 130));
            MainApp.switchScene(gs.getScene());
        });
        btnLevel3.setOnAction(e -> {
            EndlessGameScene gs = new EndlessGameScene(new Level3(MainApp.WINDOW_HEIGHT - 130));
            MainApp.switchScene(gs.getScene());
        });

        levelButtonsBox.getChildren().addAll(btnLevel1, btnLevel2, btnLevel3);
        contentLayout.getChildren().addAll(titleLabel, levelButtonsBox);

        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("level-close");
        closeButton.setOnAction(e -> onClose.run());
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(45, 65, 0, 0));

        this.getChildren().addAll(frameView, contentLayout, closeButton);
    }

    private Button createLevelButton(String title, String subtitle) {
        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("level-btn-title");

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
