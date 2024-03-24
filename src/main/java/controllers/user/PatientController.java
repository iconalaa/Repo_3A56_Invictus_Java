package controllers.user;

import entities.Patient;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.user.PatientService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class PatientController {
    @FXML
    private TextField name;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private TextField medicalCase;
    @FXML
    private TextField n_cnam;
    @FXML
    private TextField num_assurance;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private TextField assurance;
    @FXML
    private DatePicker datePicker;

    @FXML
    public void addPatient(ActionEvent event) throws IOException {
        LocalDate date = datePicker.getValue();
        int cnam = Integer.parseInt(n_cnam.getText());
        int assuranceNum = Integer.parseInt(num_assurance.getText());
        User U = new User(email.getText(), password.getText(), new String[]{"ROLE_PATIENT"}, name.getText(), lastname.getText(), date, gender.getValue(), "x");
        Patient P = new Patient(U, medicalCase.getText(), cnam, assuranceNum, assurance.getText());
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
