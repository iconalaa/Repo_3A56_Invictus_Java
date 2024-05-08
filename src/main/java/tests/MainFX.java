package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;


public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader load = new FXMLLoader(getClass().getResource("/fxml/dons/Gratifications.fxml"));
        Parent root = load.load();
        Scene scene = new Scene(root, Color.WHITE) ;
        Stage stage = new Stage() ;
        Image logo =new Image("/img/logo/favicon.png");

        stage.setTitle("Radiohub-Donate");
        stage.getIcons().add(logo);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}