package edu.bauet.java.cse.duckrun.scenes;

import edu.bauet.java.cse.duckrun.MainApp;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuScene {

    private Scene scene;

    public MenuScene() {

        StackPane root = new StackPane();

        // 🔹 Background Image
        Image bgImage = new Image(
                getClass().getResource("/images/ui/hall_background.png").toExternalForm()
        );
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(MainApp.WINDOW_WIDTH);
        bgView.setFitHeight(MainApp.WINDOW_HEIGHT);

        // 🔹 Title
        Text title = new Text("DuckRun 🦆");
        title.setFont(Font.font("Arial", 60));
        title.setStyle("-fx-fill: white; -fx-font-weight: bold;");

        // 🔹 Start Button
        Button startBtn = new Button("Start Game");
        startBtn.setPrefWidth(200);
        startBtn.setPrefHeight(50);

        startBtn.setOnAction(e -> {
            System.out.println("Start Game Clicked!");
            // Later we switch to GameScene
        });

        VBox menuBox = new VBox(30, title, startBtn);
        menuBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(bgView, menuBox);

        scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}
