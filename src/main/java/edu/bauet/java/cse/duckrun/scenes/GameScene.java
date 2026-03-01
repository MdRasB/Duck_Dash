package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.Duck;
import edu.bauet.java.cse.duckrun.ui.PauseMenu;
import edu.bauet.java.cse.duckrun.ui.SettingsMenu;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import edu.bauet.java.cse.duckrun.entities.Eagle;
import edu.bauet.java.cse.duckrun.utils.CollisionUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;

import java.awt.*;

public class GameScene {

    private Pane root;
    private Scene scene;
    private Duck duck;
    private AnimationTimer gameLoop;

    ///for background image
    private ImageView bg1;
    private ImageView bg2;

    // This must match your actual image width (1280 * 2 = 2560)
    private double bgImageWidth;
    private static final double WORLD_SPEED = 7;

    ///for pause menu
    private boolean isPaused = false;
    private Button pauseButton;
    private PauseMenu pauseMenu;

    private SettingsMenu settingsMenu;

    // Eagle system
    private List<Eagle> eagles = new ArrayList<>();
    private long nextSpawnTime = 0;

    public GameScene(String backgroundPath) {
        //pause menu actions
        pauseMenu = new PauseMenu(
                () -> resumeGame(),
                () -> restartGame(),
                () -> openSettings(),
                () -> exitToMenu()
        );

        settingsMenu = new SettingsMenu(() -> {
            settingsMenu.setVisible(false);

            pauseMenu.getRoot().getChildren().forEach(node -> node.setVisible(true));
        });
        settingsMenu.setVisible(false);

        //setup pause button
        Button btnPause = new Button();

        Image pauseBtnImg = new Image(getClass().getResourceAsStream("/images/pause_menu/pause_button.png"));
        ImageView pauseIconView = new ImageView(pauseBtnImg);
        pauseIconView.setFitWidth(60);
        pauseIconView.setPreserveRatio(true);
        btnPause.setGraphic(pauseIconView);

        btnPause.getStyleClass().add("pause-icon-button");
        //position top right
        btnPause.setLayoutX(MainApp.WINDOW_WIDTH - 80);
        btnPause.setLayoutY(15);

        //pause the game
        btnPause.setOnMouseClicked(e -> pauseGame());
        btnPause.setCursor(javafx.scene.Cursor.HAND);
        this.pauseButton = btnPause;

        initialize(backgroundPath);

    }

    private void initialize(String backgroundPath) {
        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        createBackground(backgroundPath);
        createPlayer();

        root.getChildren().addAll(bg1, bg2, duck.getNode(), pauseButton, pauseMenu.getRoot(),settingsMenu);
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/pause_menu.css").toExternalForm());

        //Neutral Focus..
        root.setFocusTraversable(true);
        root.requestFocus();

        setupControls();
        startGameLoop();
    }

    private void createBackground(String path) {
        var resource = getClass().getResource(path);
        if (resource == null) {
            throw new RuntimeException("Background not found: " + path);
        }

        Image bgImage = AssetLoader.getImage(path);

        // 1. Get the actual width (2560)
        bgImageWidth = bgImage.getWidth();

        bg1 = new ImageView(bgImage);
        bg2 = new ImageView(bgImage);

        // 2. ONLY set height. Do NOT set FitWidth to WINDOW_WIDTH.
        // Let the width stay at 2560 so the image isn't squashed.
        bg1.setFitHeight(MainApp.WINDOW_HEIGHT);
        bg1.setPreserveRatio(true);

        bg2.setFitHeight(MainApp.WINDOW_HEIGHT);
        bg2.setPreserveRatio(true);

        // 3. Position the second image at the end of the first one (2560px away)
        bg1.setLayoutX(0);
        bg2.setLayoutX(bgImageWidth);
    }

    private void updateBackground() {
        // Move both images
        bg1.setLayoutX(bg1.getLayoutX() - WORLD_SPEED);
        bg2.setLayoutX(bg2.getLayoutX() - WORLD_SPEED);

        // 4. Reset logic using bgImageWidth (2560) instead of Window Width (1280)
        if (bg1.getLayoutX() + bgImageWidth <= 0) {
            bg1.setLayoutX(bg2.getLayoutX() + bgImageWidth);
        }

        if (bg2.getLayoutX() + bgImageWidth <= 0) {
            bg2.setLayoutX(bg1.getLayoutX() + bgImageWidth);
        }
    }

    // ... (rest of your methods: createPlayer, setupControls, startGameLoop, etc.)
    private void createPlayer() {
        double groundLine = MainApp.WINDOW_HEIGHT - 130;
        duck = new Duck(200, groundLine);
    }

    private void setupControls() {
        scene.setOnKeyPressed(event -> {
            //pause game
            if (event.getCode() == KeyCode.ESCAPE) {
                if (isPaused) {
                    resumeGame();
                } else {
                    pauseGame();
                }
                return;
            }
            if (isPaused) return;

            //duck controls
            if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) {
                duck.jump();
            }
            if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.C) {
                duck.setCrouching(true);
            }

        });
        scene.setOnKeyReleased(event -> {
            if (isPaused) return;

            if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.C) {
                duck.setCrouching(false);
            }
        });


    }

    private void spawnEagle(long now) {

        if (now < nextSpawnTime) return;

        double spawnX = MainApp.WINDOW_WIDTH + 150;

        // Spawn slightly above ground
        double spawnY = MainApp.WINDOW_HEIGHT - 250;

        Eagle eagle = new Eagle(spawnX, spawnY);

        eagles.add(eagle);
        root.getChildren().add(eagle.getNode());

        // Random interval between 1.5 to 3.5 seconds
        long randomDelay = (long)((1.5 + Math.random() * 2.0) * 1_000_000_000);

        nextSpawnTime = now + randomDelay;
    }

    private void updateEagles() {

        Iterator<Eagle> iterator = eagles.iterator();

        while (iterator.hasNext()) {

            Eagle eagle = iterator.next();
            eagle.update();

            // Remove inactive
            if (!eagle.isActive()) {
                root.getChildren().remove(eagle.getNode());
                iterator.remove();
                continue;
            }

            // Collision check (only once per eagle)
            if (!eagle.hasCollided() &&
                    CollisionUtil.isColliding(
                            duck.getHitBox(),
                            eagle.getHitBox()
                    )) {

                eagle.markCollided();

                System.out.println("Duck HIT by Eagle!");
            }
        }
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    updateBackground();
                    duck.update();

                    spawnEagle(now);
                    updateEagles();
                }
            }
        };
        gameLoop.start();
    }

    private void pauseGame() {
        if (isPaused) return;
        isPaused = true;

        pauseMenu.setVisible(true, bg1, bg2);

        pauseButton.setVisible(false);
    }

    private void resumeGame() {
        //Neutraling Focus...
        root.requestFocus();

        if (!isPaused) return;
        isPaused = false;

        pauseMenu.setVisible(false, bg1, bg2);

        pauseButton.setVisible(true);
    }

    private void restartGame() {
        resumeGame();

        //reset duck
        double groundLine = MainApp.WINDOW_HEIGHT - 130;
        duck.getNode().setLayoutY(groundLine);

        //reset background
        bg1.setLayoutX(0);
        bg2.setLayoutX(bgImageWidth);
    }

    private void openSettings() {
        //hide pause menu
        pauseMenu.getRoot().getChildren().forEach(node -> {
            if (!(node instanceof javafx.scene.shape.Rectangle)) {
                node.setVisible(false);
            }
        });

        settingsMenu.setLayoutX((MainApp.WINDOW_WIDTH - 925) / 2.0);
        settingsMenu.setLayoutY((MainApp.WINDOW_HEIGHT - 546) / 2.0);
        settingsMenu.setVisible(true);
    }

    private void exitToMenu() {
        gameLoop.stop();

        MenuScene menuScene = new MenuScene(MainApp.getPrimaryStage());
        MainApp.switchScene(menuScene.createScene());
    }

    public Scene getScene() {
        return scene;
    }
}