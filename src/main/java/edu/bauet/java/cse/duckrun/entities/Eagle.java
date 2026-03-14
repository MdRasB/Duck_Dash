package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class Eagle extends Enemy {

    /*
     * HEIGHT DESIGN — why these numbers:
     *
     * Duck standing:  sprite top = groundLine - 90,  hitbox top ≈ groundLine - 63
     * Duck crouching: sprite top = groundLine - 70,  hitbox top ≈ groundLine - 49
     * Duck jumping:   hitbox top rises above         groundLine - 200 at peak
     *
     * Eagle sprite is 80 px tall.
     * We place its TOP at  groundLine - 155  →  bottom at groundLine - 75.
     * After hitbox shrink (20 % top, 20 % bottom) the collision zone is:
     *   top    ≈ groundLine - 139
     *   bottom ≈ groundLine - 91
     *
     * Result:
     *   Standing duck  — hitbox top ≈ groundLine - 63  → INSIDE eagle zone → HIT ✓
     *   Crouching duck — hitbox top ≈ groundLine - 49  → BELOW eagle zone  → SAFE ✓
     *   Jumping duck   — hitbox rises above groundLine - 200              → SAFE ✓
     *
     * The player must actively choose: crouch under it or jump over it.
     */
    private static final double FLIGHT_HEIGHT = 125; // px above groundLine for sprite top

    public Eagle(double startX, double groundLine, double worldSpeed) {

        super(startX, groundLine - FLIGHT_HEIGHT, worldSpeed, 3.2, 80);

        state1 = AssetLoader.getImage("/images/enemies/Eagle_state_1.png");
        state2 = AssetLoader.getImage("/images/enemies/Eagle_state_2.png");

        view.setImage(state1);
        view.setFitHeight(150);
        view.setPreserveRatio(true);
        view.setLayoutY(-60);
    }

    @Override
    protected double getHitboxShrinkX() {
        return 0.25;
    }

    @Override
    protected double getHitboxShrinkYTop() {
        return 0.2;
    }

    @Override
    protected double getHitboxShrinkYBottom() {
        return 0.2;
    }

    @Override
    protected int getAnimationSpeed() {
        return 10;
    }
}