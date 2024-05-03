package controllers.Image;

import entities.Image;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.Image.DroitServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Share {
    private Image selectedImage;
    private final DroitServices droitServices = new DroitServices();

    @FXML
    private VBox checkBoxContainer;

    public void setSelectedImage(Image selectedImage) throws SQLException {
        this.selectedImage = selectedImage;
        ArrayList<User> radiologists = droitServices.getRadwithoutGuest(selectedImage.getId());

        // Clear existing checkboxes
        checkBoxContainer.getChildren().clear();

        for (User radiologist : radiologists) {
            CheckBox checkBox = new CheckBox(radiologist.getName() + " ");
            checkBox.setId("radiologist_" + radiologist.getUser_id());
            checkBoxContainer.getChildren().add(checkBox);
        }
    }

    @FXML
    void sharewith(ActionEvent event) throws SQLException {
        System.out.println("Selected radiologists:");
        boolean atLeastOneSelected = false;

        List<Integer> selectedRadiologistIds = new ArrayList<>();

        for (Node node : checkBoxContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.getStyleClass().add("checkbox");
                if (checkBox.isSelected()) {
                    atLeastOneSelected=true;
                    String radiologistIdString = checkBox.getId().replace("radiologist_", "");
                    int radiologistId = Integer.parseInt(radiologistIdString);
                    selectedRadiologistIds.add(radiologistId); // Add selected ID to the list
                }
            }
        }
        if (!atLeastOneSelected) {
            System.out.println("Please select at least one radiologist.");


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select radioligist.");
            alert.showAndWait();
            return;

        }
        droitServices.addRoleToImage(selectedImage.getId(), selectedRadiologistIds);
        refreshCheckBoxes();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void refreshCheckBoxes() throws SQLException {
        checkBoxContainer.getChildren().clear();

        ArrayList<User> updatedRadiologists = droitServices.getRadwithoutGuest(selectedImage.getId());

        for (User radiologist : updatedRadiologists) {
            CheckBox checkBox = new CheckBox(radiologist.getName() + " ");
            checkBox.setId("radiologist_" + radiologist.getUser_id()); // Set the ID for identification
            checkBoxContainer.getChildren().add(checkBox);
        }
    }
}
