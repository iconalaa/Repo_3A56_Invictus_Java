package controllers.diagnostic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.diagnostic.PrescriptionService;
import services.diagnostic.ReportService;

import java.io.IOException;
import java.sql.SQLException;


public class DashoardController {
    @FXML
    private Label reportsListLabel; // Add fx:id attribute here
    @FXML
     private Label historylabel;
    @FXML
    private Label reportsdone;
    @FXML
    private Label prescriptionsLabel;
    @FXML
    private Label reportsLabel;


    @FXML
    private void initialize() {

        fetchAndDisplayCounts();
        reportsListLabel.setOnMouseClicked(event -> openReports(event));
        historylabel.setOnMouseClicked(event -> openHistory(event));

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

    private void fetchAndDisplayCounts() {
        PrescriptionService prescriptionService = new PrescriptionService();
        ReportService reportService = new ReportService();

        try {
            // Get count of awaiting reports
            int awaitingReportsCount = reportService.getAwaitingReportsCount();
            reportsLabel.setText(String.valueOf(awaitingReportsCount));

            // Get count of reports done
            int reportsDoneCount = reportService.getReportsDoneCount();
            reportsdone.setText(String.valueOf(reportsDoneCount));

            // Get count of prescriptions
            int prescriptionsCount = prescriptionService.getAllPrescriptionsCount();
            prescriptionsLabel.setText(String.valueOf(prescriptionsCount));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }
}
