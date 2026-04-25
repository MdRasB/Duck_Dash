package edu.bauet.java.cse.duckrun.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;

/**
 * LevelProgressBar
 *
 * A bottom-of-screen HUD component that shows level completion progress.
 * A duck icon slides left-to-right along a green-filling bar,
 * with a victory flag at the right end.
 *
 * Usage in GameScene:
 *   1. Instantiate once:  progressBar = new LevelProgressBar(screenW, screenH);
 *   2. Set level length:  progressBar.setLevelLength(totalLevelPixels);
 *   3. Each frame:        progressBar.update(distanceTravelled);
 *                         progressBar.draw(gc);
 */
public class LevelProgressBar {

    // ── Layout constants ────────────────────────────────────────────────────
    private static final double BAR_HEIGHT      = 10.0;
    private static final double MARGIN_X        = 50.0;   // left/right screen margin
    private static final double BOTTOM_OFFSET   = 22.0;   // distance from screen bottom
    private static final double DUCK_SIZE       = 30.0;
    private static final double FLAG_POLE_W     = 3.0;
    private static final double FLAG_POLE_H     = 30.0;
    private static final double FLAG_CLOTH_W    = 16.0;
    private static final double FLAG_CLOTH_H    = 12.0;

    // ── Colors ───────────────────────────────────────────────────────────────
    private static final Color COLOR_TRACK_BG   = Color.rgb(20,  20,  20,  0.70);
    private static final Color COLOR_TRACK_DONE = Color.rgb(50,  220, 80,  1.00);
    private static final Color COLOR_TRACK_TODO = Color.rgb(70,  70,  70,  0.85);
    private static final Color COLOR_BORDER     = Color.rgb(255, 255, 255, 0.18);
    private static final Color COLOR_FLAG_POLE  = Color.rgb(240, 240, 240);
    private static final Color COLOR_FLAG_WHITE = Color.rgb(255, 255, 255);
    private static final Color COLOR_START_DOT  = Color.rgb(180, 180, 180);
    private static final Color COLOR_DUCK_BODY  = Color.rgb(255, 220, 40);
    private static final Color COLOR_DUCK_BEAK  = Color.rgb(255, 140, 0);
    private static final Color COLOR_DUCK_EYE   = Color.rgb(30,  30,  30);
    private static final Color COLOR_DUCK_WING  = Color.rgb(210, 175, 25);

    // ── State ────────────────────────────────────────────────────────────────
    private final double screenWidth;
    private final double screenHeight;

    private double levelLength  = 5000.0; // total level distance in game-world pixels
    private double progress     = 0.0;    // 0.0 → 1.0
    private double smoothProgress = 0.0;  // lerped version for smooth animation
    private boolean completed   = false;

    // Optional sprite images (loaded from AssetLoader or resources)
    private Image duckSprite;
    private Image flagSprite;

    // ── Derived geometry (computed once or on resize) ─────────────────────
    private final double barX;      // left edge of the actual track
    private final double barWidth;  // width of the track
    private final double barY;      // top of the track
    private final double barCenterY;

    // ── Constructor ──────────────────────────────────────────────────────────

    public LevelProgressBar(double screenWidth, double screenHeight) {
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;

        // Reserve space for duck icon on left and flag on right
        barX      = MARGIN_X + DUCK_SIZE * 0.5;
        barWidth  = screenWidth - (MARGIN_X * 2) - DUCK_SIZE;
        barY      = screenHeight - BOTTOM_OFFSET - BAR_HEIGHT;
        barCenterY = barY + BAR_HEIGHT / 2.0;

        tryLoadSprites();
    }

    // ── Public API ───────────────────────────────────────────────────────────

    /** Set the total distance of the current level (in game-world units / pixels). */
    public void setLevelLength(double levelLength) {
        this.levelLength = Math.max(1.0, levelLength);
        reset();
    }

    /** Call every frame with how far the player/world has scrolled since level start. */
    public void update(double distanceTravelled) {
        progress = Math.min(1.0, Math.max(0.0, distanceTravelled / levelLength));
        if (progress >= 1.0) completed = true;

        // Smooth lerp — makes the duck glide rather than snap
        // When completed, lerp faster (0.18) so the duck visibly rushes to the flag
        double lerpSpeed = completed ? 0.18 : 0.10;
        smoothProgress += (progress - smoothProgress) * lerpSpeed;

        // Snap to exactly 1.0 once close enough so duck always reaches the flag
        if (smoothProgress > 0.992) smoothProgress = 1.0;
    }

    /**
     * Drive the bar with a pre-computed 0.0→1.0 value directly.
     * Used during the duck run-off phase when scroll is no longer tracked.
     */
    public void updateDirect(double targetProgress) {
        progress = Math.min(1.0, Math.max(0.0, targetProgress));
        if (progress >= 1.0) completed = true;

        double lerpSpeed = 0.08;
        smoothProgress += (progress - smoothProgress) * lerpSpeed;
        if (smoothProgress > 0.992) smoothProgress = progress >= 1.0 ? 1.0 : smoothProgress;
    }

    /** Reset bar to zero (call when a new level starts). */
    public void reset() {
        progress       = 0.0;
        smoothProgress = 0.0;
        completed      = false;
    }

    /** Returns true once progress reaches 1.0. */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Draw the progress bar onto the given GraphicsContext.
     * Call this AFTER rendering all game-world elements so the HUD appears on top.
     */
    public void draw(GraphicsContext gc) {
        gc.save();

        drawTrackBackground(gc);
        drawFilledTrack(gc);
        drawStartDot(gc);
        drawFlag(gc);
        drawDuck(gc);

        gc.restore();
    }

    // ── Private draw helpers ─────────────────────────────────────────────────

    private void drawTrackBackground(GraphicsContext gc) {
        double trackPad = 3.0;
        // Outer container/pill shape
        gc.setFill(COLOR_TRACK_BG);
        gc.fillRoundRect(
                barX - trackPad,
                barY - trackPad,
                barWidth + trackPad * 2,
                BAR_HEIGHT + trackPad * 2,
                (BAR_HEIGHT + trackPad * 2),
                (BAR_HEIGHT + trackPad * 2)
        );

        // Subtle border highlight
        gc.setStroke(COLOR_BORDER);
        gc.setLineWidth(1.0);
        gc.strokeRoundRect(
                barX - trackPad,
                barY - trackPad,
                barWidth + trackPad * 2,
                BAR_HEIGHT + trackPad * 2,
                (BAR_HEIGHT + trackPad * 2),
                (BAR_HEIGHT + trackPad * 2)
        );

        // Empty (grey) track
        gc.setFill(COLOR_TRACK_TODO);
        gc.fillRoundRect(barX, barY, barWidth, BAR_HEIGHT, BAR_HEIGHT, BAR_HEIGHT);
    }

    private void drawFilledTrack(GraphicsContext gc) {
        double filled = barWidth * smoothProgress;
        if (filled < 1.0) return;

        // Green gradient fill with a lighter shine on top
        LinearGradient greenGrad = new LinearGradient(
                0, barY, 0, barY + BAR_HEIGHT, false, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.rgb(100, 255, 120)),
                new Stop(0.5, Color.rgb(50,  220, 80)),
                new Stop(1.0, Color.rgb(30,  160, 55))
        );
        gc.setFill(greenGrad);
        gc.fillRoundRect(barX, barY, filled, BAR_HEIGHT, BAR_HEIGHT, BAR_HEIGHT);

        // Gloss shine strip
        gc.setFill(Color.rgb(255, 255, 255, 0.22));
        gc.fillRoundRect(barX + 2, barY + 1, Math.max(0, filled - 4), BAR_HEIGHT * 0.45, 3, 3);
    }

    private void drawStartDot(GraphicsContext gc) {
        double r = 5.0;
        gc.setFill(COLOR_START_DOT);
        gc.fillOval(barX - r, barCenterY - r, r * 2, r * 2);
        gc.setStroke(Color.rgb(255, 255, 255, 0.4));
        gc.setLineWidth(1.0);
        gc.strokeOval(barX - r, barCenterY - r, r * 2, r * 2);
    }

    private void drawFlag(GraphicsContext gc) {
        double poleX = barX + barWidth + 6;
        double poleBottom = barY + BAR_HEIGHT + 4;
        double poleTop    = poleBottom - FLAG_POLE_H;

        // Pole
        gc.setStroke(COLOR_FLAG_POLE);
        gc.setLineWidth(FLAG_POLE_W);
        gc.strokeLine(poleX, poleBottom, poleX, poleTop);

        // Flag cloth (white pennant shape)
        double[] fx = { poleX, poleX + FLAG_CLOTH_W, poleX };
        double[] fy = { poleTop, poleTop + FLAG_CLOTH_H * 0.5, poleTop + FLAG_CLOTH_H };

        if (flagSprite != null) {
            gc.drawImage(flagSprite, poleX, poleTop - 4, FLAG_CLOTH_W + 6, FLAG_CLOTH_H + 8);
        } else {
            gc.setFill(COLOR_FLAG_WHITE);
            gc.fillPolygon(fx, fy, 3);
            gc.setStroke(Color.rgb(200, 200, 200, 0.6));
            gc.setLineWidth(0.5);
            gc.strokePolygon(fx, fy, 3);
        }

        // Small glow dot at pole base (on the track)
        gc.setFill(Color.rgb(255, 255, 255, 0.60));
        gc.fillOval(poleX - 4, barCenterY - 4, 8, 8);
    }

    private void drawDuck(GraphicsContext gc) {
        // At smoothProgress=0.0 duck starts at barX (left dot).
        // At smoothProgress=1.0 duck should be right against the flag pole.
        // Flag pole is at barX + barWidth + 6, so duck right edge should touch that.
        double duckX = barX + (barWidth * smoothProgress) - (DUCK_SIZE / 2.0);
        // Clamp so duck never overlaps the flag pole base
        double maxDuckX = barX + barWidth - (DUCK_SIZE / 2.0);
        duckX = Math.min(duckX, maxDuckX);
        double duckY = barCenterY - (DUCK_SIZE / 2.0);

        if (duckSprite != null) {
            gc.drawImage(duckSprite, duckX, duckY, DUCK_SIZE, DUCK_SIZE);
        } else {
            drawDuckFallback(gc, duckX, duckY, DUCK_SIZE);
        }
    }

    /**
     * Draws a simple but recognisable duck shape when no sprite is available.
     * Body: yellow oval. Wing: darker oval. Beak: orange triangle. Eye: black dot.
     */
    private void drawDuckFallback(GraphicsContext gc, double x, double y, double size) {
        double s = size;

        // Shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.25));
        gc.fillOval(x + s * 0.1, y + s * 0.75, s * 0.8, s * 0.18);

        // Body
        gc.setFill(COLOR_DUCK_BODY);
        gc.fillOval(x + s * 0.1, y + s * 0.30, s * 0.80, s * 0.55);

        // Wing
        gc.setFill(COLOR_DUCK_WING);
        gc.fillOval(x + s * 0.22, y + s * 0.44, s * 0.45, s * 0.28);

        // Head
        gc.setFill(COLOR_DUCK_BODY);
        gc.fillOval(x + s * 0.55, y + s * 0.08, s * 0.40, s * 0.38);

        // Beak (small triangle pointing right)
        gc.setFill(COLOR_DUCK_BEAK);
        gc.fillPolygon(
                new double[]{ x + s * 0.88, x + s * 1.05, x + s * 0.88 },
                new double[]{ y + s * 0.20, y + s * 0.28,  y + s * 0.36 },
                3
        );

        // Eye
        gc.setFill(COLOR_DUCK_EYE);
        gc.fillOval(x + s * 0.75, y + s * 0.14, s * 0.10, s * 0.10);

        // White eye shine
        gc.setFill(Color.WHITE);
        gc.fillOval(x + s * 0.77, y + s * 0.14, s * 0.04, s * 0.04);
    }

    // ── Sprite loading ───────────────────────────────────────────────────────

    /**
     * Attempts to load duck and flag sprites from your resources directory.
     * Silently falls back to vector drawing if the files don't exist.
     * Update the paths to match your actual asset filenames.
     */
    private void tryLoadSprites() {
        try {
            var duckStream = getClass().getResourceAsStream("/images/player/duck_run_1.png");
            if (duckStream != null) {
                duckSprite = new Image(duckStream, DUCK_SIZE, DUCK_SIZE, true, true);
            }
        } catch (Exception ignored) { /* use fallback */ }

        try {
            var flagStream = getClass().getResourceAsStream("/images/ui/flag.png");
            if (flagStream != null) {
                flagSprite = new Image(flagStream, (int)FLAG_CLOTH_W + 6, (int)FLAG_CLOTH_H + 8, true, true);
            }
        } catch (Exception ignored) { /* use fallback */ }
    }

    /**
     * Convenience: inject pre-loaded images from your AssetLoader.
     * Call this right after construction if you manage assets centrally.
     *
     * Example:
     *   progressBar.setSprites(
     *       assetLoader.getImage("duck_run_1"),
     *       assetLoader.getImage("flag")
     *   );
     */
    public void setSprites(Image duckSprite, Image flagSprite) {
        this.duckSprite = duckSprite;
        this.flagSprite = flagSprite;
    }
}