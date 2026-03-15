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
import edu.bauet.java.cse.duckrun.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameScene {

    private Pane root;
    private Scene scene;
    private Duck duck;
    private AnimationTimer gameLoop;
    private StackPane menuLayer;

    private ImageView background1;
    private ImageView background2;
    private javafx.scene.image.Image transitionImage;

    private boolean isPaused = false;
    private Button pauseButton;
    private PauseMenu pauseMenu;
    private SettingsMenu settingsMenu;
    private final Level currentLevel;

    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Food> foods = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private long nextSpawnTime = 0;

    // Background scroll speed is now sourced directly from the Level.
    // Enemy speed is already embedded in each spawned entity via getWorldSpeed().
    private final double backgroundScrollSpeed;

    private HealthBar healthBar;
    private SleepBar sleepBar;
    private TimeUtil timeUtil;

    private final double groundY = MainApp.WINDOW_HEIGHT - 130;
    private final Random random = new Random();
    private final List<Integer> spawnHistory = new ArrayList<>();

    private long lastFrameTime = 0;

    // Tracks what killed the duck — set just before calling gameOver()
    private enum DeathCause { SLEEP, OBSTACLE, CAT, BOY, EAGLE }
    private DeathCause deathCause = null;

    // Level completion — track how far the background has scrolled
    private static final int    LOOPS_TO_COMPLETE   = 11;
    private int                  bgScrolledTotal     = 0;   // counts tiles that have wrapped
    private boolean              levelCompleted      = false;

    public GameScene(Level level) {
        this.currentLevel = level;
        // Pull the background scroll speed from the level instead of computing
        // it here — each level controls its own visual pacing.
        this.backgroundScrollSpeed = level.getBackgroundScrollSpeed();
        initialize(level.getBackgroundPath());
    }

    private void initialize(String backgroundPath) {
        root = new Pane();
        root.setPrefSize(MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
        root.setStyle("-fx-background-color: black;");

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

        timeUtil = new TimeUtil();
        Label timerLabel = new Label();
        timerLabel.textProperty().bind(timeUtil.timeProperty());
        try {
            Font pixelfont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 32);
            if (pixelfont != null) {
                timerLabel.setFont(pixelfont);
            } else {
                timerLabel.setFont(Font.font("Arial", 32));
            }
        } catch (Exception e) {
            timerLabel.setFont(Font.font("Arial", 32));
        }

        timerLabel.setTextFill(Color.web("#AE6819"));
        timerLabel.setPrefWidth(200);
        timerLabel.setAlignment(javafx.geometry.Pos.CENTER);
        timerLabel.setLayoutX((MainApp.WINDOW_WIDTH - 200) / 2.0);
        timerLabel.setLayoutY(20);

        DropShadow border = new DropShadow();
        border.setBlurType(BlurType.ONE_PASS_BOX);
        border.setColor(Color.BLACK);
        border.setRadius(4.0);
        border.setSpread(2.0);
        timerLabel.setEffect(border);

        createPauseSystem();

        root.getChildren().addAll(background1,
                background2,
                duck.getNode(),
                healthBar.getView(),
                sleepBar.getView(),
                timerLabel,
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
        pauseButton.setLayoutY(10);
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
        transitionImage = AssetLoader.getImage("/images/backgrounds/level1_transition.png");
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

    private void updateBackground(double deltaTime) {
        double moveAmount = backgroundScrollSpeed * deltaTime;

        background1.setLayoutX(background1.getLayoutX() - moveAmount);
        background2.setLayoutX(background2.getLayoutX() - moveAmount);

        double width = background1.getBoundsInParent().getWidth();

        if (background1.getLayoutX() <= -width) {
            background1.setLayoutX(background2.getLayoutX() + width);
            bgScrolledTotal++;
            onTileWrapped(background1);
        }
        if (background2.getLayoutX() <= -width) {
            background2.setLayoutX(background1.getLayoutX() + width);
            bgScrolledTotal++;
            onTileWrapped(background2);
        }

        // Victory: right edge of transition tile has reached right edge of screen
        // i.e. layoutX + width <= WINDOW_WIDTH means it's fully entered
        if (levelCompleted) {
            ImageView transitionTile = (background1.getImage() == transitionImage)
                    ? background1 : background2;
            double rightEdge = transitionTile.getLayoutX() + transitionTile.getBoundsInParent().getWidth();
            if (rightEdge <= MainApp.WINDOW_WIDTH) {
                levelCompleted = false; // prevent re-trigger
                showVictoryScreen();
            }
        }
    }

    private void onTileWrapped(ImageView tile) {
        // At loop 9 (0-indexed), swap the NEXT incoming tile to transition image
        // so it becomes the 10th tile (1-indexed = position 11 in the sequence)
        if (bgScrolledTotal == LOOPS_TO_COMPLETE - 1 && !levelCompleted) {
            tile.setImage(transitionImage);
            levelCompleted = true;
            timeUtil.stop();
            // Stop spawning but keep duck fully controllable
        }
    }

    private void createPlayer() {
        duck = new Duck(200, groundY);
        // Apply per-level jump and fall speeds to the duck.
        // Enemy speed is already handled inside each spawned entity.
        duck.setJumpSpeed(currentLevel.getDuckJumpSpeed());
        duck.setFallSpeed(currentLevel.getDuckFallSpeed());
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
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                duck.setCrouching(true);
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                duck.setCrouching(false);
            }
        });
    }

    private void spawnEntities(long now) {
        if (now < nextSpawnTime) return;

        double spawnX = MainApp.WINDOW_WIDTH + 50;
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

        long delay = (long) ((1.5 + Math.random() * 2.5) * 1_000_000_000);
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

    private void updateEnemies(double deltaTime) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(deltaTime);
            if (!enemy.isActive()) {
                root.getChildren().remove(enemy.getNode());
                iterator.remove();
                continue;
            }
            if (!enemy.hasCollided() && CollisionUtil.isColliding(duck.getHitBox(), enemy.getHitBox())) {
                enemy.markCollided();
                duck.hit();
                healthBar.decreaseHealth();
                sleepBar.decreaseSegment();
                timeUtil.increaseTime(5);
                if (healthBar.isDead()) {
                    if (enemy instanceof Cat)   deathCause = DeathCause.CAT;
                    else if (enemy instanceof Eagle) deathCause = DeathCause.EAGLE;
                    else if (enemy instanceof Boy)   deathCause = DeathCause.BOY;
                    else                             deathCause = DeathCause.CAT; // fallback
                    gameOver();
                }
            }
        }
    }

    private void updateFoods(double deltaTime) {
        Iterator<Food> iterator = foods.iterator();
        while (iterator.hasNext()) {
            Food food = iterator.next();
            food.update(deltaTime);
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
                timeUtil.increaseTime(10);
            }
        }
    }

    private void updateObstacles(double deltaTime) {
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.update(deltaTime);
            if (!obstacle.isActive()) {
                root.getChildren().remove(obstacle.getNode());
                iterator.remove();
                continue;
            }
            if (!obstacle.hasCollided() && CollisionUtil.isColliding(duck.getHitBox(), obstacle.getHitBox())) {
                obstacle.markCollided();
                duck.hit();
                healthBar.decreaseHealth();
                sleepBar.decreaseSegment();
                timeUtil.increaseTime(5);
                if (healthBar.isDead()) {
                    deathCause = DeathCause.OBSTACLE;
                    gameOver();
                }
            }
        }
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    return;
                }

                double deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
                lastFrameTime = now;

                if (!isPaused) {
                    duck.setSleepy(!sleepBar.isEmpty());
                    updateBackground(deltaTime);
                    duck.update(deltaTime);
                    if (!levelCompleted) spawnEntities(now);
                    updateEnemies(deltaTime);
                    updateFoods(deltaTime);
                    updateObstacles(deltaTime);

                    if (sleepBar.isFull()) {
                        deathCause = DeathCause.SLEEP;
                        gameOver();
                    }
                }
            }
        };
        timeUtil.start();
        gameLoop.start();
    }

    private void pauseGame() {
        if (isPaused) return;
        isPaused = true;
        timeUtil.stop();
        pauseMenu.setVisible(true, background1, background2);
        menuLayer.toFront();
        pauseMenu.getRoot().toFront();
        pauseButton.setVisible(false);
    }

    private void resumeGame() {
        if (!isPaused) return;
        isPaused = false;
        lastFrameTime = 0;
        timeUtil.start();
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
        duck.setSleepy(false);
        timeUtil.reset();
        spawnHistory.clear();
        bgScrolledTotal = 0;
        levelCompleted  = false;
        Image normalBg = AssetLoader.getImage(currentLevel.getBackgroundPath());
        background1.setImage(normalBg);
        background2.setImage(normalBg);
    }

    private void gameOver() {
        if (gameLoop == null) return;

        gameLoop.stop();
        gameLoop = null;
        timeUtil.stop();

        if (deathCause == DeathCause.SLEEP) {
            showGameOverScreen("/images/duck/sleeping.png");
        } else if (deathCause == DeathCause.OBSTACLE) {
            showGameOverScreen("/images/game_over/game_over_bump.png");
        } else if (deathCause == DeathCause.CAT) {
            showGameOverScreen("/images/game_over/game_over_cat.png");
        } else if (deathCause == DeathCause.BOY) {
            showGameOverScreen("/images/game_over/game_over_caught.png");
        } else if (deathCause == DeathCause.EAGLE) {
            showGameOverScreen("/images/game_over/game_over_eagle.png");
        } else {
            exitToMenu(); // fallback, should never happen
        }
    }

    private void showGameOverScreen(String imagePath) {
        // 1. Full-screen background image (e.g., your black.png or a darkened version)
        javafx.scene.shape.Rectangle darkOverlay = new javafx.scene.shape.Rectangle(
                MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT, Color.rgb(0, 0, 0, 0.8)
        );

        // 2. The Frame Container
        StackPane frameContainer = new StackPane();
        frameContainer.getStyleClass().add("game-over-frame-container");

        // Using the game_over_frame.png you uploaded
        ImageView frameView = new ImageView(AssetLoader.getImage("/images/game_over/game_over_frame.png"));
        frameView.setFitWidth(750); // Slightly larger to give the content room to breathe
        frameView.setPreserveRatio(true);

        // 3. Content Layout (Vertical Box)
        VBox contentLayout = new VBox(25);
        contentLayout.setAlignment(javafx.geometry.Pos.CENTER);

        // Title with CSS styling
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("game-over-title");

        // Death Image (dynamically chosen based on cause)
        ImageView deathView = new ImageView(AssetLoader.getImage(imagePath));
        deathView.setFitHeight(180);
        deathView.setPreserveRatio(true);

        // Buttons Row
        HBox buttonRow = new HBox(50);
        buttonRow.setAlignment(Pos.CENTER);

        Button restartBtn = new Button();
        restartBtn.setGraphic(createButtonIcon("/images/pause_menu/restart_button.png"));
        restartBtn.getStyleClass().add("game-over-button");
        restartBtn.setOnAction(e -> {
            GameScene fresh = new GameScene(currentLevel);
            MainApp.switchScene(fresh.getScene());
        });

        Button exitBtn = new Button();
        exitBtn.setGraphic(createButtonIcon("/images/pause_menu/exit_button.png"));
        exitBtn.getStyleClass().add("game-over-button");
        exitBtn.setOnAction(e -> exitToMenu());

        buttonRow.getChildren().addAll(restartBtn, exitBtn);
        contentLayout.getChildren().addAll(gameOverLabel, deathView, buttonRow);

        // Add frame and content to the stack
        frameContainer.getChildren().addAll(frameView, contentLayout);

        // Position the frame in the center of the screen
        // Using StackPane's layout properties or centering it manually:
        frameContainer.setTranslateX((MainApp.WINDOW_WIDTH - 750) / 2.0);
        frameContainer.setTranslateY((MainApp.WINDOW_HEIGHT - 480) / 2.0);

        // 4. Final assembly
        root.getChildren().addAll(darkOverlay, frameContainer);

        // Load the CSS if it's not present
        String css = Objects.requireNonNull(getClass().getResource("/styles/game_over.css")).toExternalForm();
        if (!scene.getStylesheets().contains(css)) {
            scene.getStylesheets().add(css);
        }
    }

    // Helper to keep code clean
    private ImageView createButtonIcon(String path) {
        ImageView iv = new ImageView(AssetLoader.getImage(path));
        iv.setFitWidth(100);
        iv.setPreserveRatio(true);
        return iv;
    }


    private void showVictoryScreen() {
        // Stop the game loop — freeze everything
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }

        // Clear remaining entities
        for (Enemy e : enemies) root.getChildren().remove(e.getNode());
        enemies.clear();
        for (Food f : foods) root.getChildren().remove(f.getNode());
        foods.clear();
        for (Obstacle o : obstacles) root.getChildren().remove(o.getNode());
        obstacles.clear();
        // 1. Dark overlay — mouse transparent so buttons behind it still work
        javafx.scene.shape.Rectangle darkOverlay = new javafx.scene.shape.Rectangle(
                MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT, Color.rgb(0, 0, 0, 0.8));
        darkOverlay.setMouseTransparent(true);

        // 2. Frame
        StackPane frameContainer = new StackPane();
        ImageView frameView = new ImageView(
                AssetLoader.getImage("/images/game_over/game_over_frame.png"));
        frameView.setFitWidth(750);
        frameView.setPreserveRatio(true);

        // 3. Content
        VBox contentLayout = new VBox(25);
        contentLayout.setAlignment(Pos.CENTER);

        Label levelPassedLabel = new Label("LEVEL PASSED!");
        levelPassedLabel.getStyleClass().add("game-over-title");

        ImageView victoryView = new ImageView(
                AssetLoader.getImage("/images/duck/victory.png"));
        victoryView.setFitHeight(180);
        victoryView.setPreserveRatio(true);

        // 4. Buttons row — restart | play/next | exit
        HBox buttonRow = new HBox(40);
        buttonRow.setAlignment(Pos.CENTER);

        // Restart — same level
        Button restartBtn = new Button();
        restartBtn.setGraphic(createButtonIcon("/images/pause_menu/restart_button.png"));
        restartBtn.getStyleClass().add("game-over-button");
        restartBtn.setOnAction(e -> {
            GameScene fresh = new GameScene(currentLevel);
            MainApp.switchScene(fresh.getScene());
        });

        // Play — next level OR main menu if on level 3
        Button playBtn = new Button();
        playBtn.setGraphic(createButtonIcon("/images/pause_menu/play_button.png"));
        playBtn.getStyleClass().add("game-over-button");
        playBtn.setOnAction(e -> {
            Level nextLevel = getNextLevel();
            if (nextLevel != null) {
                GameScene next = new GameScene(nextLevel);
                MainApp.switchScene(next.getScene());
            } else {
                exitToMenu(); // Level 3 completed — go to main menu
            }
        });

        // Exit — main menu
        Button exitBtn = new Button();
        exitBtn.setGraphic(createButtonIcon("/images/pause_menu/exit_button.png"));
        exitBtn.getStyleClass().add("game-over-button");
        exitBtn.setOnAction(e -> exitToMenu());

        buttonRow.getChildren().addAll(restartBtn, playBtn, exitBtn);
        contentLayout.getChildren().addAll(levelPassedLabel, victoryView, buttonRow);

        frameContainer.getChildren().addAll(frameView, contentLayout);
        frameContainer.setTranslateX((MainApp.WINDOW_WIDTH - 750) / 2.0);
        frameContainer.setTranslateY((MainApp.WINDOW_HEIGHT - 480) / 2.0);

        root.getChildren().addAll(darkOverlay, frameContainer);

        String css = Objects.requireNonNull(
                getClass().getResource("/styles/game_over.css")).toExternalForm();
        if (!scene.getStylesheets().contains(css)) {
            scene.getStylesheets().add(css);
        }
    }

    /** Returns the next level, or null if currentLevel is Level3. */
    private Level getNextLevel() {
        if (currentLevel instanceof edu.bauet.java.cse.duckrun.levels.Level1) {
            return new edu.bauet.java.cse.duckrun.levels.Level2(groundY);
        } else if (currentLevel instanceof edu.bauet.java.cse.duckrun.levels.Level2) {
            return new edu.bauet.java.cse.duckrun.levels.Level3(groundY);
        } else {
            return null; // Level3 — no next level
        }
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
        if (gameLoop != null) {
            gameLoop.stop();
        }
        MenuScene menuScene = new MenuScene(MainApp.getPrimaryStage());
        MainApp.switchScene(menuScene.createScene());
    }

    public Scene getScene() {
        return scene;
    }
}