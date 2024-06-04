package controllers.dons;

import controllers.ShowSceen;
import controllers.user.ProfileController;
import controllers.user.SessionManager;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import entities.Gratification;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import services.dons.ServiceGratification;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GratificationController implements Initializable {

    User user = SessionManager.getLoggedInUser();
    Stage stage;
    @FXML
    private TableView<Gratification> tableGrats;

    @FXML
    private TableColumn<Gratification, Integer> idColumn;

    @FXML
    private Button statsbutton;

    @FXML
    private TableColumn<Gratification, Integer> donorIDColumn;

    @FXML
    private TableColumn<Gratification, Date> dateColumn;

    @FXML
    private TableColumn<Gratification, String> titreColumn;

    @FXML
    private TableColumn<Gratification, String> typeColumn;

    @FXML
    private TableColumn<Gratification, String> descColumn;

    @FXML
    private TableColumn<Gratification, Integer> montantColumn;

    @FXML
    private TextField searchField;


    @FXML
    private MenuButton menu;

    @FXML
    private MenuItem pgdonations;

    @FXML
    private MenuItem pggratifications;

    /*@FXML
    private Pagination pagination;
     */

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profileImg;

    private final int itemsPerPage = 5;

    @FXML
    void GoReports(MouseEvent event) {
        ShowSceen s = new ShowSceen();
        s.open(event,"diagnostic/admin/reports-admin.fxml","Dashboard");
    }
    @FXML
    void fxBlog(MouseEvent event) {
    return;
    }

    @FXML
    void fxDashboard(MouseEvent event) {
        ShowSceen s = new ShowSceen();
        s.open(event,"dashboardHome.fxml","Dashboard");
    }

    @FXML
    void fxDonor(MouseEvent event) {
    return;
    }

    @FXML
    void fxUser(MouseEvent event) {
        ShowSceen s = new ShowSceen();
        s.open(event,"dashboard.fxml","Dashboard");
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

    @FXML
    private ObservableList<Gratification> gratssList = FXCollections.observableArrayList();


    private void loadGratificationData() {
        try {
            ServiceGratification service  = new ServiceGratification();
            List<Gratification> gratss = service.afficher();
            gratssList.clear();
            gratssList.addAll(gratss);
            tableGrats.setItems(gratssList);
            //pagination.setPageCount((int) Math.ceil((double) gratssList.size() / itemsPerPage));
            //pagination.setCurrentPageIndex(0); // Reset to first page
            //pagination.setPageFactory(this::createPage); // Update the page factory
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @FXML
    private void modifygrat(ActionEvent event) throws IOException {
        Gratification selectedGratification = tableGrats.getSelectionModel().getSelectedItem();
        if (selectedGratification != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dons/updateGratification.fxml"));
            Parent root = loader.load();

            UpdateGratification updategrat = loader.getController();
            updategrat.setSelectedDonor(selectedGratification);

            // Show the update form
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // After updating, reload the donor data
            loadGratificationData();
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a donateur to modify");
        }
    }


    @FXML
    private void deleteGrat(ActionEvent event) {
        Gratification selectedGrat = tableGrats.getSelectionModel().getSelectedItem();
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




    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setVisible(false);
        //donorIDColumn.setCellValueFactory(new PropertyValueFactory<>("id_donateur_id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_grat"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre_grat"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_grat"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("desc_grat"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText(user.getName() + " " + user.getLastName());
        profileImg.setImage(new Image(new File("C:/Users/Ala/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + user.getBrochure_filename()).toURI().toString()));
        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);
        profileImg.setPreserveRatio(false);
        initializeTable();
        //pagination.setPageCount(1); // Initially set to 1 page
        //pagination.setCurrentPageIndex(0); // Initially set to the first page
        //pagination.setPageFactory(this::createPage);
        loadGratificationData();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchGratification();
        });

        pgdonations.setOnAction(this::navigateToDonations);
        pggratifications.setOnAction(this::navigateToGratifications);
        statsbutton.setOnAction(this::navigateToStats);

    }

    @FXML
    private void searchGratification() {
        String query = searchField.getText().toLowerCase();
        if (query.isEmpty()) {
            tableGrats.setItems(gratssList);
        } else {
            ObservableList<Gratification> filteredList = FXCollections.observableArrayList(
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
    private void navigateToStats(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dons/stats.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Stats");
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






























    /*
    private ObservableList<gratification> createPage(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, gratssList.size());
        return (ObservableList<gratification>) gratssList.subList(fromIndex, toIndex);
    }

     */



    /*private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, gratssList.size());

        TableView<gratification> tableView = new TableView<>(FXCollections.observableArrayList(gratssList.subList(fromIndex, toIndex)));
        tableView.getColumns().addAll(idColumn, dateColumn, titreColumn, typeColumn, descColumn, montantColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vbox = new VBox(tableView);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        return vbox;
    }

     */



}
