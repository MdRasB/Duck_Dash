package edu.bauet.java.cse.duckrun.ui;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MenuBackground extends Pane {

    // ── tuneable constants ────────────────────────────────────────────────────

    /** Number of cloud sprites on screen at once. */
    private static final int CLOUD_COUNT = 5;

    /** Base pixel-per-second speed. */
    private static final double BASE_SPEED = 28.0;

    /** Vertical band (fraction of window height) where clouds appear.
     *  Negative values go above the screen top. */
    private static final double CLOUD_Y_MIN = -0.06;
    private static final double CLOUD_Y_MAX =  0.30;

    // ─────────────────────────────────────────────────────────────────────────

    private final double W = MainApp.WINDOW_WIDTH;
    private final double H = MainApp.WINDOW_HEIGHT;

    private final List<CloudSprite> clouds      = new ArrayList<>();
    private final List<Image>       cloudImages = new ArrayList<>();
    private AnimationTimer timer;
    private long lastNano = -1;
    private final Random rng = new Random();

    private MenuDuck menuDuck;   // ← duck layer

    public MenuBackground() {
        setPrefSize(W, H);
        setMaxSize(W, H);
        setMinSize(W, H);
        buildLayers();
    }

    // ── public lifecycle ──────────────────────────────────────────────────────

    public void start() {
        lastNano = -1;
        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (lastNano < 0) { lastNano = now; return; }
                double dt = (now - lastNano) / 1_000_000_000.0;
                lastNano = now;
                moveClouds(dt);
            }
        };
        timer.start();
        menuDuck.start();   // ← start duck animation
    }

    public void stop() {
        if (timer != null) { timer.stop(); timer = null; }
        menuDuck.stop();    // ← stop duck animation
    }

    // ── private helpers ───────────────────────────────────────────────────────

    private void buildLayers() {

        // ── Layer 0: background (sky + ground) ───────────────────────────────
        Image bgImg = AssetLoader.getImage("/images/ui/menu/menubg.jpeg");
        ImageView bgView = new ImageView(bgImg);
        bgView.setFitWidth(W);
        bgView.setFitHeight(H);
        bgView.setPreserveRatio(false);
        getChildren().add(bgView);

        // ── Load cloud images ─────────────────────────────────────────────────
        for (int i = 1; i <= 5; i++) {
            Image img = AssetLoader.getImage("/images/ui/menu/clouds/cloud" + i + ".png");
            if (img != null) cloudImages.add(img);
        }
        if (cloudImages.isEmpty()) {
            System.err.println("[MenuBackground] No cloud images found – cloud layer skipped.");
        }

        // ── Layer 1: clouds ───────────────────────────────────────────────────
        for (int i = 0; i < CLOUD_COUNT; i++) {
            CloudSprite cs = createCloud(true);
            clouds.add(cs);
            getChildren().add(cs.view);
        }

        // ── Layer 3: building ─────────────────────────────────────────────────
        Image buildingImg = AssetLoader.getImage("/images/ui/menu/bauet.png");
        ImageView buildingView = new ImageView(buildingImg);
        buildingView.setFitHeight(H * 0.80);
        buildingView.setFitWidth(W * 0.70);

        buildingView.setLayoutX(W * 0.30);

        double groundStripHeight = H * 0.1;
        buildingView.setLayoutY(H - buildingView.getFitHeight() - groundStripHeight + 36);

        buildingImg.progressProperty().addListener((obs, o, n) -> {
            if (n.doubleValue() >= 1.0) {
                double renderedH = buildingView.getFitHeight();
                buildingView.setLayoutY(H - renderedH - groundStripHeight + 40);
            }
        });
        getChildren().add(buildingView);

        // ── Layer 2: duck (runs in front of everything) ─────────
        menuDuck = new MenuDuck(this);
    }

    /** Returns true for cloud1 and cloud2, which should render larger. */
    private boolean isLargeCloud(Image img) {
        String url = img.getUrl();
        if (url == null) return false;
        return url.contains("cloud1") || url.contains("cloud2");
    }

    /** Create a cloud sprite, optionally scattering it across the screen (for init). */
    private CloudSprite createCloud(boolean scatterInitially) {
        ImageView iv = new ImageView();
        Image img = cloudImages.get(rng.nextInt(cloudImages.size()));
        iv.setImage(img);

        boolean isLarge = isLargeCloud(img);
        double cloudW = isLarge
                ? 260 + rng.nextDouble() * 180
                : 100 + rng.nextDouble() * 80;

        iv.setFitWidth(cloudW);
        iv.setPreserveRatio(true);
        iv.setOpacity(0.70 + rng.nextDouble() * 0.30);

        double speedMultiplier = isLarge ? 1.5 : 0.8;
        double speed = BASE_SPEED * speedMultiplier * (0.5 + (1.0 - (cloudW - 140) / 240.0) * 1.2);

        double yMin = CLOUD_Y_MIN;
        double yMax = isLarge ? 0.05 : CLOUD_Y_MAX;
        double y = yMin * H + rng.nextDouble() * (yMax - yMin) * H;

        double x = scatterInitially
                ? -iv.getFitWidth() + rng.nextDouble() * (W + iv.getFitWidth())
                : -iv.getFitWidth() - 10;

        iv.setLayoutX(x);
        iv.setLayoutY(y);

        return new CloudSprite(iv, speed);
    }

    private void moveClouds(double dt) {
        for (CloudSprite cs : clouds) {
            cs.view.setLayoutX(cs.view.getLayoutX() + cs.speed * dt);
            if (cs.view.getLayoutX() > W + 10) {
                recycleCloud(cs);
            }
        }
    }

    private void recycleCloud(CloudSprite cs) {
        Image img = cloudImages.get(rng.nextInt(cloudImages.size()));
        cs.view.setImage(img);

        boolean isLarge = isLargeCloud(img);
        double cloudW = isLarge
                ? 260 + rng.nextDouble() * 180
                : 100 + rng.nextDouble() * 80;

        cs.view.setFitWidth(cloudW);

        double speedMultiplier = isLarge ? 1.5 : 0.8;
        cs.speed = BASE_SPEED * speedMultiplier * (0.5 + (1.0 - (cloudW - 140) / 240.0) * 1.2);

        double yMin = CLOUD_Y_MIN;
        double yMax = isLarge ? 0.05 : CLOUD_Y_MAX;
        double y = yMin * H + rng.nextDouble() * (yMax - yMin) * H;

        cs.view.setLayoutX(-cloudW - rng.nextDouble() * 120);
        cs.view.setLayoutY(y);
        cs.view.setOpacity(0.70 + rng.nextDouble() * 0.30);
    }

    // ── inner class ───────────────────────────────────────────────────────────

    private static class CloudSprite {
        ImageView view;
        double speed;
        CloudSprite(ImageView view, double speed) {
            this.view = view;
            this.speed = speed;
        }
    }
}