package controllers.dons;

import Entities.donateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entities.gratification;
import Services.ServiceGratification;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GratificationController implements Initializable {

    @FXML
    private TableView<gratification> tableGrats;



    @FXML
    private TableColumn<gratification, Integer> idColumn;

    @FXML
    private TableColumn<gratification, Integer> donorIDColumn;

    @FXML
    private TableColumn<gratification, Date> dateColumn;

    @FXML
    private TableColumn<gratification, String> titreColumn;

    @FXML
    private TableColumn<gratification, String> typeColumn;

    @FXML
    private TableColumn<gratification, String> descColumn;

    @FXML
    private TableColumn<gratification, Integer> montantColumn;

    @FXML
    private TextField titre ;

    @FXML
    private Label titreErreur ;

    @FXML
    private TextField desc ;

    @FXML
    private Label descErreur ;

    @FXML
    private TextField montant ;

    @FXML
    private Label montantErreur ;

    @FXML
    private TextField type ;

    @FXML
    private Label typeErreur ;

    @FXML
    private TextField searchField;

    @FXML
    private TextField updtitle;

    @FXML
    private TextField updtype;

    @FXML
    private TextField upddesc;

    @FXML
    private TextField updmont;

    @FXML
    private MenuButton menu;

    @FXML
    private MenuItem pgdonations;

    @FXML
    private MenuItem pggratifications;

    @FXML
    private ObservableList<gratification> gratssList = FXCollections.observableArrayList();

    /*
    public void addGrat(ActionEvent event) throws IOException {

        if (validateFields()) {
            int  mont = Integer.parseInt(montant.getText());
            gratification g = new gratification(mont, titre.getText(), desc.getText(), type.getText());
            ServiceGratification service = new ServiceGratification();
            try {
                service.ajouter(g);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations");
                alert.setHeaderText("Your donation has been added successfully'");
                alert.show();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid Inputs");
        }
    }

     */

    private void loadGratificationData() {
        try {
            ServiceGratification service  = new ServiceGratification();
            List<gratification> gratss = service.afficher();
            gratssList.clear();
            gratssList.addAll(gratss);
            tableGrats.setItems(gratssList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void modifyGratification(ActionEvent event) {
        gratification selectedGratification = tableGrats.getSelectionModel().getSelectedItem();
        if (selectedGratification != null) {
            // Fetch modified values from text fields
            String modifiedTitle = updtitle.getText();
            String modifiedType = updtype.getText();
            String modifiedDesc = upddesc.getText();
            int modifiedMontant = Integer.parseInt(updmont.getText());

            selectedGratification.setTitre_grat(modifiedTitle);
            selectedGratification.setType_grat(modifiedType);
            selectedGratification.setDesc_grat(modifiedDesc);
            selectedGratification.setMontant(modifiedMontant);

            try {
                ServiceGratification service = new ServiceGratification();
                service.modifier(selectedGratification);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Gratification modified successfully");
                // Refresh the table view to reflect the changes
                loadGratificationData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to modify gratification: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a gratification to modify");
        }
    }


    @FXML
    private void deleteGrat(ActionEvent event) {
        gratification selectedGrat = tableGrats.getSelectionModel().getSelectedItem();
        if (selectedGrat != null) {
            try {
                ServiceGratification service = new ServiceGratification();
                service.supprimer(selectedGrat.getId());
                gratssList.remove(selectedGrat);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Grant deleted successfully");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete Grant: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a Grat to delete");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /*
    public boolean validateFields() {
        String textPattern = "^[A-Za-z ]+$";
        String numberPattern = "\\d+";
        boolean test = true;
        if (!titre.getText().matches(textPattern)) {
            titreErreur.setText("Invalid title !");
            test = false;
        }

        if (!desc.getText().matches(textPattern)) {
            descErreur.setText("Description must be text !");
            test = false;
        }

        if (!montant.getText().matches(numberPattern)) {
            montantErreur.setText("The amount must be numeric !");
            test = false;
        }

        if (!type.getText().matches(textPattern)) {
            typeErreur.setText("Invalid Type !");
            test = false;
        }



        return test;
    }

     */



    private void initializeTable() {
        //idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //donorIDColumn.setCellValueFactory(new PropertyValueFactory<>("id_donateur_id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_grat"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre_grat"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_grat"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("desc_grat"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        loadGratificationData();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchGratification();
        });
        tableGrats.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                updtitle.setText(newValue.getTitre_grat());
                updtype.setText(newValue.getType_grat());
                upddesc.setText(newValue.getDesc_grat());
                updmont.setText(String.valueOf(newValue.getMontant()));
            }
        });
        pgdonations.setOnAction(this::navigateToDonations);
        pggratifications.setOnAction(this::navigateToGratifications);
    }

    @FXML
    private void searchGratification() {
        String query = searchField.getText().toLowerCase();
        if (query.isEmpty()) {
            tableGrats.setItems(gratssList);
        } else {
            ObservableList<gratification> filteredList = FXCollections.observableArrayList(
                    gratssList.stream()
                            .filter(g -> g.getTitre_grat().toLowerCase().contains(query) ||
                                    g.getType_grat().toLowerCase().contains(query) ||
                                    g.getDesc_grat().toLowerCase().contains(query) ||
                                    g.getDate_grat().toString().contains(query) ||
                                    String.valueOf(g.getMontant()).contains(query))
                            .collect(Collectors.toList())
            );
            tableGrats.setItems(filteredList);
        }
    }

    private void navigateToDonations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Donateurs.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menu.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToGratifications(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Gratifications.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menu.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
