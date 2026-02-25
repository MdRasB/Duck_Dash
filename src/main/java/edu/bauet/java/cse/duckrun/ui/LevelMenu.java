package edu.bauet.java.cse.duckrun.ui;

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

    //close action
    public LevelMenu(Runnable onCloseAction) {
        this.onClose = onCloseAction;
        initialize();
    }

    private void initialize() {
        //set style class
        this.getStyleClass().add("level-box");

        this.setPrefSize(925, 546);
        this.setMaxSize(925, 546);
        //link with CSS file
        this.getStylesheets().add(getClass().getResource("/styles/level_menu.css").toExternalForm());

        //frame image
        Image frameImg = new Image(getClass().getResourceAsStream("/images/pause_menu/settings_menu_frame.png"));
        ImageView frameView = new ImageView(frameImg);

        VBox contentLayout = new VBox(20);
        contentLayout.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("LEVELS");
        titleLabel.getStyleClass().add("level-title");
        VBox.setMargin(titleLabel, new Insets(60, 0, 0, 0));

        // Close Button (X)
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("level-close");
        closeButton.setOnAction(e -> onClose.run());

        // Position the Close button
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(45, 65, 0, 0));

        // final assembly (Order: Bottom to Top)
        this.getChildren().addAll(frameView, contentLayout, closeButton);
    }
}
