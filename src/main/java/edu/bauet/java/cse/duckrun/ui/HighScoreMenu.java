package edu.bauet.java.cse.duckrun.ui;

import edu.bauet.java.cse.duckrun.utils.HighScoreManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HighScoreMenu extends StackPane {

    private Runnable onClose;
    private VBox storyPanel;
    private VBox endlessPanel;

    public HighScoreMenu(Runnable onCloseAction) {
        this.onClose = onCloseAction;
        initialize();
    }

    /** Rebuilds the score rows with the latest values from HighScoreManager. */
    public void refresh() {
        storyPanel.getChildren().setAll(
                buildScoreRow("Level 1", HighScoreManager.getLevel1Best()),
                buildScoreRow("Level 2", HighScoreManager.getLevel2Best()),
                buildScoreRow("Level 3", HighScoreManager.getLevel3Best())
        );
        endlessPanel.getChildren().setAll(
                buildScoreRow("Level 1", HighScoreManager.getEndless1Best()),
                buildScoreRow("Level 2", HighScoreManager.getEndless2Best()),
                buildScoreRow("Level 3", HighScoreManager.getEndless3Best())
        );
    }

    private void initialize() {
        this.setVisible(false);
        this.getStyleClass().add("hs-box");
        this.setPrefSize(925, 546);
        this.setMaxSize(925, 546);
        this.getStylesheets().add(getClass().getResource("/styles/hs_menu.css").toExternalForm());

        Image frameImg = new Image(getClass().getResourceAsStream("/images/pause_menu/settings_menu_frame.png"));
        ImageView frameView = new ImageView(frameImg);

        Label titleLabel = new Label("HIGH SCORES");
        titleLabel.getStyleClass().add("hs-title");

        Button btnStory   = new Button("Story");
        Button btnEndless = new Button("Endless");
        btnStory.getStyleClass().addAll("hs-tab-button", "hs-tab-active");
        btnEndless.getStyleClass().add("hs-tab-button");

        HBox tabRow = new HBox(20, btnStory, btnEndless);
        tabRow.setAlignment(Pos.CENTER);

        storyPanel   = buildStoryPanel();
        endlessPanel = buildEndlessPanel();
        endlessPanel.setVisible(false);
        endlessPanel.setManaged(false);

        btnStory.setOnAction(e -> {
            btnStory.getStyleClass().add("hs-tab-active");
            btnEndless.getStyleClass().remove("hs-tab-active");
            storyPanel.setVisible(true);   storyPanel.setManaged(true);
            endlessPanel.setVisible(false); endlessPanel.setManaged(false);
        });

        btnEndless.setOnAction(e -> {
            btnEndless.getStyleClass().add("hs-tab-active");
            btnStory.getStyleClass().remove("hs-tab-active");
            endlessPanel.setVisible(true);  endlessPanel.setManaged(true);
            storyPanel.setVisible(false);   storyPanel.setManaged(false);
        });

        VBox contentLayout = new VBox(18);
        contentLayout.setAlignment(Pos.TOP_CENTER);
        contentLayout.setPadding(new Insets(55, 0, 0, 0));
        contentLayout.getChildren().addAll(titleLabel, tabRow, storyPanel, endlessPanel);

        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("hs-close");
        closeButton.setOnAction(e -> onClose.run());
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(45, 65, 0, 0));

        this.getChildren().addAll(frameView, contentLayout, closeButton);
    }

    private VBox buildStoryPanel() {
        VBox panel = new VBox(14);
        panel.setAlignment(Pos.CENTER);
        panel.getChildren().addAll(
                buildScoreRow("Level 1", HighScoreManager.getLevel1Best()),
                buildScoreRow("Level 2", HighScoreManager.getLevel2Best()),
                buildScoreRow("Level 3", HighScoreManager.getLevel3Best())
        );
        return panel;
    }

    private HBox buildScoreRow(String levelName, String time) {
        Label nameLabel = new Label(levelName);
        nameLabel.getStyleClass().add("hs-row-label");
        nameLabel.setPrefWidth(260);
        nameLabel.setAlignment(Pos.CENTER_LEFT);

        Label dots = new Label("................");
        dots.getStyleClass().add("hs-row-dots");

        Label timeLabel = new Label(time);
        timeLabel.getStyleClass().add("hs-row-time");
        timeLabel.setPrefWidth(160);
        timeLabel.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(10, nameLabel, dots, timeLabel);
        row.setAlignment(Pos.CENTER);
        row.setPrefWidth(560);
        return row;
    }

    private VBox buildEndlessPanel() {
        VBox panel = new VBox(14);
        panel.setAlignment(Pos.CENTER);
        panel.getChildren().addAll(
                buildScoreRow("Level 1", HighScoreManager.getEndless1Best()),
                buildScoreRow("Level 2", HighScoreManager.getEndless2Best()),
                buildScoreRow("Level 3", HighScoreManager.getEndless3Best())
        );
        return panel;
    }
}