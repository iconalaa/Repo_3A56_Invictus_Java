package controllers.user;

import entities.Patient;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
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
        String hashedPassword = HashPassword.hashPassword(password.getText());
        User U = new User(email.getText(), hashedPassword, new String[]{"ROLE_PATIENT"}, name.getText(), lastname.getText(), date, gender.getValue(), "x");
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
    @FXML
    void returnSignUp(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/signUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
            stage.setTitle("RadioHub");
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
