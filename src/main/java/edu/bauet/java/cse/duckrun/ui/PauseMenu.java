package edu.bauet.java.cse.duckrun.ui;

import edu.bauet.java.cse.duckrun.MainApp;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.GaussianBlur;
import javafx.geometry.Insets;

public class PauseMenu {

    private StackPane pauseRoot;
    private Rectangle overlay;
    private ImageView frame;
    private Runnable onResume, onRestart, onSettings, onExit;

    public PauseMenu(Runnable onResume, Runnable onRestart, Runnable onSettings, Runnable onExit) {
        this.onResume = onResume;
        this.onRestart = onRestart;
        this.onSettings = onSettings;
        this.onExit = onExit;
        initialize();
    }

    private void initialize() {
        pauseRoot = new StackPane();
        pauseRoot.setVisible(false);

        // 1. Background blurry Overlay
        overlay = new Rectangle(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        overlay.getStyleClass().add("pause-overlay");

        // 2. Pause Menu Frame
        Image frameImg = new Image(getClass().getResourceAsStream("/images/pause_menu/pause_menu_frame.png"));
        frame = new ImageView(frameImg);
        frame.setPreserveRatio(true);
        frame.setFitWidth(500); // Adjust based on your frame resolution

        // 3. Button Grid (2x2)
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(20);
        grid.setPadding(new Insets(40, 0, 0, 0)); // Offset to center buttons in the frame

        // Create Buttons
        Button btnRestart = createButton("/images/pause_menu/restart_button.png", onRestart);
        Button btnPlay = createButton("/images/pause_menu/play_button.png", onResume);
        Button btnExit = createButton("/images/pause_menu/exit_button.png", onExit);
        Button btnSettings = createButton("/images/pause_menu/settings_button.png", onSettings);

        grid.add(btnRestart, 0, 0);
        grid.add(btnPlay, 1, 0);
        grid.add(btnExit, 0, 1);
        grid.add(btnSettings, 1, 1);

        pauseRoot.getChildren().addAll(overlay, frame, grid);
    }

    private Button createButton(String path, Runnable action) {
        ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(path)));
        iv.setFitWidth(80);
        iv.setPreserveRatio(true);

        Button btn = new Button();
        btn.setGraphic(iv);
        btn.getStyleClass().add("pause-icon-button");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    public StackPane getRoot() {
        return pauseRoot;
    }

    public void setVisible(boolean visible, ImageView backgroundToBlur) {
        pauseRoot.setVisible(visible);
        if (visible) {
            backgroundToBlur.setEffect(new GaussianBlur(10));
        } else {
            backgroundToBlur.setEffect(null);
        }
    }
}