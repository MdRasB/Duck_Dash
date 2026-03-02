package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.Duck;
import edu.bauet.java.cse.duckrun.entities.Eagle;
import edu.bauet.java.cse.duckrun.entities.Enemy;
import edu.bauet.java.cse.duckrun.ui.PauseMenu;
import edu.bauet.java.cse.duckrun.ui.SettingsMenu;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
//import edu.bauet.java.cse.duckrun.entities.Eagle;    //Eagle is not needed right now...
import edu.bauet.java.cse.duckrun.entities.Cat;
import edu.bauet.java.cse.duckrun.utils.CollisionUtil;
import edu.bauet.java.cse.duckrun.ui.HealthBar;

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
import javafx.scene.layout.StackPane;

import java.awt.*;

public class GameScene {

    private Pane root;
    private Scene scene;
    private Duck duck;
    private AnimationTimer gameLoop;
    // In GameScene.java
    private StackPane menuLayer;  // a new layer for menus

    ///for background image
    private ImageView background1;
    private ImageView background2;

    // This must match your actual image width (1280 * 2 = 2560)
    private double bgImageWidth;
    private static final double WORLD_SPEED = 7;

    ///for pause menu
    private boolean isPaused = false;
    private Button pauseButton;
    private PauseMenu pauseMenu;

    private SettingsMenu settingsMenu;

    //Enemy creation
    private final List<Enemy> enemies = new ArrayList<>();
    private long nextSpawnTime = 0;

    private final boolean spawnCats;
    private final boolean spawnEagles;

    private double worldSpeed = 5;   // shared speed

    //HealthBar
    private HealthBar healthBar;

    public GameScene(String backgroundPath,
                     boolean spawnCats,
                     boolean spawnEagles,
                     double worldSpeed) {

        this.spawnCats = spawnCats;
        this.spawnEagles = spawnEagles;
        this.worldSpeed = worldSpeed;

        initialize(backgroundPath);
    }

    private void initialize(String backgroundPath) {
        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        // Layer for menus
        menuLayer = new StackPane();
        menuLayer.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        menuLayer.setPickOnBounds(false); // clicks pass through when menu not visible

        scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/pause_menu.css").toExternalForm());

        // Background
        createBackground(backgroundPath);

        // Player
        createPlayer();

        // Health Bar
        healthBar = new HealthBar(3);
        healthBar.getView().setLayoutX(20);
        healthBar.getView().setLayoutY(20);

        // Pause & Settings
        createPauseSystem();

        // Add layers in correct order
        root.getChildren().addAll(background1, background2, duck.getNode(), healthBar.getView(), pauseButton, menuLayer);

        // Menus go inside menuLayer
        menuLayer.getChildren().addAll(pauseMenu.getRoot(), settingsMenu);

        root.setFocusTraversable(true);
        root.requestFocus();

        setupControls();
        startGameLoop();
    }

    private void createPauseSystem() {

        // Load pause image
        Image pauseImage = AssetLoader.getImage("/images/pause_menu/pause_button.png");
        ImageView pauseIcon = new ImageView(pauseImage);
        pauseIcon.setFitWidth(40);  // adjust size as needed
        pauseIcon.setFitHeight(40);
        pauseIcon.setPreserveRatio(true);

        // Pause Button with image
        pauseButton = new Button();
        pauseButton.setGraphic(pauseIcon);  // set image instead of text
        pauseButton.setStyle("-fx-background-color: transparent;"); // optional: make button background transparent
        pauseButton.setLayoutX(MainApp.WINDOW_WIDTH - 80);
        pauseButton.setLayoutY(20);

        pauseButton.setOnAction(e -> {
            if (isPaused) {
                resumeGame();
            } else {
                pauseGame();
            }
        });

        // Pause Menu
        pauseMenu = new PauseMenu(
                this::resumeGame,
                this::restartGame,
                this::openSettings,
                this::exitToMenu
        );

        pauseMenu.getRoot().setVisible(false);

        // Settings Menu
        settingsMenu = new SettingsMenu(() -> {
            settingsMenu.setVisible(false);
            pauseMenu.getRoot().setVisible(true);
            pauseMenu.getRoot().toFront();  // restore pause menu on top
            pauseButton.setVisible(true);
            root.requestFocus();
        });

        settingsMenu.setVisible(false);
    }

    private void createBackground(String path) {

        Image bgImage = AssetLoader.getImage(path);

        background1 = new ImageView(bgImage);
        background2 = new ImageView(bgImage);

        // DO NOT stretch vertically
        background1.setFitHeight(MainApp.WINDOW_HEIGHT);
        background1.setPreserveRatio(true);

        background2.setFitHeight(MainApp.WINDOW_HEIGHT);
        background2.setPreserveRatio(true);

        double width = bgImage.getWidth();

        background1.setLayoutX(0);
        background2.setLayoutX(width);

        background1.setLayoutY(0);
        background2.setLayoutY(0);

        //root.getChildren().addAll(background1, background2);
    }

    private void updateBackground() {

        background1.setLayoutX(background1.getLayoutX() - worldSpeed);
        background2.setLayoutX(background2.getLayoutX() - worldSpeed);

        double width = background1.getImage().getWidth();

        if (background1.getLayoutX() <= -width) {
            background1.setLayoutX(background2.getLayoutX() + width);
        }

        if (background2.getLayoutX() <= -width) {
            background2.setLayoutX(background1.getLayoutX() + width);
        }
    }

    // ... (rest of your methods: createPlayer, setupControls, startGameLoop, etc.)
    private void createPlayer() {
        double groundLine = MainApp.WINDOW_HEIGHT - 130;
        duck = new Duck(200, groundLine);
        //root.getChildren().add(duck.getNode());
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

    //Enemy Spawn Method::
    private void spawnEnemy(long now) {

        if (now < nextSpawnTime) return;

        double spawnX = MainApp.WINDOW_WIDTH + 100;
        Enemy enemy = null;

        if (spawnCats) {

            double groundY = MainApp.WINDOW_HEIGHT - 150;
            enemy = new Cat(spawnX, groundY, worldSpeed);

        } else if (spawnEagles) {

            double airY = MainApp.WINDOW_HEIGHT - 250;
            enemy = new Eagle(spawnX, airY, worldSpeed);
        }

        if (enemy != null) {
            enemies.add(enemy);
            root.getChildren().add(root.getChildren().size() - 1, enemy.getNode());
        }

        long delay = (long)((2 + Math.random() * 2) * 1_000_000_000);
        nextSpawnTime = now + delay;
    }

    //Spawn Update Method::
    private void updateEnemies() {

        Iterator<Enemy> iterator = enemies.iterator();

        while (iterator.hasNext()) {

            Enemy enemy = iterator.next();
            enemy.update();

            if (!enemy.isActive()) {
                root.getChildren().remove(enemy.getNode());
                iterator.remove();
                continue;
            }

            if (!enemy.hasCollided() &&
                    CollisionUtil.isColliding(
                            duck.getHitBox(),
                            enemy.getHitBox()
                    )) {

                enemy.markCollided();
                healthBar.decreaseHealth();

                if (healthBar.isDead()) {
                    System.out.println("GAME OVER");
                }
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

                    spawnEnemy(now);
                    updateEnemies();
                }
            }
        };
        gameLoop.start();
    }

    private void pauseGame() {
        if (isPaused) return;
        isPaused = true;
        pauseMenu.setVisible(true);
        menuLayer.toFront();            // make sure menu layer is top
        pauseMenu.getRoot().toFront();  // pause menu visible
        pauseButton.setVisible(false);
    }

    private void resumeGame() {
        if (!isPaused) return;

        isPaused = false;

        pauseMenu.setVisible(false);
        pauseButton.setVisible(true);
        root.requestFocus();
    }

    private void restartGame() {
        // Resume if paused
        resumeGame();

        // Reset Duck
        duck.resetState();

        // Reset Background
        background1.setLayoutX(0);
        background2.setLayoutX(background1.getImage().getWidth());

        // Clear enemies
        for (Enemy e : enemies) {
            root.getChildren().remove(e.getNode());
        }
        enemies.clear();
        nextSpawnTime = 0;

        // Reset Health Bar
        healthBar.reset();

        // Optionally reset other game states: score, time, etc.
    }

    private void openSettings() {
        pauseMenu.getRoot().setVisible(false);

        // Center settings menu
        settingsMenu.setLayoutX((MainApp.WINDOW_WIDTH - settingsMenu.getPrefWidth()) / 2.0);
        settingsMenu.setLayoutY((MainApp.WINDOW_HEIGHT - settingsMenu.getPrefHeight()) / 2.0);

        settingsMenu.setVisible(true);
        settingsMenu.toFront();
        menuLayer.toFront();

        isPaused = true;
        root.requestFocus();
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