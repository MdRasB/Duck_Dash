package edu.bauet.java.cse.duckrun;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage){
        Label label=new Label("Duck Run Started!");
        Scene scene=new Scene(label,400,300);
        stage.setTitle("Duck Run");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
