package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class Dog extends Enemy {

    private static final String SLEEPING_IMAGE = "/images/enemies/dog_sleeping.png";
    private static final String HIT_IMAGE      = "/images/enemies/dog_hit.png";

    public Dog(double startX, double startY, double worldSpeed) {
        super(startX, startY - 65, worldSpeed, 0.0, 70);

        view.setImage(AssetLoader.getImage(SLEEPING_IMAGE));
        view.setFitHeight(120);
        view.setPreserveRatio(true);
        view.setLayoutY(-55);
    }

    /**
     * Dog doesn't animate between two frames — it stays on whichever image
     * is currently set (sleeping or hit). Overriding prevents the base-class
     * animate() from wiping the image with null state1/state2 values.
     */
    @Override
    protected void animate() {
        // intentionally no-op: image is managed manually via showHitImage()
    }

    /**
     * Called by GameScene/EndlessGameScene when the duck collides with this dog.
     * Swaps the sprite to the "hit" image so the dog looks disturbed.
     */
    public void showHitImage() {
        view.setImage(AssetLoader.getImage(HIT_IMAGE));
    }

    @Override
    protected double getHitboxShrinkX() {
        return 0.1;
    }

    @Override
    protected double getHitboxShrinkYTop() {
        return 0.08;
    }

    @Override
    protected double getHitboxShrinkYBottom() {
        return 0.05;
    }
}