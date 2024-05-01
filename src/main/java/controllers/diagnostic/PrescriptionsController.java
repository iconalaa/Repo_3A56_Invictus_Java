package controllers.diagnostic;

import entities.Prescription;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import services.diagnostic.PrescriptionService;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

public class PrescriptionsController {

    @FXML
    private TextArea prescriptionContent;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label errorLabel;
    @FXML
    private Canvas signatureCanvas;

    @FXML
    private TextArea diseaseTextArea;
    @FXML
    private TextArea medicationsTextArea;

    private HashMap<String, String[]> database = new HashMap<>();

    private GraphicsContext gc;
    private int selectedReportId;
    private PrescriptionService prescriptionService;
    @FXML
    public void initialize() {

             //// database implimentation
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/database.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                database.put(parts[0].trim().toLowerCase(), parts[1].split(": "));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
          ////signature
        prescriptionService = new PrescriptionService();
        gc = signatureCanvas.getGraphicsContext2D();
        initDraw(gc);
        signatureCanvas.setOnMousePressed(e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        signatureCanvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });
    }
    @FXML
    protected void handleSubmit(ActionEvent actionEvent) {
        if (validateInput()) {
            String signatureFilename = saveSignature();
            if (signatureFilename != null) {
                // Create a new Prescription object with the form data
                Prescription newPrescription = new Prescription(prescriptionContent.getText(), signatureFilename);

                // Use a static report_id for testing purposes
                int reportId = selectedReportId; // Replace with the actual report_id from your database

                // Call the add method from the PrescriptionService
                try {
                    prescriptionService.add2(newPrescription, reportId);

                    // Load history.fxml directly
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/history.fxml"));
                    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    window.setScene(new Scene(loader.load()));
                    window.show();

                } catch (SQLException | IOException e) {
                    errorLabel.setText("Failed to add prescription to the database.");
                    e.printStackTrace();
                }
            } else {
                errorLabel.setText("Failed to save signature.");
            }
        }
    }
    private void initDraw(GraphicsContext gc) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
    }
    private boolean validateInput() {
        String errorMessage = "";
        if (prescriptionContent.getText().trim().isEmpty()) {
            errorMessage += "Prescription content cannot be empty.\n";
        }
        if (datePicker.getValue() == null || datePicker.getValue().isBefore(LocalDate.now())) {
            errorMessage += "Please pick a valid date.\n";
        }
        if (isCanvasEmpty()) {
            errorMessage += "Please draw a signature.\n";
        }
        errorLabel.setText(errorMessage);
        return errorMessage.isEmpty(); // Return true if all checks pass
    }
    private boolean isCanvasEmpty() {
        WritableImage snapshot = signatureCanvas.snapshot(null, null);
        PixelReader pixelReader = snapshot.getPixelReader();
        for (int y = 0; y < snapshot.getHeight(); y++) {
            for (int x = 0; x < snapshot.getWidth(); x++) {
                // Read the color of the pixel at x, y
                Color color = pixelReader.getColor(x, y);
                // Check if the pixel color is not white
                if (!color.equals(Color.WHITE)) {
                    return false; // The canvas is not empty
                }
            }
        }
        return true; // The canvas is empty
    }
    private String saveSignature() {
        WritableImage image = signatureCanvas.snapshot(null, null);
        String filename = "signature_" + UUID.randomUUID();

        // Specify the directory path within the resources folder
        String directoryPath = "src/main/resources/img/signatures"; // Update with your desired path
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it does not exist
        }

        // Save the image in the specified directory
        File file = new File(directory, filename + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            return filename; // Return just the filename without the extension
        } catch (IOException e) {
            errorLabel.setText("Failed to save signature.");
            e.printStackTrace();
            return null; // Return null if save is unsuccessful
        }
    }

    public void setSelectedReportId(int selectedReportId) {
        this.selectedReportId = selectedReportId;
    }
    public void returnDoctorSpace(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/history.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void searchMedications(ActionEvent event) {
        String disease = diseaseTextArea.getText().trim().toLowerCase(); // Convert to lowercase
        if (!disease.isEmpty()) {
            String[] medications = database.getOrDefault(disease, new String[]{"No medications found."});
            StringBuilder result = new StringBuilder();
            for (String medication : medications) {
                result.append(medication).append("\n");
            }
            medicationsTextArea.setText(result.toString());
        } else {
            medicationsTextArea.setText("Please enter a disease or medical problem.");
        }
    }
}
