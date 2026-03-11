package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.scenes.GameScene;
import javafx.scene.Scene;

public class Level1 {

    // Background image path for Level 1
    private static final String BACKGROUND_PATH = "/images/backgrounds/level1.png";

    private GameScene gameScene;

    public Level1() {

        gameScene = new GameScene(
                BACKGROUND_PATH,
                true,   // spawnCats
                false,  // spawnEagles
                true,   // spawnBread (Set to false to disable bread)
                4     // worldSpeed
        );
    }

    /**
     * Returns the scene for this level
     */
    public Scene createLevel() {
        return gameScene.getScene();
    }
}
