package edu.bauet.java.cse.duckrun.ui;

import edu.bauet.java.cse.duckrun.MainApp;
import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MenuDuck {

    // ── tuneable constants ────────────────────────────────────────────────────

    /** Rendered height of the duck sprite (px). Adjust to taste. */
    private static final double DUCK_DISPLAY_HEIGHT = 60;

    /**
     * Distance from the bottom of the parent pane to the duck's feet (px).
     * Match this to the ground strip in menubg.jpeg.
     * Positive = higher up, 0 = flush with very bottom.
     */
    private static final double DUCK_Y_OFFSET = 27;

    /** How fast the duck walks across the screen (pixels per second). */
    private static final double DUCK_SPEED = 150;

    /**
     * Animation frame toggle interval in game-loop ticks (~60 ticks/sec).
     * Lower = faster leg animation. 10 ≈ fast run, 18 ≈ casual walk.
     */
    private static final int FRAME_THRESHOLD = 12;

    /** Minimum seconds to wait before spawning the duck again after it exits. */
    private static final double SPAWN_INTERVAL_MIN = 4.0;

    /** Maximum seconds to wait before spawning the duck again after it exits. */
    private static final double SPAWN_INTERVAL_MAX = 8.0;

    // ─────────────────────────────────────────────────────────────────────────

    private final Pane parent;
    private final double W;
    private final double H;

    private final ImageView duckView;
    private final Image runningImage;
    private final Image runningMidImage;

    private AnimationTimer timer;
    private long lastNano = -1;

    // animation state
    private int     frameCounter = 0;
    private boolean toggleFrame  = false;

    // spawn / active state
    private boolean active         = false;  // duck is currently on screen
    private double  spawnCountdown = 0;      // seconds until next spawn
    private double  duckX          = 0;
    private double  duckY          = 0;
    private boolean goingRight     = true;   // current travel direction

    private final java.util.Random rng = new java.util.Random();

    public MenuDuck(Pane parent) {
        this.parent = parent;
        this.W = MainApp.WINDOW_WIDTH;
        this.H = MainApp.WINDOW_HEIGHT;

        runningImage    = AssetLoader.getImage("/images/duck/running.png");
        runningMidImage = AssetLoader.getImage("/images/duck/running_mid_point.png");

        duckView = new ImageView(runningImage);
        duckView.setFitHeight(DUCK_DISPLAY_HEIGHT);
        duckView.setPreserveRatio(true);
        duckView.setVisible(false);

        parent.getChildren().add(duckView);

        // first spawn — short initial delay so it doesn't appear instantly
        spawnCountdown = 2.0 + rng.nextDouble() * 4.0;
    }

    // ── public lifecycle ──────────────────────────────────────────────────────

    public void start() {
        lastNano = -1;
        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (lastNano < 0) { lastNano = now; return; }
                double dt = (now - lastNano) / 1_000_000_000.0;
                lastNano = now;
                update(dt);
            }
        };
        timer.start();
    }

    public void stop() {
        if (timer != null) { timer.stop(); timer = null; }
        duckView.setVisible(false);
    }

    // ── private update loop ───────────────────────────────────────────────────

    private void update(double dt) {
        if (active) {
            // move duck in current direction
            duckX += (goingRight ? 1 : -1) * DUCK_SPEED * dt;
            duckView.setLayoutX(duckX);

            // animate legs
            frameCounter++;
            if (frameCounter >= FRAME_THRESHOLD) {
                toggleFrame = !toggleFrame;
                frameCounter = 0;
                // slight vertical bob: -2px on one frame, 0 on the other
                double runOffset = toggleFrame ? -2 : 0;
                duckView.setImage(toggleFrame ? runningImage : runningMidImage);
                duckView.setLayoutY(duckY + runOffset);
            }

            // duck has fully left the screen edge → deactivate and schedule next spawn
            boolean exited = goingRight
                    ? duckX > W + duckView.getFitWidth() + 20    // off right edge
                    : duckX < -duckView.getFitWidth() - 20;      // off left edge
            if (exited) {
                deactivate();
            }

        } else {
            // count down to next spawn
            spawnCountdown -= dt;
            if (spawnCountdown <= 0) {
                spawn();
            }
        }
    }

    private void spawn() {
        // randomly pick a direction each time
        goingRight = rng.nextBoolean();

        double duckWidth = duckView.getFitWidth();

        // start just off the appropriate edge
        duckX = goingRight
                ? -duckWidth - 10      // off left edge, walking right
                : W + duckWidth + 10;  // off right edge, walking left

        duckY = H - DUCK_DISPLAY_HEIGHT - DUCK_Y_OFFSET;

        // flip the sprite horizontally when walking left
        duckView.setScaleX(goingRight ? 1.0 : -1.0);

        duckView.setLayoutX(duckX);
        duckView.setLayoutY(duckY);
        duckView.setImage(runningImage);
        duckView.setVisible(true);

        frameCounter = 0;
        toggleFrame  = false;
        active       = true;
    }

    private void deactivate() {
        active = false;
        duckView.setVisible(false);
        spawnCountdown = SPAWN_INTERVAL_MIN
                + rng.nextDouble() * (SPAWN_INTERVAL_MAX - SPAWN_INTERVAL_MIN);
    }
}