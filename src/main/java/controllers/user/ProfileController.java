package controllers.user;

import com.sun.javafx.stage.StageHelper;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

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


    void initialise(User u) {
        profileName.setText(u.getName());
        profileLastName.setText(u.getLastName());
        profileEmail.setText(u.getEmail());
//        profileImg
    }

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
            for (Window window : Window.getWindows()) {
                if (window instanceof Stage) {
                    Stage stage = (Stage) window;
                    stage.close();
                }
            }
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            LoginController log= new LoginController();
            log.showScene(event,"user/login.fxml","Login");
    }

}
