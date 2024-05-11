package controllers.user;

import entities.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HomeDash {

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profileImg;

    private Stage stage;
    User user = SessionManager.getLoggedInUser();


    public void initialize(){
        nameLabel.setText(user.getName() + " " + user.getLastName());
        profileImg.setImage(new Image(new File("C:/Users/Ala/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + user.getBrochure_filename()).toURI().toString()));
        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);
        profileImg.setPreserveRatio(false);
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
        stage.setResizable(false);
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    void GoReports(MouseEvent event) {
        showScene(event,"diagnostic/admin/reports-admin.fxml","Dashboard");

    }

    @FXML
    void fxDonor(MouseEvent event) {
        showScene(event, "dons/Donateurs.fxml", "Donor");
    }
    @FXML
    void fxBlog(MouseEvent event) {
        showScene(event, "blog/ListArticle.fxml", "Blog");
    }

    @FXML
    void fxUser(MouseEvent event) {
        showScene(event,"dashboard.fxml","User");
    }
    public void showScene(MouseEvent event, String x, String title) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + x));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
            stage.setTitle(title + " | Dashboard");
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
