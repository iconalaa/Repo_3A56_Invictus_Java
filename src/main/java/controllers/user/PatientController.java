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
    private Label nameError;
    @FXML
    private Label lastnameError;
    @FXML
    private Label emailError;
    @FXML
    private Label passwordError;
    @FXML
    private Label genderError;
    @FXML
    private Label dateError;
    @FXML
    private Label assuranceError;
    @FXML
    private Label assuranceNumberError;
    @FXML
    private Label cnamError;
    @FXML
    private Label medicalcaseError;


    @FXML
    public void addPatient(ActionEvent event) throws IOException {
        if (validateFields()) {
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
        } else System.out.println("Invalid Inputs !");
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

    @FXML
    public boolean validateFields() {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        String namePattern = "^[A-Za-z]{3,}(?:['-][A-Za-z]+)*$";
        String numberPattern = "\\d+";
        boolean test = true;
        if (!name.getText().matches(namePattern)) {
            nameError.setText("Invalid Name !");
            test = false;
        }
        if (!lastname.getText().matches(namePattern)) {
            lastnameError.setText("Invalid Last Name !");
            test = false;
        }
        if (!email.getText().matches(emailPattern)) {
            emailError.setText("Invalid Email !");
            test = false;
        }
        if (!password.getText().matches(passwordPattern)) {
            passwordError.setText("Invalid Password Minimum 6 Characters !");
            test = false;
        }
        if (gender.getValue() == null) {
            genderError.setText("Chose a gender !");
            test = false;
        }
        if (datePicker.getValue() == null) {
            dateError.setText("Chose Date of birth please !");
            test = false;
        }
        if (!assurance.getText().matches(namePattern)) {
            assuranceError.setText("Invalid Assurance Name !");
            test = false;
        }
        if (!medicalCase.getText().matches(namePattern)) {
            medicalcaseError.setText("Invalid Medical Case !");
            test = false;
        }
        if (!num_assurance.getText().matches(numberPattern)) {
            assuranceNumberError.setText("Write only numbers !");
            test = false;
        }
        if (!n_cnam.getText().matches(numberPattern)) {
            cnamError.setText("Write only numbers !");
            test = false;
        }
        return test;
    }

}