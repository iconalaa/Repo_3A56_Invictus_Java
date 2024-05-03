package controllers.diagnostic;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import entities.Report;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ReportsItemController {

    @FXML
    private Label username;
    @FXML
    private Label lastname;
    @FXML
    private ImageView itemview;

    private Report report;

    public void updateReportItem(Report report) {
        this.report = report;
        if (report != null) {
            username.setText(report.getImage().getPatient().getName());
            lastname.setText(report.getImage().getPatient().getLastName());
        }
        setImageData();
    }
    public void setReport(Report report) {
        this.report = report;
    }

    @FXML
    public void handleReportSelection(MouseEvent mouseEvent) {
        if (report != null) {
            try {
                // Load the edit report FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/edit_report.fxml"));
                Parent root = loader.load();


                // Get the controller for the edit report
                ReportEditController controller = loader.getController();
                controller.setSelectedReport(report);

                // Get the stage from the mouse event
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

                // Check if the stage is null
                if (stage != null) {
                    // Set the scene on the stage
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    System.err.println("Error: Stage is null");
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the IOException, such as showing an error message to the user
            }
        }
    }

    private void setImageData() {
        if (report != null && report.getImage() != null) {
            String filename = report.getImage().getFilename();
            if (filename != null) {
                File imageFile = new File("src/main/resources/img/testimage/" + filename + ".png");
                System.out.println("Image file exists: " + imageFile.exists());
                Image image = new Image(imageFile.toURI().toString());
                itemview.setImage(image);
            } else {
                System.out.println("Image filename not found.");
            }
        }
    }



}
