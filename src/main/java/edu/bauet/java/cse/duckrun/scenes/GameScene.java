package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.Duck;
import edu.bauet.java.cse.duckrun.entities.Eagle;
import edu.bauet.java.cse.duckrun.entities.Enemy;
import edu.bauet.java.cse.duckrun.ui.PauseMenu;
import edu.bauet.java.cse.duckrun.ui.SettingsMenu;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import edu.bauet.java.cse.duckrun.entities.Cat;
import edu.bauet.java.cse.duckrun.utils.CollisionUtil;
import edu.bauet.java.cse.duckrun.ui.HealthBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class GameScene {

    private Pane root;
    private Scene scene;
    private Duck duck;
    private AnimationTimer gameLoop;
    private StackPane menuLayer;

    private ImageView background1;
    private ImageView background2;

    private boolean isPaused = false;
    private Button pauseButton;
    private PauseMenu pauseMenu;
    private SettingsMenu settingsMenu;

    private final List<Enemy> enemies = new ArrayList<>();
    private long nextSpawnTime = 0;

    private final boolean spawnCats;
    private final boolean spawnEagles;
    private double worldSpeed = 7;

    private HealthBar healthBar;

    // Constructor with parameters for level configuration
    public GameScene(String backgroundPath, boolean spawnCats, boolean spawnEagles, double worldSpeed) {
        this.spawnCats = spawnCats;
        this.spawnEagles = spawnEagles;
        this.worldSpeed = worldSpeed;
        initialize(backgroundPath);
    }

    // Default constructor for quick testing/default level
    public GameScene() {
        this("/images/ui/hall_ui_1200x600.png", true, true, 7);
    }

    private void initialize(String backgroundPath) {
        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        menuLayer = new StackPane();
        menuLayer.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        menuLayer.setPickOnBounds(false);

        scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles/pause_menu.css").toExternalForm());

        createBackground(backgroundPath);
        createPlayer();

        healthBar = new HealthBar(3);
        healthBar.getView().setLayoutX(20);
        healthBar.getView().setLayoutY(20);

        createPauseSystem();

        // Add layers: Background -> Duck -> Enemies (added dynamically) -> UI
        root.getChildren().addAll(background1, background2, duck.getNode(), healthBar.getView(), pauseButton, menuLayer);

        menuLayer.getChildren().addAll(pauseMenu.getRoot(), settingsMenu);

        root.setFocusTraversable(true);
        root.requestFocus();

        setupControls();
        startGameLoop();
    }

    private void createPauseSystem() {
        Image pauseImage = AssetLoader.getImage("/images/pause_menu/pause_button.png");
        ImageView pauseIcon = new ImageView(pauseImage);
        pauseIcon.setFitWidth(60);
        pauseIcon.setFitHeight(60);
        pauseIcon.setPreserveRatio(true);

        pauseButton = new Button();
        pauseButton.setGraphic(pauseIcon);
        pauseButton.setStyle("-fx-background-color: transparent;");
        pauseButton.setLayoutX(MainApp.WINDOW_WIDTH - 80);
        pauseButton.setLayoutY(20);

        pauseButton.setOnAction(e -> {
            if (isPaused) resumeGame();
            else pauseGame();
        });

        pauseMenu = new PauseMenu(this::resumeGame, this::restartGame, this::openSettings, this::exitToMenu);
        pauseMenu.getRoot().setVisible(false);

        settingsMenu = new SettingsMenu(() -> {
            settingsMenu.setVisible(false);
            pauseMenu.getRoot().setVisible(true);
            pauseMenu.getRoot().toFront();
            pauseButton.setVisible(true);
            root.requestFocus();
        });
        settingsMenu.setVisible(false);
    }

    private void createBackground(String path) {
        Image bgImage = AssetLoader.getImage(path);
        background1 = new ImageView(bgImage);
        background2 = new ImageView(bgImage);

        background1.setFitHeight(MainApp.WINDOW_HEIGHT);
        background1.setPreserveRatio(true);
        background2.setFitHeight(MainApp.WINDOW_HEIGHT);
        background2.setPreserveRatio(true);

        double width = bgImage.getWidth() * (MainApp.WINDOW_HEIGHT / bgImage.getHeight());

        background1.setLayoutX(0);
        background2.setLayoutX(width);
    }

    private void updateBackground() {
        background1.setLayoutX(background1.getLayoutX() - worldSpeed);
        background2.setLayoutX(background2.getLayoutX() - worldSpeed);

        double width = background1.getBoundsInLocal().getWidth();

        if (background1.getLayoutX() <= -width) {
            background1.setLayoutX(background2.getLayoutX() + width);
        }
        if (background2.getLayoutX() <= -width) {
            background2.setLayoutX(background1.getLayoutX() + width);
        }
    }

    private void createPlayer() {
        double groundLine = MainApp.WINDOW_HEIGHT - 130;
        duck = new Duck(200, groundLine);
    }

    private void setupControls() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (isPaused) resumeGame();
                else pauseGame();
                return;
            }
            if (isPaused) return;

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

    private void spawnEnemy(long now) {
        if (now < nextSpawnTime) return;

        double spawnX = MainApp.WINDOW_WIDTH + 100;
        Enemy enemy = null;

        if (spawnCats && spawnEagles) {
            if (Math.random() > 0.5) {
                double groundY = MainApp.WINDOW_HEIGHT - 130;
                enemy = new Cat(spawnX, groundY, worldSpeed);
            } else {
                double airY = MainApp.WINDOW_HEIGHT - 350;
                enemy = new Eagle(spawnX, airY, worldSpeed);
            }
        } else if (spawnCats) {
            double groundY = MainApp.WINDOW_HEIGHT - 130;
            enemy = new Cat(spawnX, groundY, worldSpeed);
        } else if (spawnEagles) {
            double airY = MainApp.WINDOW_HEIGHT - 350;
            enemy = new Eagle(spawnX, airY, worldSpeed);
        }

        if (enemy != null) {
            enemies.add(enemy);
            int uiIndex = root.getChildren().indexOf(pauseButton);
            if (uiIndex != -1) {
                root.getChildren().add(uiIndex, enemy.getNode());
            } else {
                root.getChildren().add(enemy.getNode());
            }
        }

        long delay = (long)((2 + Math.random() * 2) * 1_000_000_000);
        nextSpawnTime = now + delay;
    }

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

            if (!enemy.hasCollided() && CollisionUtil.isColliding(duck.getHitBox(), enemy.getHitBox())) {
                enemy.markCollided();
                healthBar.decreaseHealth();
                duck.hit(); // Call the hit method for visual feedback
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
        pauseMenu.setVisible(true, background1, background2);
        menuLayer.toFront();
        pauseMenu.getRoot().toFront();
        pauseButton.setVisible(false);
    }

    private void resumeGame() {
        if (!isPaused) return;
        isPaused = false;
        pauseMenu.setVisible(false, background1, background2);
        pauseButton.setVisible(true);
        root.requestFocus();
    }

    private void restartGame() {
        resumeGame();
        duck.resetState();
        background1.setLayoutX(0);
        background2.setLayoutX(background1.getBoundsInLocal().getWidth());

        for (Enemy e : enemies) {
            root.getChildren().remove(e.getNode());
        }
        enemies.clear();
        nextSpawnTime = 0;
        healthBar.reset();
    }

    private void openSettings() {
        pauseMenu.getRoot().setVisible(false);
        settingsMenu.setLayoutX((MainApp.WINDOW_WIDTH - 925) / 2.0);
        settingsMenu.setLayoutY((MainApp.WINDOW_HEIGHT - 546) / 2.0);
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
