package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.entities.*;
import edu.bauet.java.cse.duckrun.levels.Level;
import edu.bauet.java.cse.duckrun.ui.PauseMenu;
import edu.bauet.java.cse.duckrun.ui.SettingsMenu;
import edu.bauet.java.cse.duckrun.ui.SleepBar;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import edu.bauet.java.cse.duckrun.utils.CollisionUtil;
import edu.bauet.java.cse.duckrun.ui.HealthBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
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
    private final Level currentLevel;

    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Food> foods = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private long nextSpawnTime = 0;

    private final double worldSpeed;

    private HealthBar healthBar;
    private SleepBar sleepBar;

    private final double groundY = MainApp.WINDOW_HEIGHT - 130;
    private final Random random = new Random();
    private final List<Integer> spawnHistory = new ArrayList<>();

    public GameScene(Level level) {
        this.currentLevel = level;
        this.worldSpeed = level.getWorldSpeed();
        initialize(level.getBackgroundPath());
    }

    private void initialize(String backgroundPath) {
        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);

        menuLayer = new StackPane();
        menuLayer.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        menuLayer.setPickOnBounds(false);

        scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/pause_menu.css")).toExternalForm());

        createBackground(backgroundPath);
        createPlayer();

        healthBar = new HealthBar(3);
        healthBar.getView().setLayoutX(20);
        healthBar.getView().setLayoutY(20);
        
        sleepBar = new SleepBar();
        sleepBar.getView().setLayoutX(20);
        sleepBar.getView().setLayoutY(60);

        createPauseSystem();

        root.getChildren().addAll(background1,
                background2,
                duck.getNode(),
                healthBar.getView(),
                sleepBar.getView(),
                pauseButton,
                menuLayer
        );

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
        pauseButton.setCursor(Cursor.HAND);
        pauseButton.getStyleClass().add("pause-button");

        pauseButton.setOnAction(e -> {
            if (isPaused) resumeGame();
            else pauseGame();
        });

        pauseMenu = new PauseMenu(this::resumeGame,
                this::restartGame,
                this::openSettings,
                this::exitToMenu
        );

        pauseMenu.getRoot().setVisible(false);

        settingsMenu = new SettingsMenu(() -> {
            settingsMenu.setVisible(false);
            pauseMenu.getRoot().getChildren().forEach(node -> node.setVisible(true));
            pauseMenu.getRoot().toFront();
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
        duck = new Duck(200, groundY);
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

    private void spawnEntities(long now) {
        if (now < nextSpawnTime) return;

        double spawnX = MainApp.WINDOW_WIDTH + 100;
        int entityType;

        if (spawnHistory.size() == 2 && spawnHistory.get(0).equals(spawnHistory.get(1))) {
            int lastSpawnedType = spawnHistory.get(0);
            List<Integer> possibleTypes = IntStream.range(0, 3)
                                                   .filter(i -> i != lastSpawnedType)
                                                   .boxed()
                                                   .collect(Collectors.toList());
            entityType = possibleTypes.get(random.nextInt(possibleTypes.size()));
        } else {
            entityType = random.nextInt(3);
        }

        switch (entityType) {
            case 0:
                Enemy enemy = currentLevel.spawnEnemy(spawnX);
                if (enemy != null) {
                    enemies.add(enemy);
                    addNodeToScene(enemy.getNode());
                }
                break;
            case 1:
                Food food = currentLevel.spawnFood(spawnX);
                if (food != null) {
                    foods.add(food);
                    addNodeToScene(food.getNode());
                }
                break;
            case 2:
                Obstacle obstacle = currentLevel.spawnObstacle(spawnX);
                if (obstacle != null) {
                    obstacles.add(obstacle);
                    addNodeToScene(obstacle.getNode());
                }
                break;
        }

        spawnHistory.add(entityType);
        if (spawnHistory.size() > 2) {
            spawnHistory.remove(0);
        }

        long delay = (long)((1.5 + Math.random() * 2.5) * 1_000_000_000);
        nextSpawnTime = now + delay;
    }
    
    private void addNodeToScene(javafx.scene.Node node) {
        int uiIndex = root.getChildren().indexOf(pauseButton);
        if (uiIndex != -1) {
            root.getChildren().add(uiIndex, node);
        } else {
            root.getChildren().add(node);
        }
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
                duck.hit();
                healthBar.decreaseHealth();
                if (healthBar.isDead()) {
                    System.out.println("GAME OVER");
                }
            }
        }
    }

    private void updateFoods() {
        Iterator<Food> iterator = foods.iterator();
        while (iterator.hasNext()) {
            Food food = iterator.next();
            food.update();
            if (!food.isActive()) {
                root.getChildren().remove(food.getNode());
                iterator.remove();
                continue;
            }
            if (CollisionUtil.isColliding(duck.getHitBox(), food.getHitBox())) {
                food.deactivate();
                if (!healthBar.isFull()) {
                    healthBar.increaseHealth();
                }
                sleepBar.addSegment();
                duck.powerUp();
            }
        }
    }

    private void updateObstacles() {
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.update();
            if (!obstacle.isActive()) {
                root.getChildren().remove(obstacle.getNode());
                iterator.remove();
                continue;
            }
            if (!obstacle.hasCollided() && CollisionUtil.isColliding(duck.getHitBox(), obstacle.getHitBox())) {
                obstacle.markCollided();
                duck.hit();
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
                    spawnEntities(now);
                    updateEnemies();
                    updateFoods();
                    updateObstacles();
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

        for (Food f : foods) {
            root.getChildren().remove(f.getNode());
        }
        foods.clear();
        
        for (Obstacle o : obstacles) {
            root.getChildren().remove(o.getNode());
        }
        obstacles.clear();
        
        nextSpawnTime = 0;
        healthBar.reset();
        sleepBar.reset();
        spawnHistory.clear();
    }

    private void openSettings() {
        pauseMenu.getRoot().getChildren().forEach(node -> {
            if (!(node instanceof javafx.scene.shape.Rectangle)) {
                node.setVisible(false);
            }
        });

        settingsMenu.setVisible(true);
        settingsMenu.toFront();
        menuLayer.toFront();
        isPaused = true;
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
