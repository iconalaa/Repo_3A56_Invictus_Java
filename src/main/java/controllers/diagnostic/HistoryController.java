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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.diagnostic.ReportService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryController {

    @FXML
    private Label reportsListLabel;
    @FXML
    private Label doctorsapcelabel;
    private ReportService reportService;

    @FXML
    private ListView<Report> HistoryView;
    public HistoryController(){
        reportService = new ReportService();
    }

    @FXML
    private void initialize() {
        try {
            // Fetch reports from the database
            List<Report> reports = reportService.displayAll();

            // Filter reports with is_edited = true
            List<Report> filteredReports = reports.stream()
                    .filter(report -> report.isIs_edited())
                    .collect(Collectors.toList());

            // Convert list to ObservableList
            ObservableList<Report> observableReports = FXCollections.observableArrayList(filteredReports);

            // Set the items in the ListView
            HistoryView.setItems(observableReports);
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

}
