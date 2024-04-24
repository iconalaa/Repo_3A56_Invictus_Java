package controllers.diagnostic;

import entities.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.diagnostic.ReportService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryController {

    public TextField searchprompt;
    @FXML
    private Label reportsListLabel;
    @FXML
    private Label doctorsapcelabel;
    private ReportService reportService;

    @FXML
    private ListView<Report> HistoryView;

    public HistoryController() {
        reportService = new ReportService();
    }

    @FXML
    private void initialize() {
        try {
            // Fetch reports from the database
            List<Report> reports = reportService.displayAll();

            // Convert list to ObservableList
            ObservableList<Report> observableReports = FXCollections.observableArrayList(reports);

            // Set the custom cell factory for the ListView
            HistoryView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Report item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Create labels for each piece of information
                        Label doctorLabel = new Label("Doctor: " + item.getImage().getPatient().getName() + " " + item.getImage().getPatient().getLastName());
                        Label dateLabel = new Label("Date: " + item.getDate());
                        Label medInterpretationLabel = new Label("Interpretation (Medical): " + item.getInterpretation_med());
                        Label radInterpretationLabel = new Label("Interpretation (Radiology): " + item.getInterpretation_rad());

                        // Customize label styles if needed
                        doctorLabel.setStyle("-fx-font-weight: bold;");
                        dateLabel.setStyle("-fx-font-weight: bold;");

                        // Create a VBox to hold the labels
                        VBox vbox = new VBox(doctorLabel, dateLabel, medInterpretationLabel, radInterpretationLabel);
                        vbox.setSpacing(5); // Adjust spacing between labels if needed

                        // Set the VBox as the graphic of the ListCell
                        setGraphic(vbox);
                    }
                }
            });

            // Set the items in the ListView
            HistoryView.setItems(observableReports);

            // Add listener to search text field
            searchprompt.textProperty().addListener((observable, oldValue, newValue) -> {
                // Filter reports based on the doctor's matricule
                ObservableList<Report> filteredReports = observableReports.filtered(report ->
                        report.getDoctor().getName().toLowerCase().contains(newValue.toLowerCase())
                );

                // Update the ListView with the filtered reports
                HistoryView.setItems(filteredReports);
            });
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the SQLException properly, such as showing an error message to the user
        }

        reportsListLabel.setOnMouseClicked(event -> openReports(event));
        doctorsapcelabel.setOnMouseClicked(event -> openSpace(event));
    }


    private void openSpace(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/dashboard.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    private void openPrescription(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/prescription.fxml"));
            Parent prescriptionRoot = loader.load();

            PrescriptionsController prescriptionsController = loader.getController();
            Report selectedReport = HistoryView.getSelectionModel().getSelectedItem();
            if (selectedReport != null) {
                prescriptionsController.setSelectedReportId(selectedReport.getId());
            }

            // Get the current scene and replace its root with the prescription form
            Scene scene = HistoryView.getScene();
            scene.setRoot(prescriptionRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
