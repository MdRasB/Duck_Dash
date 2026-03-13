package edu.bauet.java.cse.duckrun.ui;

import edu.bauet.java.cse.duckrun.utils.AssetLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SleepBar {

    private final HBox container = new HBox(5);

    private final Image fullBar;
    private final Image emptyBar;

    private final int maxSegments = 3;
    private int currentSegments = 0;

    private final ImageView[] segments;

    public SleepBar() {

        fullBar = AssetLoader.getImage("/images/indicator/sleep_bar_full.png");
        emptyBar = AssetLoader.getImage("/images/indicator/sleep_bar_empty.png");

        segments = new ImageView[maxSegments];

        for (int i = 0; i < maxSegments; i++) {

            segments[i] = new ImageView(emptyBar);
            segments[i].setFitHeight(35);
            segments[i].setPreserveRatio(true);

            container.getChildren().add(segments[i]);
        }
    }

    public void addSegment() {

        if (currentSegments >= maxSegments) return;

        currentSegments++;
        updateSegments();
    }

    private void updateSegments() {

        for (int i = 0; i < maxSegments; i++) {

            if (i < currentSegments)
                segments[i].setImage(fullBar);
            else
                segments[i].setImage(emptyBar);
        }
    }

    public boolean isFull() {
        return currentSegments >= maxSegments;
    }

    public HBox getView() {
        return container;
    }

    public void reset() {

        currentSegments = 0;
        updateSegments();
    }

    public void decreaseSegment() {
        if (currentSegments > 0) {
            currentSegments--;
            updateSegments();
        }
    }
}