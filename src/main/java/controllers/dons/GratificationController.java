package controllers.dons;

import Entities.donateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GratificationController implements Initializable {

    @FXML
    private TableView<gratification> tableGrats;

    @FXML
    private TableColumn<gratification, Integer> idColumn;

    @FXML
    private Button statsbutton;

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

    /*@FXML
    private Pagination pagination;

     */

    private final int itemsPerPage = 5;

    @FXML
    private ObservableList<gratification> gratssList = FXCollections.observableArrayList();


    private void loadGratificationData() {
        try {
            ServiceGratification service  = new ServiceGratification();
            List<gratification> gratss = service.afficher();
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
        gratification selectedGratification = tableGrats.getSelectionModel().getSelectedItem();
        if (selectedGratification != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UpdateGratification.fxml"));
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
    @FXML
    private void navigateToStats(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/stats.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menu.getScene().getWindow();
            stage.setScene(new Scene(root));
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
