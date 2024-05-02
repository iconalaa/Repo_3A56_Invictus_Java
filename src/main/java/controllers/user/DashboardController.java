package controllers.user;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
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
            int maxColumns = 1;
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



    public static String convertRole(String roleString) {
        // Remove square brackets and leading/trailing whitespace
        String[] roles = roleString.substring(1, roleString.length()-1).split(",");

        // Process each role
        StringBuilder convertedRoles = new StringBuilder();
        for (String role : roles) {
            String trimmedRole = role.trim().substring(6).toLowerCase();
            convertedRoles.append("| ").append(trimmedRole);
        }
        if (convertedRoles.length() > 0) {
            convertedRoles.setLength(convertedRoles.length() - 1);
        }

        return convertedRoles.toString();
    }

        private StackPane createUserCard(User user) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/card.fxml"));
            StackPane stackPane = loader.load();

            ImageView imgCard = (ImageView) stackPane.lookup("#imgCard");
            Label nameCard = (Label) stackPane.lookup("#nameCard");
            Label emailCard = (Label) stackPane.lookup("#emailCard");
            Label roleCard = (Label) stackPane.lookup("#roleCard");
            Button deleteBtn = (Button) stackPane.lookup("#deleteBtn");
            Button updateBtn = (Button) stackPane.lookup("#updateBtn");
            imgCard.setImage(new Image(new File("C:/Users/Mega-Pc/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/"+user.getBrochure_filename()).toURI().toString()));
            nameCard.setText("Name: "+user.getName() + " " + user.getLastName());
            emailCard.setText("Email: "+user.getEmail());

            String roleString = String.join("",user.getRole());
            String convertedRole = convertRole(roleString);
            roleCard.setText("Role: "+convertedRole);
            deleteBtn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Are you sure you want to delete this?");
                alert.setContentText("This action cannot be undone.");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            ps.delete(user.getUser_id());
                            refreshDisplay();
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                });
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
        refreshDisplay();

    }

}