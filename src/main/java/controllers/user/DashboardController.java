package controllers.user;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.user.UserService;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private VBox mainVBox;

    private final UserService ps = new UserService();
    private User userToUpdate;


    public void initialize() {
        try {
            List<User> users = ps.showAll();
            mainVBox.setSpacing(10);
            int maxColumns = 4;
            for (int i = 0; i < users.size(); i += maxColumns) {
                HBox rowHBox = new HBox();
                rowHBox.setSpacing(20);
                for (int j = i; j < Math.min(i + maxColumns, users.size()); j++) {
                    User user = users.get(j);
                    StackPane userCard = createUserCard(user);
                    rowHBox.getChildren().add(userCard);
                }
                mainVBox.getChildren().add(rowHBox);
                mainVBox.setSpacing(20);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    private StackPane createUserCard(User user) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/card.fxml"));
            StackPane stackPane = loader.load();

            ImageView imgCard = (ImageView) stackPane.lookup("#imgCard");
            Label nameCard = (Label) stackPane.lookup("#nameCard");
            Button deleteBtn = (Button) stackPane.lookup("#deleteBtn");
            Button updateBtn = (Button) stackPane.lookup("#updateBtn");

            imgCard.setImage(new Image(new File("C:/Users/Mega-Pc/Pictures/img/low-img/femme-1.png").toURI().toString()));
            nameCard.setText(user.getName() + " " + user.getLastName());
            deleteBtn.setOnAction(e -> {
                try {
                    ps.delete(user.getUser_id());
                    refreshDisplay();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            });
            updateBtn.setOnAction(e -> {
                userToUpdate = user;
                loadUpdateScene();
            });
            return stackPane;
        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
    }

    private void refreshDisplay() {
        mainVBox.getChildren().clear();
        initialize();
    }

    private void loadUpdateScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/admin/update.fxml"));
            Parent root = loader.load();
            updateController controller = loader.getController();
            controller.setModifyUser(userToUpdate);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
            refreshDisplay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void FxAdd(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/admin/add.fxml"));
        Parent root = loader.load();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

}