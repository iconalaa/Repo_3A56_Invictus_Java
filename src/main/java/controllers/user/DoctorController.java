package controllers.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.Doctor;

import javax.mail.*;
import javax.mail.internet.*;
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
import services.user.DoctorService;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;

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
    private Label matriculeError;

    @FXML
    public void addDoctor(ActionEvent event) throws IOException {

        if (validateFields()) {
            LocalDate date = datePicker.getValue();
            // Generate confirmation token
            String confirmationToken = generateConfirmationToken();
//            ******** cryPtage *********
            char[] bcryptChars = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToChar(13, password.getText().toCharArray());
            StringBuilder sb = new StringBuilder();
            for (char c : bcryptChars) {
                sb.append(c);
            }
            String hashedPassword = sb.toString();
            Doctor P = new Doctor(email.getText(),hashedPassword,new String[]{"ROLE_PATIENT"},name.getText(),lastname.getText(),date,gender.getValue(),"x",matricule.getText());
            P.setConfirmationToken(confirmationToken);

            DoctorService service = new DoctorService();
            try {
                service.add(P);
                sendConfirmationEmail(P.getEmail(), confirmationToken);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Created");
                alert.setHeaderText("Welcome To our Compaign");
                alert.show();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid Inputs");
        }
    }
    private String generateConfirmationToken() {
        // Generate a random token
        return UUID.randomUUID().toString();
    }
    // Method to send confirmation email
    private void sendConfirmationEmail(String userEmail, String confirmationToken) {
        // Sender's email address
        // Email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");


        String senderEmail = "radiohub.noreply@gmail.com";
        String senderPassword = "itsRadiohub1";
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Email Confirmation");
            message.setText("Please click on the following link to confirm your email: http://yourapp.com/confirm?token="+confirmationToken);
            Transport.send(message);
            System.out.println("Confirmation email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean validateFields() {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        String namePattern = "^[A-Za-z]{3,}(?:['-][A-Za-z]+)*$";
        String matriculePattern = "^[A-Za-z0-9]{6,}$";
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
        if (datePicker.getValue() == null || datePicker.getValue().isAfter(LocalDate.of(2021, 12, 31))) {
            dateError.setText("Chose Date of birth please !");
            test = false;
        }
        if (!matricule.getText().matches(matriculePattern)) {
            matriculeError.setText("Invalid Matricule !");
            test = false;
        }
        return test;
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
