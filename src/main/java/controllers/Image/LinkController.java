package controllers.Image;

import entities.Image;
import entities.Report;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.diagnostic.ReportService;
import services.user.UserService;

import java.sql.SQLException;
import java.util.List;

public class LinkController {
    @FXML
    private ComboBox<User> doctorsCol;

    @FXML
    private TextArea radtext;



    private final UserService doctorService = new UserService();
    private ReportService reportService = new ReportService();

    @FXML
    void initialize() {
        System.out.println(ImageDashboard.selectedImage.getId());
        try {
            List<User> allPatients = doctorService.showAlld();

            ObservableList<User> doctors = FXCollections.observableArrayList();

            User associatedDoctor = null;

            if (associatedDoctor != null) {
                for (User patient : allPatients) {
                    if (!doctors.equals(associatedDoctor)) {
                        doctors.add(patient);
                    }
                }
            } else {
                doctors.addAll(allPatients);
            }

            doctorsCol.setItems(doctors);


            if (!doctors.isEmpty()) {
                doctorsCol.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addreport(MouseEvent event) {
        try {
            // Get the selected doctor from the ComboBox
            User selectedDoctor = doctorsCol.getValue();

            // Get the radiologist's interpretation from the TextArea
            String radiologistInterpretation = radtext.getText();

            // Check if a doctor is selected and the radiologist's interpretation is not empty
            if (selectedDoctor != null && !radiologistInterpretation.isEmpty()) {
                // Create a new Report object with the radiologist's interpretation
                Report report = new Report();
                report.setInterpretation_rad(radiologistInterpretation);

                // Call the add method of your report service to add the report
                reportService.add(report, selectedDoctor, ImageDashboard.selectedImage);

                // Get the scene and window
                Node sourceNode = (Node) event.getSource();
                Stage stage = (Stage) sourceNode.getScene().getWindow();

                // Close the window
                stage.close();
            } else {
                // Handle the case where either the doctor is not selected or the interpretation is empty
                System.out.println("Please select a doctor and provide the radiologist's interpretation.");
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        }
    }


}
