package controllers.diagnostic;

import controllers.user.SessionManager;
import entities.Prescription;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import services.diagnostic.PdfPrescriptionGenerator;
import services.diagnostic.PrescriptionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PrescritionsListController {


    @FXML
    private Label dashboardLabel;
    @FXML
    private ListView<Prescription> PrescriptionsView;
    @FXML
    private Label historylabel;

    @FXML
    private Label reportsListLabel;

    User loggedInUser = SessionManager.getLoggedInUser();
    private PrescriptionService prescriptionService;
    public PrescritionsListController(){
        prescriptionService =new PrescriptionService();

    }

    @FXML
    private void initialize(){

        try {


            List<Prescription> prescriptions = prescriptionService.displayAll(loggedInUser.getUser_id());

            ObservableList<Prescription> observablePrescriptions = FXCollections.observableArrayList(prescriptions);


            PrescriptionsView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Prescription item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label doctorLabel = new Label("Patient: " + item.getReport().getImage().getPatient().getName() + " " + item.getReport().getImage().getPatient().getLastName() );
                        Label dateLabel = new Label("Date: " + item.getDate());
                        Label medInterpretationLabel = new Label("content (Medical): " + item.getContenu());

                        doctorLabel.setStyle("-fx-font-weight: bold;");
                        dateLabel.setStyle("-fx-font-weight: bold;");
                        medInterpretationLabel.setStyle("-fx-font-weight: bold;");

                        // Create a VBox to hold the labels
                        VBox vbox = new VBox(doctorLabel,dateLabel, medInterpretationLabel);
                        vbox.setSpacing(10); // Adjust spacing between labels if needed

                        // Set the VBox as the graphic of the cell
                        setGraphic(vbox);
                    }
                }
            });


            // Set the items in the ListView
            PrescriptionsView.setItems(observablePrescriptions);


        } catch (SQLException e) {
            e.printStackTrace(); // Handle the SQLException properly, such as showing an error message to the user
        }
        dashboardLabel.setOnMouseClicked(this::openDashboard);
        historylabel.setOnMouseClicked(this::openHistory);
        reportsListLabel.setOnMouseClicked(this::openReports);


    }


    private void openReports(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/reports.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openHistory(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/history.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openDashboard(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/dashboard.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void openPdfPrescription(ActionEvent actionEvent) {
        Prescription selectedPrescription = PrescriptionsView.getSelectionModel().getSelectedItem();
        if (selectedPrescription != null) {
            PdfPrescriptionGenerator.generatePdfPrescription(selectedPrescription); // Call the method and pass the selected prescription
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("PDF Prescription has been generated and saved to your desktop.");
            alert.showAndWait();
        }
    }

}
