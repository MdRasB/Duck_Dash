package edu.bauet.java.cse.duckrun.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

public class TimeUtil {

    private Timeline timeline;
    private int currentTimeSeconds = 0;
    private final StringProperty timeProperty = new SimpleStringProperty();

    public TimeUtil() {
        updateTimeProperty();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            currentTimeSeconds++;
            updateTimeProperty();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateTimeProperty() {
        int minutes = currentTimeSeconds / 60;
        int seconds = currentTimeSeconds % 60;
        timeProperty.set(String.format("%02d:%02d", minutes, seconds));
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void increaseTime(int seconds) {
        currentTimeSeconds += seconds;
        updateTimeProperty();
    }

    public StringProperty timeProperty() {
        return timeProperty;
    }
    
    public void reset() {
        currentTimeSeconds = 0;
        updateTimeProperty();
    }
}
