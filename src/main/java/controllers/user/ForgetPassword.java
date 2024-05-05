package controllers.user;
import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
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
    private String randomNumber;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final UserService ps = new UserService();
    private static final SecureRandom RANDOM = new SecureRandom();
    private User user;



    @FXML
    void sendMail(ActionEvent event) throws Exception {
        GMailerController mail = new GMailerController();
        if (ps.getUserByEmail(resetMail.getText()) == null){
            emailError.setText("Email Doesn't Exist in the DataBase");
            return;
        }
        user = ps.getUserByEmail(resetMail.getText());
        mail.reciever = resetMail.getText();
        randomNumber = generateCode(8);
        mail.sendMail("Password Reset Request", """
        <html>
          <body>
              <h1 style="color: #007bff;text-align:center;font-size:48px;">RESET PASSWORD</h1>
              <hr><br>
              <p style="font-size:20px;">This is the <strong>Verification Code :</strong></p>
              <p
                style="border-radius:5px;
                width:50%;
                margin:0 auto;
                display:flex;
                justify-content:center;
                text-align:center;
                letter-spacing:2px;
                background-color:#222;
                color:white;
                padding:10px 30px;"
                 >
                 """ + randomNumber + """
                 </p>
              <p style="font-style: italic;">Best regards,<br><strong>RadioHub Team</strong></p>
          </body>
          </html>
        """);

    }

    @FXML
    void submit(ActionEvent event) throws IOException {

        if(randomNumber.equals(verifCode.getText())){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/newPassword.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
            stage.setTitle("New Password");
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

        }else{
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
    void submitNewPassword(ActionEvent event) throws SQLException {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        if (!passwordField.getText().matches(passwordPattern)) {
            passwordError.setText("Invalid Password Minimum 6 Characters !");
            return;
        }
        char[] bcryptChars = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToChar(13, passwordField.getText().toCharArray());
        StringBuilder sb = new StringBuilder();
        for (char c : bcryptChars) {
            sb.append(c);
        }
        String hashedPassword = sb.toString();
        user.setPassword(hashedPassword);
        ps.update(user,user.getUser_id());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("reset");
        alert.setHeaderText("Your Password Has changed");
        alert.showAndWait();

    }


}
