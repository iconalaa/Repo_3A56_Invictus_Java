package controllers.user;

import entities.Radiologist;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.user.RadiologistService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class RadiologistController {

    @FXML
    private TextField name;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private TextField mat_cnom;

    @FXML
    private ComboBox<String> gender;
 ;
    @FXML
    private DatePicker datePicker;
    @FXML
    public void addRadiologist(ActionEvent event) throws IOException {
        LocalDate date = datePicker.getValue();
        String hashedPassword = HashPassword.hashPassword(password.getText());
        User U = new User(email.getText(), hashedPassword, new String[]{"ROLE_PATIENT"}, name.getText(), lastname.getText(), date, gender.getValue(), "x");
        Radiologist R = new Radiologist(U,mat_cnom.getText());
        RadiologistService service = new RadiologistService();
        try {
            service.add(R);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Created");
            alert.setHeaderText("Welcome To our Compaign");
            alert.show();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
