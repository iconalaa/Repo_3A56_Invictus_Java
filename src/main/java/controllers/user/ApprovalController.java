package controllers.user;

import entities.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.user.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ApprovalController {
    @FXML
    private VBox notifVbox;


    private final UserService ps = new UserService();

    public void initialize() {
        try {
            List<User> allUsers = ps.getToApproveUsers();
            displayNotifications(allUsers);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void displayNotifications(List<User> users) throws SQLException {
        notifVbox.getChildren().clear();
        notifVbox.setSpacing(10);
        int maxColumns = 1;
        for (int i = 0; i < users.size(); i += maxColumns) {
            HBox rowHBox = new HBox();
            for (int j = i; j < Math.min(i + maxColumns, users.size()); j++) {
                User user = users.get(j);
                AnchorPane userCard = createNotification(user);
                rowHBox.getChildren().add(userCard);
            }
            notifVbox.getChildren().add(rowHBox);
        }
    }

    private AnchorPane createNotification(User user) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/approvalCard.fxml"));
            AnchorPane stackPane = loader.load();
            Label Email = (Label) stackPane.lookup("#Email");
            Email.setText(user.getEmail() + " | " + ps.matriculeUser(user.getEmail()));
            ImageView approve = (ImageView) stackPane.lookup("#approve");
            ImageView decline = (ImageView) stackPane.lookup("#decline");
            ImageView pdf = (ImageView) stackPane.lookup("#pdfBtn");
            decline.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Are you sure you want to Decline this Request?");
                    alert.setContentText("This action cannot be undone.");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            user.setRole(new String[]{"ROLE_USER"});
                            try {
                                ps.update(user, user.getUser_id());
                                displayNotifications(ps.getToApproveUsers());
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    });
                }
            });
            approve.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Are you sure you want to Decline this Request?");
                    alert.setContentText("This action cannot be undone.");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            String role = user.getRole()[0];
                            if (role.contains("ROLE_WAITING_DOCTOR")){
                                user.setRole(new String[]{"ROLE_DOCTOR"});
                            } else {
                                user.setRole(new String[]{"ROLE_RADIOLOGIST"});
                            }
                            try {
                                ps.update(user, user.getUser_id());
                                displayNotifications(ps.getToApproveUsers());
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    });
                }
            });

            return stackPane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
