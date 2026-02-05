package edu.bauet.java.cse.duckrun;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label(" !! JavaFX is working! The game is starting. This is the test repository");
        Scene scene = new Scene(label, 680, 840);

        stage.setTitle("DuckRun");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
