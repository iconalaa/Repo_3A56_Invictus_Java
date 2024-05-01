package controllers.user;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class HomeController {
    private Stage stage;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profileImg;

    private User user = new User();



    public void setHome(User u){
        this.user=u;
        nameLabel.setText(user.getName()+" "+user.getLastName());
    }

    @FXML
    void profileAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/profile.fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();
        controller.initialise(user);
        stage = new Stage();
        stage.setTitle("Profile | RadioHub");
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }


}
