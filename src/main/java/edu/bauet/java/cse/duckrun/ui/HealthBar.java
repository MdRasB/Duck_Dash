package edu.bauet.java.cse.duckrun.ui;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HealthBar {

    private final HBox container = new HBox(5);

    private final Image fullHeart;
    private final Image emptyHeart;

    private final int maxHealth;
    private int currentHealth;

    private final ImageView[] hearts;

    public HealthBar(int maxHealth) {

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;

        fullHeart = AssetLoader.getImage("/images/indicator/heart_full.png");
        emptyHeart = AssetLoader.getImage("/images/indicator/heart_empty.png");

        hearts = new ImageView[maxHealth];

        for (int i = 0; i < maxHealth; i++) {

            hearts[i] = new ImageView(fullHeart);
            hearts[i].setFitHeight(35);
            hearts[i].setPreserveRatio(true);

            container.getChildren().add(hearts[i]);
        }
    }

    public void decreaseHealth() {

        if (currentHealth <= 0) return;

        currentHealth--;
        updateHearts();
    }

    public void increaseHealth() {

        if (currentHealth >= maxHealth) return;

        currentHealth++;
        updateHearts();
    }

    public boolean isFull() {
        return currentHealth >= maxHealth;
    }

    private void updateHearts() {

        for (int i = 0; i < maxHealth; i++) {

            if (i < currentHealth)
                hearts[i].setImage(fullHeart);
            else
                hearts[i].setImage(emptyHeart);
        }
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public HBox getView() {
        return container;
    }

    public void reset() {

        currentHealth = maxHealth;
        updateHearts();
    }
}