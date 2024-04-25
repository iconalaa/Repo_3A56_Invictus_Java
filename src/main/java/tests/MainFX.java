package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;


public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
        primaryStage.setTitle("RadioHub");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}