package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

/**
 * The Boy enemy — exclusive to Level 3.
 *
 * Design rules:
 *  - Stands on the ground and scrolls with the background.
 *  - Animated between boy_state_1 (standing) and boy_state_2 (charging).
 *  - Tall enough that the duck CANNOT jump over him; the player must CROUCH.
 *  - Marked as LETHAL: one hit drains all of the duck's health instantly.
 */
public class Boy extends Enemy {

    /** Height in pixels — tall so the duck has to crouch, not jump. */
    private static final double DISPLAY_HEIGHT = 160;

    /**
     * Extra speed on top of the world speed (0 = moves purely with the background).
     * Keep at 0 so the Boy feels like part of the environment.
     */
    private static final double EXTRA_SPEED = 0;

    // ---------------------------------------------------------------
    // Construction
    // ---------------------------------------------------------------

    public Boy(double startX, double groundY, double worldSpeed) {
        super(startX,
                groundY - DISPLAY_HEIGHT,   // anchor bottom of sprite to ground
                worldSpeed,
                EXTRA_SPEED,
                DISPLAY_HEIGHT);

        state1 = AssetLoader.getImage("/images/enemies/boy_state_1.png");
        state2 = AssetLoader.getImage("/images/enemies/boy_state_2.png");

        view.setImage(state1);
        view.setFitHeight(200);
        view.setPreserveRatio(true);
        view.setLayoutY(-45);
    }

    // ---------------------------------------------------------------
    // Hitbox tuning  (tighter horizontal, normal vertical)
    // ---------------------------------------------------------------

    @Override
    protected double getHitboxShrinkX()       { return 0.18; }
    @Override
    protected double getHitboxShrinkYTop()    { return 0.0; }
    @Override
    protected double getHitboxShrinkYBottom() { return 0.25; }

    // ---------------------------------------------------------------
    // Animation — slightly slower than the Cat for a menacing feel
    // ---------------------------------------------------------------

    @Override
    protected int getAnimationSpeed() { return 18; }

    // ---------------------------------------------------------------
    // Lethal flag — GameScene checks this to drain full health
    // ---------------------------------------------------------------

    /**
     * Returns {@code true} so that GameScene knows this enemy is lethal
     * (one hit = instant game over, all health removed at once).
     */
    public boolean isLethal() {
        return true;
    }
}