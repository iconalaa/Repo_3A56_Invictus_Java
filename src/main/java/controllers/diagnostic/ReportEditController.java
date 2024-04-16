package controllers.diagnostic;

import entities.Report;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.diagnostic.ReportService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class ReportEditController {
    @FXML
    private DatePicker dateTextField;

    @FXML
    private TextArea interpretationMedTextField;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private ImageView backButton;

    private ReportService reportService; // Initialize the ReportService
    private Report selectedReport;

    // Initialize the reportService directly in the controller
    public ReportEditController() {
        reportService = new ReportService();
    }
    @FXML
    private void initialize() {

        backButton.setOnMouseClicked(event -> backToDash(event));

    }

    private void backToDash(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/reports.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedReport(Report selectedReport) {
        this.selectedReport = selectedReport;

    }

    @FXML
    public void handleUpdate() {
        // Check if either of the fields is empty
        if (dateTextField.getValue() == null && interpretationMedTextField.getText().isEmpty()) {
            // Both fields are empty
            errorMessageLabel.setText("Please provide date & interpretation.");
        } else if (dateTextField.getValue() == null) {
            // Date field is empty
            errorMessageLabel.setText("Please select a date.");
        } else if (interpretationMedTextField.getText().isEmpty()) {
            // Interpretation field is empty
            errorMessageLabel.setText("Please provide interpretation.");
        } else {
            // All fields are filled, clear any previous error message
            errorMessageLabel.setText("");

            // Proceed with the update logic since all fields are filled
            if (selectedReport != null) {
                try {
                    // Update the fields of the selected report
                    selectedReport.setDate(Date.valueOf(dateTextField.getValue()));
                    selectedReport.setInterpretation_med(interpretationMedTextField.getText());
                    selectedReport.setIs_edited(true);

                    // Update the report in the database
                    reportService.update(selectedReport, selectedReport.getId());

                    // Close the current stage
                    Stage stage = (Stage) dateTextField.getScene().getWindow();
                    stage.close();

                    // Load the reports.fxml page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/reports.fxml"));
                    Stage reportsStage = new Stage();
                    reportsStage.setScene(new Scene(loader.load()));
                    reportsStage.show();
                } catch (SQLException | IOException e) {
                    e.printStackTrace(); // Handle the exception properly
                }
            }
        }


}


    public void returnBack(MouseEvent mouseEvent) {
    }
}
