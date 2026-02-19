package edu.bauet.java.cse.duckrun.levels;

import edu.bauet.java.cse.duckrun.scenes.GameScene;
import javafx.scene.Scene;

public class Level1 {

    private static final String BACKGROUND =
            "/images/backgrounds/level1.png";

    private GameScene gameScene;

    public Level1() {
        gameScene = new GameScene(BACKGROUND);
    }

    public Scene createLevel() {
        return gameScene.getScene();
    }
}
