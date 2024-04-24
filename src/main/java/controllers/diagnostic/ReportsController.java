package controllers.diagnostic;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import services.diagnostic.ReportService;
import entities.Report;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ReportsController {
    @FXML
    private Label doctorSpaceLabel;
    @FXML
    private Label historyLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;

    private ReportService reportService;

    public ReportsController() {
        reportService = new ReportService(); // Initialize the reportService
    }
    public void initialize() {
        doctorSpaceLabel.setOnMouseClicked(this::openDashboard);
        historyLabel.setOnMouseClicked(this::openHistory);

        try {
            List<Report> allReports = reportService.displayAll();
            List<Report> filteredReports = new ArrayList<>();

            // Filter reports where edited is false
            for (Report report : allReports) {
                if (!report.isIs_edited()) { // Assuming isEdited() returns true if the report is edited
                    filteredReports.add(report);
                }
            }

            int column = 0;
            int row = 0;

            for (Report report : filteredReports) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/report-item.fxml"));

                try {
                    Node reportItem = loader.load();
                    gridPane.add(reportItem, column, row);

                    // Get the controller associated with the loaded report item
                    ReportsItemController reportItemController = loader.getController();

                    // Set the report object to the report item controller
                    reportItemController.setReport(report);
                    reportItemController.updateReportItem(report);


                    // Add event handler for report selection
                    reportItem.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        reportItemController.handleReportSelection(event);
                    });

                    // Increment column and row values
                    column++;
                    if (column == 3) {
                        column = 0;
                        row++;
                    }

                    // Adjust grid width and height
                    gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxWidth(Region.USE_PREF_SIZE);

                    gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxHeight(Region.USE_PREF_SIZE);

                    GridPane.setMargin(reportItem, new Insets(10));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions properly
        }
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




    public void test(MouseEvent mouseEvent) {
        System.out.println("prees works");
    }
}
