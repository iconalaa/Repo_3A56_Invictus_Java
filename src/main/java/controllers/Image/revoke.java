package controllers.Image;

import entities.Droit;
import entities.Image;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import services.Image.DroitServices;

import java.sql.SQLException;
import java.util.ArrayList;

public class revoke {

    private Image selectedImage;
    private final DroitServices droitServices = new DroitServices();

    @FXML
    private ListView<Droit> listCol;

    public void setSelectedImage(Image selectedImage) throws SQLException {
        this.selectedImage = selectedImage;
        ArrayList<Droit> droits = droitServices.getDroitWithImage(selectedImage.getId());

        listCol.getItems().clear();

        listCol.getItems().setAll(droits);

        listCol.setCellFactory(param -> new ListCell<Droit>() {
            @Override
            protected void updateItem(Droit droit, boolean empty) {
                super.updateItem(droit, empty);
                if (empty || droit == null) {
                    setText(null);
                } else {
                    setText("Radiologist: " + droit);
                }
            }
        });
    }

    @FXML
    void deleteSelectedDroit(ActionEvent event) throws SQLException {
        System.out.println("entred inside the method ");
        Droit selectedDroit = listCol.getSelectionModel().getSelectedItem();

        if (selectedDroit != null) {
            droitServices.deleteShraedRoleById(selectedDroit.getId());

            listCol.getItems().remove(selectedDroit);
        }
        else{

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;

        }
    }
}
