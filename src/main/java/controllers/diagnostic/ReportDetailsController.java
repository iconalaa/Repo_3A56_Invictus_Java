package controllers.diagnostic;

import entities.Report;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.diagnostic.ReportService;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class ReportDetailsController {

    private ReportService reportService = new ReportService();
    private Report report;
    private int reportId;
    private ReportsAdminController parentController;

    @FXML
    private TextField interpretationMedField;

    @FXML
    private TextField interpretationRadField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label doctorLabel;

    @FXML
    private Label patientLabel;

    @FXML
    private ImageView reportImageView;

    public void setReportAndId(Report report, int reportId, ReportsAdminController parentController) {
        this.report = report;
        this.reportId = reportId;
        this.parentController = parentController;

        // Initialize the text fields with the current report information
        interpretationMedField.setText(report.getInterpretation_med());
        interpretationRadField.setText(report.getInterpretation_rad());

        // Set the doctor and patient labels
        doctorLabel.setText(report.getImage().getPatient().getName()+" "+report.getImage().getPatient().getLastName());
        patientLabel.setText(report.getDoctor().getName()+ " "+report.getDoctor().getLastName());

        // Set the image
        setImageData();

        // Convert java.sql.Date to java.util.Date
        if (report.getDate() != null) {
            java.util.Date utilDate = new java.util.Date(report.getDate().getTime());
            LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Set the localDate to the DatePicker
            datePicker.setValue(localDate);
        } else {
            // Handle the case where the date is null
            datePicker.setValue(null); // Set the DatePicker value to null
        }
    }

    @FXML
    void updateReport() {
        try {
            // Get the updated values from the text fields and DatePicker
            String interpretationMed = interpretationMedField.getText();
            String interpretationRad = interpretationRadField.getText();
            LocalDate localDate = datePicker.getValue();
            Date sqlDate = null;

            // Convert LocalDate to java.sql.Date if it's not null
            if (localDate != null) {
                sqlDate = Date.valueOf(localDate);
            }

            // Update the report object with the new values
            report.setInterpretation_med(interpretationMed);
            report.setInterpretation_rad(interpretationRad);
            report.setDate(sqlDate);

            // Call the update method in the ReportService to update the report in the database
            reportService.update(report, reportId);

            // Refresh the display in the parent controller
            parentController.refreshDisplay();

            // Close the window after successful update
            Stage stage = (Stage) interpretationMedField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error updating report: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void setImageData() {
        if (report != null && report.getImage() != null) {
            String filename = String.valueOf(report.getImage().getId());
            if (filename != null) {
                File imageFile = new File("src/main/java/dicom/" + filename + ".png");
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    reportImageView.setImage(image);
                } else {
                    System.out.println("Image file not found.");
                }
            } else {
                System.out.println("Image filename not found.");
            }
        }
    }
}
