package controllers.dons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.List;
import java.util.stream.Collectors;
import entities.donateur;
import services.ServiceDonateur;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class DonateurController implements Initializable {

    @FXML
    private TableView<donateur> tableDonors;

    @FXML
    private TableColumn<donateur, Integer> idColumn;

    @FXML
    private TableColumn<donateur, String> nomColumn;

    @FXML
    private TableColumn<donateur, String> prenomColumn;

    @FXML
    private TableColumn<donateur, String> emailColumn;

    @FXML
    private TableColumn<donateur, Integer> telephoneColumn;

    @FXML
    private TableColumn<donateur, String> typeColumn;

    @FXML
    private TextField nom;

    @FXML
    private Label nomErreur;

    @FXML
    private TextField prenom;

    @FXML
    private Label prenomError;

    @FXML
    private TextField email;

    @FXML
    private Label emailError;

    @FXML
    private TextField telephone;

    @FXML
    private Label telephoneError;

    @FXML
    private TextField Type;

    @FXML
    private Label TypeError;

    @FXML
    private TextField searchField;

    @FXML
    private MenuButton menu;

    @FXML
    private javafx.scene.control.MenuItem pgdonations;

    @FXML
    private MenuItem pggratifications;

    @FXML
    private ObservableList<donateur> donateursList = FXCollections.observableArrayList();

    /*public void addDonor(ActionEvent event) throws IOException {

        if (validateFields()) {
            int  tel = Integer.parseInt(telephone.getText());
            donateur d = new donateur(tel, nom.getText(), prenom.getText(), email.getText(), Type.getText());
            ServiceDonateur service = new ServiceDonateur();
            try {
                service.ajouter(d);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations");
                alert.setHeaderText("You have been added as a donor");
                alert.show();

                navigateTonewgrat();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid Inputs");
        }
    }

     */

    private void loadDonateurData() {
        try {
            ServiceDonateur service  = new ServiceDonateur();
            List<donateur> donateurs = service.afficher();
            donateursList.clear(); // Clear the existing data
            donateursList.addAll(donateurs); // Add new data
            tableDonors.setItems(donateursList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void deleteDonor(ActionEvent event) {
        donateur selectedDonateur = tableDonors.getSelectionModel().getSelectedItem();
        if (selectedDonateur != null) {
            try {
                ServiceDonateur service = new ServiceDonateur();
                service.supprimer(selectedDonateur.getId());
                donateursList.remove(selectedDonateur);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Donateur deleted successfully");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete donateur: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a donateur to delete");
        }
    }


    @FXML
    private void modifyDonor(ActionEvent event) throws IOException {
        donateur selectedDonateur = tableDonors.getSelectionModel().getSelectedItem();
        if (selectedDonateur != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dons/updateDonateur.fxml"));
            Parent root = loader.load();

            UpdateDonateur updateDonateurController = loader.getController();
            updateDonateurController.setSelectedDonor(selectedDonateur);

            // Show the update form
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // After updating, reload the donor data
            loadDonateurData();
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a donateur to modify");
        }
    }

    /*
    @FXML
    private void modifyDonor(ActionEvent event) {
        donateur selectedDonateur = tableDonors.getSelectionModel().getSelectedItem();
        if (selectedDonateur != null) {
            // Fetch modified values from text fields
            String modifiedNom = updnom.getText();
            String modifiedPrenom = updprenom.getText();
            String modifiedEmail = updemail.getText();
            int modifiedTel = Integer.parseInt(updtel.getText());
            String modifiedType = updtype.getText();

            // Update selected donor
            selectedDonateur.setNom_donateur(modifiedNom);
            selectedDonateur.setPrenom_donateur(modifiedPrenom);
            selectedDonateur.setEmail(modifiedEmail);
            selectedDonateur.setTelephone(modifiedTel);
            selectedDonateur.setType_donateur(modifiedType);

            try {
                ServiceDonateur service = new ServiceDonateur();
                service.modifier(selectedDonateur);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Donateur modified successfully");
                // Refresh the table view to reflect the changes
                loadDonateurData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to modify donateur: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a donateur to modify");
        }
    } */


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

/*
    public boolean validateFields() {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String namePattern = "^[A-Za-z]{3,}(?:['-][A-Za-z]+)*$";
        String numberPattern = "\\b\\d{8}\\b";
        boolean test = true;
        if (!nom.getText().matches(namePattern)) {
            nomErreur.setText("Invalid Name !");
            test = false;
        }

        if (!prenom.getText().matches(namePattern)) {
            prenomError.setText("Invalid FirstName !");
            test = false;
        }

        if (!email.getText().matches(emailPattern)) {
            emailError.setText("Invalid Email !");
            test = false;
        }

        if (!Type.getText().matches(namePattern)) {
            TypeError.setText("Invalid Type !");
            test = false;
        }

        if (!telephone.getText().matches(numberPattern)) {
            telephoneError.setText("Invalid Phone Number !");
            test = false;
        }


        return test;
    }

 */


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ServiceDonateur service = new ServiceDonateur();
        initializeTable();
        loadDonateurData();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDonateur();
        });
        /*tableDonors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updnom.setText(newValue.getNom_donateur());
                updprenom.setText(newValue.getPrenom_donateur());
                updemail.setText(newValue.getEmail());
                updtel.setText(String.valueOf(newValue.getTelephone()));
                updtype.setText(newValue.getType_donateur());
            }
        });*/
        pgdonations.setOnAction(this::navigateToDonations);
        pggratifications.setOnAction(this::navigateToGratifications);


    }

    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setVisible(false);
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom_donateur"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom_donateur"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_donateur"));
    }

    @FXML
    private void searchDonateur() {
        String query = searchField.getText().toLowerCase();
        if (query.isEmpty()) {
            tableDonors.setItems(donateursList);
        } else {
            ObservableList<donateur> filteredList = FXCollections.observableArrayList(
                    donateursList.stream()
                            .filter(d -> d.getNom_donateur().toLowerCase().contains(query) ||
                                    d.getPrenom_donateur().toLowerCase().contains(query) ||
                                    d.getEmail().toLowerCase().contains(query) ||
                                    d.getType_donateur().toLowerCase().contains(query) ||
                                    String.valueOf(d.getTelephone()).contains(query))
                            .collect(Collectors.toList())
            );
            tableDonors.setItems(filteredList);
        }
    }

    private void navigateToDonations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dons/Donateurs.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menu.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void navigateToGratifications(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dons/Gratifications.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menu.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
