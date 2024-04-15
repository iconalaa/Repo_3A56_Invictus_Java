package controllers.diagnostic;

import entities.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.diagnostic.ReportService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReportsController {
    @FXML
    private GridPane reportGrid;

    @FXML
    private Label doctorSpaceLabel;

    public void initialize() {
        populateReportGrid();
        // Add event handler for Doctor's Space label
        doctorSpaceLabel.setOnMouseClicked(event -> openDashboard(event));
    }

    private void populateReportGrid() {
        ReportService reportService = new ReportService();
        List<Report> reports;
        try {
            reports = reportService.displayAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        int row = 0;
        for (Report report : reports) {
            Label interpretationLabel = new Label(report.getInterpretation_med());
            reportGrid.add(interpretationLabel, 0, row);
            row++;
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
}
