package controllers.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.user.UserService;

import java.sql.SQLException;


public class SettingsController {

    @FXML
    private TextField fxLastName;

    @FXML
    private TextField fxName;
    @FXML
    private PasswordField fxPassword;
    private User user;
    private final UserService ps = new UserService();

    public void setUser(User user) {
        this.user = user;
        fxName.setText(user.getName());
        fxLastName.setText(user.getLastName());
    }

    @FXML
    void updateAction(ActionEvent event) throws SQLException {
        user.setName(fxName.getText());
        user.setLastName(fxLastName.getText());
        if(!fxPassword.getText().isEmpty()){
            char[] bcryptChars = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToChar(13, fxPassword.getText().toCharArray());
            StringBuilder sb = new StringBuilder();
            for (char c : bcryptChars) {
                sb.append(c);
            }
            String hashedPassword = sb.toString();
            user.setPassword(hashedPassword);
        }
        ps.update(user,user.getUser_id());
    }
}
