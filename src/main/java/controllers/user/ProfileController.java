package controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class ProfileController {
    private Stage stage;
    @FXML
    private Label profileEmail;

    @FXML
    private ImageView profileImg;

    @FXML
    private Label profileLastName;

    @FXML
    private Label profileName;


    @FXML
    void settingsAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/settings.fxml"));
        Parent root = loader.load();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    @FXML
    void logoutAction(ActionEvent event) {
        System.out.println("Hello");
    }
}
