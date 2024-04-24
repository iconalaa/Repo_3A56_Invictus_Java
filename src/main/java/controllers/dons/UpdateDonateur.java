package controllers.dons;

import Entities.donateur;
import Services.ServiceDonateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateDonateur implements Initializable {

    @FXML
    private TextField updnom;

    @FXML
    private TextField updprenom;

    @FXML
    private TextField updemail;

    @FXML
    private TextField updtel;

    @FXML
    private TextField updtype;


    private donateur selectedDonateur;


    public void setSelectedDonor(donateur donor) {
        selectedDonateur = donor;
        if (selectedDonateur != null) {
            // Populate the fields with donor's information
            updnom.setText(selectedDonateur.getNom_donateur());
            updprenom.setText(selectedDonateur.getPrenom_donateur());
            updemail.setText(selectedDonateur.getEmail());
            updtel.setText(String.valueOf(selectedDonateur.getTelephone()));
            updtype.setText(selectedDonateur.getType_donateur());
        }
    }

    @FXML
    private void updateDonor() {
        if (selectedDonateur != null) {
            // Get the modified values from the text fields
            String modifiedNom = updnom.getText();
            String modifiedPrenom = updprenom.getText();
            String modifiedEmail = updemail.getText();
            int modifiedTel = Integer.parseInt(updtel.getText());
            String modifiedType = updtype.getText();

            // Update the selected donor's information
            selectedDonateur.setNom_donateur(modifiedNom);
            selectedDonateur.setPrenom_donateur(modifiedPrenom);
            selectedDonateur.setEmail(modifiedEmail);
            selectedDonateur.setTelephone(modifiedTel);
            selectedDonateur.setType_donateur(modifiedType);

            try {
                // Update the donor in the database
                ServiceDonateur service = new ServiceDonateur();
                service.modifier(selectedDonateur);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Donateur modified successfully");

                // Close the update window
                Stage stage = (Stage) updnom.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                // Show error message if database update fails
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to modify donateur: " + e.getMessage());
            }
        } else {
            // Show warning if no donor is selected
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a donateur to modify");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updnom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedDonateur != null) {
                selectedDonateur.setNom_donateur(newValue);
            }
        });

        updprenom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedDonateur != null) {
                selectedDonateur.setPrenom_donateur(newValue);
            }
        });

        updemail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedDonateur != null) {
                selectedDonateur.setEmail(newValue);
            }
        });

        updtel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedDonateur != null) {
                try {
                    int newTelephone = Integer.parseInt(newValue);
                    selectedDonateur.setTelephone(newTelephone);
                } catch (NumberFormatException e) {
                    // Handle invalid input
                }
            }
        });

        updtype.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedDonateur != null) {
                selectedDonateur.setType_donateur(newValue);
            }
        });

    }
}
