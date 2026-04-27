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
    private static final double CLOUD_Y_MIN = -0.06;  // -4% above top
    private static final double CLOUD_Y_MAX =  0.30;  // 20% from top

    // ─────────────────────────────────────────────────────────────────────────

    private final double W = MainApp.WINDOW_WIDTH;
    private final double H = MainApp.WINDOW_HEIGHT;

    private final List<CloudSprite> clouds      = new ArrayList<>();
    private final List<Image>       cloudImages = new ArrayList<>();
    private AnimationTimer timer;
    private long lastNano = -1;
    private final Random rng = new Random();

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
    }

    public void stop() {
        if (timer != null) { timer.stop(); timer = null; }
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

        // ── Layer 2: building ─────────────────────────────────────────────────
        Image buildingImg = AssetLoader.getImage("/images/ui/menu/bauet.png");
        ImageView buildingView = new ImageView(buildingImg);
        buildingView.setFitHeight(H * 0.80);  // drive by height to preserve tall proportions
        buildingView.setFitWidth(W * 0.70);           // width follows automatically
        //buildingView.setPreserveRatio(true);

        buildingView.setLayoutX(W * 0.30);     // horizontal position — tweak as needed

        double groundStripHeight = H * 0.1;   // matches ground strip in menubg
        buildingView.setLayoutY(H - buildingView.getFitHeight() - groundStripHeight + 36);

        // Reposition once image is fully loaded (actual rendered height may differ)
        buildingImg.progressProperty().addListener((obs, o, n) -> {
            if (n.doubleValue() >= 1.0) {
                double renderedH = buildingView.getFitHeight();
                buildingView.setLayoutY(H - renderedH - groundStripHeight + 40);
            }
        });
        getChildren().add(buildingView);
    }

    /** Returns true for cloud1 and cloud2, which should render larger. */
    private boolean isLargeCloud(Image img) {
        String url = img.getUrl();
        // Null check added to prevent the crash
        if (url == null) return false;
        return url.contains("cloud1") || url.contains("cloud2");
    }

    /** Create a cloud sprite, optionally scattering it across the screen (for init). */
    private CloudSprite createCloud(boolean scatterInitially) {
        ImageView iv = new ImageView();
        Image img = cloudImages.get(rng.nextInt(cloudImages.size()));
        iv.setImage(img);

        boolean isLarge = isLargeCloud(img); //
        double cloudW = isLarge
                ? 260 + rng.nextDouble() * 180
                : 100 + rng.nextDouble() * 80;

        iv.setFitWidth(cloudW);
        iv.setPreserveRatio(true);
        iv.setOpacity(0.70 + rng.nextDouble() * 0.30);

        double speedMultiplier = isLarge ? 1.5 : 0.8;
        double speed = BASE_SPEED * speedMultiplier * (0.5 + (1.0 - (cloudW - 140) / 240.0) * 1.2);

        // --- ADJUSTMENT START ---
        double yMin = CLOUD_Y_MIN; // Both start at -0.04
        double yMax = isLarge ? 0.05 : CLOUD_Y_MAX;
        double y = yMin * H + rng.nextDouble() * (yMax - yMin) * H;
        // --- ADJUSTMENT END ---

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

        // --- ADJUSTMENT START ---
        double yMin = CLOUD_Y_MIN;
        double yMax = isLarge ? 0.05 : CLOUD_Y_MAX;
        double y = yMin * H + rng.nextDouble() * (yMax - yMin) * H;
        // --- ADJUSTMENT END ---

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