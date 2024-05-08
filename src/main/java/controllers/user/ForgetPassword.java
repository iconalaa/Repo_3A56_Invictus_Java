package controllers.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import services.user.UserService;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;

public class ForgetPassword {

    @FXML
    private TextField resetMail;
    @FXML
    private TextField verifCode;
    @FXML
    private Label emailError;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private VBox newPassVbox;

    private String randomNumber;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXY0123456789";
    private static final UserService ps = new UserService();
    private static final SecureRandom RANDOM = new SecureRandom();
    private User user;


    @FXML
    void sendMail(ActionEvent event) throws Exception {
        GMailerController mail = new GMailerController();
        if (ps.getUserByEmail(resetMail.getText()) == null) {
            emailError.setText("Email Doesn't Exist in the DataBase");
            return;
        }
        user = ps.getUserByEmail(resetMail.getText());
        mail.reciever = resetMail.getText();
        randomNumber = generateCode(8);
        mail.sendMail("Password Reset Request",
                "<html>" +
                        "<body>" +
                        "<div style=\"width: 90%; margin: 20px auto;box-shadow: 0 0 5px grey;padding: 16px;\">" +
                        "<div style=\"background-color: #1997ff; padding: 10px 0;\">" +
                        "<h1 style=\"text-align: center; color: white;\">RESET PASSWORD</h1>" +
                        "</div>" +
                        "<div style=\"background-color: #FFF; padding: 10px 5px;\">" +
                        "<p style=\"font-size:20px;\">This is the <strong>Verification Code :</strong></p>" +
                        "</div>" +
                        "<div style=\"background-color: #1997ff; padding: 5px 0; width: 50%;margin: 0 auto;\">" +
                        "<h3 style=\"text-align: center; color: #FFF;\">" +
                        randomNumber +
                        "</h3>" +
                        "</div>" +
                        "<br>" +
                        "<p>Best Regards,</p>" +
                        "<i>RadioHub Team</i>" +
                        "</div>" +
                        "</body>" +
                        "</html>"
        );


    }

    @FXML
    void submit(ActionEvent event) throws IOException {

        if (randomNumber.equals(verifCode.getText())) {
            newPassVbox.setVisible(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The Code is Wrong");
            alert.showAndWait();
        }
    }

    public static String generateCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }
        return code.toString();
    }

    @FXML
    void confirm(ActionEvent event) throws SQLException {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        if (!passwordField.getText().matches(passwordPattern)) {
            passwordError.setVisible(true);
            return;
        }
        passwordError.setVisible(false);
        char[] bcryptChars = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToChar(13, passwordField.getText().toCharArray());
        StringBuilder sb = new StringBuilder();
        for (char c : bcryptChars) {
            sb.append(c);
        }
        String hashedPassword = sb.toString();
        user = ps.getUserByEmail(resetMail.getText());
        System.out.println(user.toString());
        user.setPassword(hashedPassword);
        ps.update(user, user.getUser_id());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("reset");
        alert.setHeaderText("Your Password Has changed");
        alert.showAndWait();

    }


}
