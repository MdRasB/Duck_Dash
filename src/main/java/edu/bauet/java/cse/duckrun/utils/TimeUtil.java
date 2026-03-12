package edu.bauet.java.cse.duckrun.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

public class TimeUtil {

    private Timeline timeline;
    private int initialTime;
    private int timeSeconds;
    private final StringProperty timeProperty = new SimpleStringProperty();
    private Runnable onTimeEnd;

    public TimeUtil(int initialTime, Runnable onTimeEnd) {
        this.initialTime = initialTime;
        this.timeSeconds = initialTime;
        this.onTimeEnd = onTimeEnd;
        updateTimeProperty();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;
            updateTimeProperty();
            if (timeSeconds <= 0) {
                stop();
                if (onTimeEnd != null) {
                    onTimeEnd.run();
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateTimeProperty() {
        int minutes = timeSeconds / 60;
        int seconds = timeSeconds % 60;
        timeProperty.set(String.format("%02d:%02d", minutes, seconds));
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void decreaseTime(int seconds) {
        timeSeconds = Math.max(0, timeSeconds - seconds);
        updateTimeProperty();
    }

    public StringProperty timeProperty() {
        return timeProperty;
    }
    
    public void reset() {
        timeSeconds = initialTime;
        updateTimeProperty();
    }
}
