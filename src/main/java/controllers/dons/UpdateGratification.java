package controllers.dons;

import entities.Gratification;
import services.dons.ServiceGratification;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateGratification implements Initializable {

    @FXML
    private TextField updtitle;

    @FXML
    private TextField updtype;

    @FXML
    private TextField upddesc;

    @FXML
    private TextField updmont;

    @FXML
    private Label ermont;

    @FXML
    private Label ertitre;

    @FXML
    private Label erdesc;

    @FXML
    private Label ertype;

    private Gratification selectedGrtification;


    public void setSelectedDonor(Gratification grat) {
        selectedGrtification = grat;
        if (selectedGrtification != null) {
            // Populate the fields with donor's information
            updtitle.setText(selectedGrtification.getTitre_grat());
            updtype.setText(selectedGrtification.getType_grat());
            upddesc.setText(selectedGrtification.getDesc_grat());
            updmont.setText(String.valueOf(selectedGrtification.getMontant()));
        }
    }

    @FXML
    private void updategrat() {
        if (selectedGrtification != null &&validateFields())  {
            // Get the modified values from the text fields
            String modifiedTitle = updtitle.getText();
            String modifiedType = updtype.getText();
            String modifiedDesc = upddesc.getText();
            int modifiedmont = Integer.parseInt(updmont.getText());

            // Update the selected donor's information
            selectedGrtification.setTitre_grat(modifiedTitle);
            selectedGrtification.setType_grat(modifiedType);
            selectedGrtification.setDesc_grat(modifiedDesc);
            selectedGrtification.setMontant(modifiedmont);

            try {
                // Update the donor in the database
                ServiceGratification service = new ServiceGratification();
                service.modifier(selectedGrtification);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Gratification modified successfully");

                // Close the update window
                Stage stage = (Stage) updtitle.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                // Show error message if database update fails
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to modify Gratification: " + e.getMessage());
            }
        } else {
            // Show warning if no donor is selected
            showAlert(Alert.AlertType.WARNING, "Error", "One of the values you entered does not match the correct form");
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
        updtitle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedGrtification != null) {
                selectedGrtification.setTitre_grat(newValue);
            }
        });

        updtype.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedGrtification != null) {
                selectedGrtification.setType_grat(newValue);
            }
        });

        upddesc.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedGrtification != null) {
                selectedGrtification.setDesc_grat(newValue);
            }
        });

        updmont.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedGrtification != null) {
                try {
                    int newTelephone = Integer.parseInt(newValue);
                    selectedGrtification.setMontant(newTelephone);
                } catch (NumberFormatException e) {
                    // Handle invalid input
                }
            }
        });
    }

    public boolean validateFields() {
        String textPattern = "^[A-Za-z ]+$";
        String numberPattern = "\\d+";
        boolean test = true;
        if (!updtitle.getText().matches(textPattern)) {
            ertitre.setText("Invalid title !");
            test = false;
        }

        if (!upddesc.getText().matches(textPattern)) {
            erdesc.setText("Description must be text !");
            test = false;
        }

        if (!updmont.getText().matches(numberPattern)) {
            ermont.setText("The amount must be numeric !");
            test = false;
        }

        if (!updtype.getText().matches(textPattern)) {
            ertype.setText("Invalid Type !");
            test = false;
        }



        return test;
    }
}
