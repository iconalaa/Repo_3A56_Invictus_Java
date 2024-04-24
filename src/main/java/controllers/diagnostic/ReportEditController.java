package controllers.diagnostic;

import entities.Report;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.diagnostic.ImageService;
import services.diagnostic.ReportService;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

public class ReportEditController {

    @FXML
    private DatePicker dateTextField;
    @FXML
    private TextArea interpretationMedTextField;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private ImageView backButton;
    @FXML
    private Text textradio;
    @FXML
    private ImageView reportimage;

    private ReportService reportService;
    private ImageService imageService;
    private Report selectedReport;

    public ReportEditController() {
        imageService = new ImageService();
        reportService = new ReportService();
    }

    @FXML
    private void initialize() {
        interpretationMedTextField.setText(""); // Use an empty string instead of null
        dateTextField.valueProperty().addListener((observable, oldValue, newValue) -> {
            errorMessageLabel.setText(""); // Clear error message when date changes
        });
        interpretationMedTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorMessageLabel.setText(""); // Clear error message when interpretation changes
        });
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

        if (selectedReport != null) {
            if (selectedReport.getDate() != null) {
                java.util.Date utilDate = new java.util.Date(selectedReport.getDate().getTime());
                LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dateTextField.setValue(localDate);
            }
            interpretationMedTextField.setText(selectedReport.getInterpretation_med());
            textradio.setText(selectedReport.getInterpretation_rad());

        }
        setImageData();
    }

    @FXML
    public void handleUpdate() {
        if (dateTextField.getValue() == null || interpretationMedTextField.getText().isEmpty()) {
            errorMessageLabel.setText("Please provide both date and interpretation.");
            return;
        }

        errorMessageLabel.setText("");

        if (selectedReport != null) {
            try {
                selectedReport.setDate(Date.valueOf(dateTextField.getValue()));
                selectedReport.setInterpretation_med(interpretationMedTextField.getText());
                selectedReport.setIs_edited(true);

                reportService.update(selectedReport, selectedReport.getId());

                Stage stage = (Stage) dateTextField.getScene().getWindow();
                stage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/reports.fxml"));
                Stage reportsStage = new Stage();
                reportsStage.setScene(new Scene(loader.load()));
                reportsStage.show();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                errorMessageLabel.setText("Error updating report. Please try again.");
            }
        }
    }

    public void returnBack(MouseEvent mouseEvent) {
    }

    private void setImageData() {
        if (selectedReport != null && selectedReport.getImage() != null) {
            String filename = selectedReport.getImage().getFilename();
            System.out.println(selectedReport.getDoctor().getName());
            if (filename != null) {
                File imageFile = new File("src/main/resources/img/testimage/" + filename + ".png");
                System.out.println("Image file exists: " + imageFile.exists());
                Image image = new Image(imageFile.toURI().toString());
                reportimage.setImage(image);
            } else {
                System.out.println("Image filename not found.");
            }
        }
    }
}
