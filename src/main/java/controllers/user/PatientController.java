package controllers.user;

import entities.Patient;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import services.user.PatientService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.scene.control.TextField;
public class PatientController {
    @FXML
    private TextField name;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField medicalCase;
    @FXML
    private TextField cnam;
    @FXML
    private TextField assuranceNum;
    @FXML
    private TextField gender;
    @FXML
    private TextField assurance;

    @FXML
    public void addPatient(ActionEvent event) throws IOException {
        LocalDate date = LocalDate.parse("2024-03-23");
        User U = new User(email.getText(), password.getText(), new String[]{"ROLE_PATIENT"}, name.getText(), lastname.getText(), date, gender.getText(), "x");
        Patient P = new Patient(U, medicalCase.getText(), 55, 66, assurance.getText());
        PatientService service = new PatientService();
        try {
            service.add(P);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Created");
            alert.setHeaderText("Welcome To our Compaign");
            alert.show();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
