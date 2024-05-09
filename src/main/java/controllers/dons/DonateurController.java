package controllers.dons;

import controllers.user.ProfileController;
import controllers.user.SessionManager;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.List;
import java.util.stream.Collectors;
import entities.Donateur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import services.dons.ServiceDonateur;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class DonateurController implements Initializable {
    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profileImg;
    private Stage stage;

    @FXML
    private TableView<Donateur> tableDonors;

    @FXML
    private TableColumn<Donateur, Integer> idColumn;

    @FXML
    private TableColumn<Donateur, String> nomColumn;

    @FXML
    private TableColumn<Donateur, String> prenomColumn;

    @FXML
    private TableColumn<Donateur, String> emailColumn;

    @FXML
    private TableColumn<Donateur, Integer> telephoneColumn;

    @FXML
    private TableColumn<Donateur, String> typeColumn;


    @FXML
    private TextField searchField;

    @FXML
    private MenuButton menu;

    @FXML
    private javafx.scene.control.MenuItem pgdonations;

    @FXML
    private MenuItem pggratifications;

    User user = SessionManager.getLoggedInUser();
    @FXML
    void fxBlog(MouseEvent event) {
        return;
    }

    @FXML
    void fxDashboard(MouseEvent event) {
        showScene(event,"dashboardHome.fxml","Dashboard");
    }

    @FXML
    void fxDonor(MouseEvent event) {
        return;
    }

    @FXML
    void fxUser(MouseEvent event) {
        showScene(event,"dashboard.fxml","Dashboard");
    }  @FXML
    void GoReports(MouseEvent event) {
        showScene(event,"diagnostic/admin/reports-admin.fxml","Dashboard");
    }

    @FXML
    private ObservableList<Donateur> donateursList = FXCollections.observableArrayList();

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
            List<Donateur> donateurs = service.afficher();
            donateursList.clear(); // Clear the existing data
            donateursList.addAll(donateurs); // Add new data
            tableDonors.setItems(donateursList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void deleteDonor(ActionEvent event) {
        Donateur selectedDonateur = tableDonors.getSelectionModel().getSelectedItem();
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
        Donateur selectedDonateur = tableDonors.getSelectionModel().getSelectedItem();
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
        nameLabel.setText(user.getName() + " " + user.getLastName());
        profileImg.setImage(new Image(new File("C:/Users/Mega-Pc/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + user.getBrochure_filename()).toURI().toString()));
        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);
        profileImg.setPreserveRatio(false);
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
            ObservableList<Donateur> filteredList = FXCollections.observableArrayList(
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

    public void showScene(MouseEvent event, String path, String title) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
            stage.setTitle(title+" | RadioHub");
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
