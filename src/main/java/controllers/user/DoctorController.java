package controllers.user;

import entities.Doctor;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.user.DoctorService;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class DoctorController {
    @FXML
    private TextField name;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private TextField matricule;
    @FXML
    private ComboBox<String> gender;

    @FXML
    private DatePicker datePicker;

    @FXML
    public void addDoctor(ActionEvent event) throws IOException {
        LocalDate date = datePicker.getValue();
        User U = new User(email.getText(), password.getText(), new String[]{"ROLE_PATIENT"}, name.getText(), lastname.getText(), date, gender.getValue(), "x");
        Doctor P = new Doctor(U, matricule.getText());
        DoctorService service = new DoctorService();
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
