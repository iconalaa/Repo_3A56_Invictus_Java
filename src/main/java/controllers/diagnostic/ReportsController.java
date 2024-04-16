package controllers.diagnostic;

import entities.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import services.diagnostic.ReportService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsController {
    @FXML
    private Label doctorSpaceLabel;
    @FXML
    private Label historyLabel;


    @FXML
    private ListView<Report> reportsListView;

    private ReportService reportService;

    public ReportsController() {

        reportService = new ReportService(); // Initialize the reportService
    }

    public void initialize() {
        try {
            // Fetch reports from the database
            List<Report> reports = reportService.displayAll();

            // Filter reports with is_edited = true
            List<Report> filteredReports = reports.stream()
                    .filter(report -> !report.isIs_edited())
                    .collect(Collectors.toList());

            // Convert list to ObservableList
            ObservableList<Report> observableReports = FXCollections.observableArrayList(filteredReports);

            // Set the items in the ListView
            reportsListView.setItems(observableReports);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the SQLException properly, such as showing an error message to the user
        }

        // Handle click event on the doctorSpaceLabel
        doctorSpaceLabel.setOnMouseClicked(event -> openDashboard(event));
        historyLabel.setOnMouseClicked(event -> openHistory(event));



    }

    private void openDashboard(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/dashboard.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the IOException properly, such as showing an error message to the user
        }
    }
    private void openHistory(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/history.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the IOException properly, such as showing an error message to the user
        }
    }

    @FXML
    public void handleReportSelection() {
        Report selectedReport = reportsListView.getSelectionModel().getSelectedItem();
        if (selectedReport != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/edit_report.fxml"));
                Scene scene = reportsListView.getScene();
                scene.setRoot(loader.load());

                // Get the controller and pass the selected report to it
                ReportEditController controller = loader.getController();
                controller.setSelectedReport(selectedReport);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the IOException properly, such as showing an error message to the user
            }
        }
    }

}
