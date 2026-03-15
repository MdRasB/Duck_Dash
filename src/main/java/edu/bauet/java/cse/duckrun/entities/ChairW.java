package edu.bauet.java.cse.duckrun.entities;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;

public class ChairW extends Obstacle {

    // === HITBOX DESIGN FOR CHAIR (crouch-or-jump obstacle) ===
    //
    // Scene coordinate reference (groundY = 590):
    //   Chair root.layoutY  = 590 - 65      = 525
    //   View local Y range  = -25 to +75    (100px tall, layoutY=-25)
    //   View in scene       = 500 to 600
    //
    // Duck hitboxes in scene coords:
    //   Standing  top = 527,  bottom = 590
    //   Crouching top = 537,  bottom = 600  (duckView shifts down 20px)
    //   Jumping   top = 295,  bottom = 340  (at peak, jumpHeight=250)
    //
    // Goal: hitbox covers only the back+seat (upper part of chair image),
    //       NOT the legs — so a crouching duck slides cleanly underneath.
    //
    // shrinkYBottom = 0.68  →  hitbox bottom (local) = -25 + 100 - 68 =  7
    //                          hitbox bottom (scene) = 525 + 7         = 532
    //   Crouching duck top (scene) = 537  >  532  → NO collision ✓
    //   Standing  duck top (scene) = 527  <  532  → collision fires  ✓
    //
    // shrinkYTop = 0.05  →  hitbox top (local) = -25 + 5  = -20
    //                        hitbox top (scene) = 525 - 20 = 505
    //   Jumping duck bottom at layoutY=415: 415+45+45=505 → just clearing
    //   Duck reaches layoutY=415 early in the jump arc, so jump works ✓
    //
    // shrinkX = 0.15  →  tighter sides to match the chair's narrow silhouette

    public ChairW(double startX, double startY, double worldSpeed) {
        super(startX, startY - 65, worldSpeed, 0.0, 70);

        view.setImage(AssetLoader.getImage("/images/obstacles/Chair_wood.png"));
        view.setFitHeight(125);
        view.setPreserveRatio(true);
        view.setLayoutY(-60);
    }

    @Override
    protected double getHitboxShrinkX() {
        return 0.15; // Trim sides to match chair width
    }

    @Override
    protected double getHitboxShrinkYTop() {
        return 0.05; // Keep top of hitbox near the chair back
    }

    @Override
    protected double getHitboxShrinkYBottom() {
        // Cuts away the leg region — crouching duck passes under the seat
        return 0.5;
    }
}