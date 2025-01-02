package org.example.sqlformat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class  SqlFormat extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox root = FXMLLoader.load(getClass().getResource("/org/example/sqlformat/view/main.fxml"));
        Scene scene = new Scene(root, 800, 400);
        primaryStage.setTitle("SQL Formatter");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {

        launch(args);

    }
}