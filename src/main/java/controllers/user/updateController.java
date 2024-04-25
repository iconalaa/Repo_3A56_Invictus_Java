package controllers.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.user.UserService;

import java.sql.SQLException;
import java.time.LocalDate;

public class updateController {
    @FXML
    private TextField fxEmail;

    @FXML
    private TextField fxLastName;

    @FXML
    private TextField fxMatricule;

    @FXML
    private TextField fxName;

    @FXML
    private TextField fxPassword;

    @FXML
    private DatePicker addDate;

    @FXML
    private TextField addEmail;

    @FXML
    private ComboBox<String> addGender;

    @FXML
    private TextField addLastName;


    @FXML
    private TextField addName;

    @FXML
    private TextField addPassword;
    @FXML
    private ComboBox<String> addRole;

    private final UserService ps = new UserService();
    private User user = new User();

    public void setModifyUser(User user) {
        this.user = user;
        fxName.setText(user.getName());
        fxLastName.setText(user.getLastName());
        fxEmail.setText(user.getEmail());
        fxPassword.setText(user.getPassword());
        fxMatricule.setText(user.getLastName());
    }



    @FXML
    void addAction(ActionEvent event) {
        String name = addName.getText();
        String lastname = addLastName.getText();
        String email = addEmail.getText();
        String gender = addGender.getValue();
        LocalDate local = addDate.getValue();
        char[] bcryptChars = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToChar(13, addPassword.getText().toCharArray());
        StringBuilder sb = new StringBuilder();
        for (char c : bcryptChars) {
            sb.append(c);
        }
        String hashedPassword = sb.toString();
        String[] role = new String[15];
        if (addRole.getValue() == "Radiologist") {
            role = new String[]{"ROLE_RADIOLOGIST"};
        } else if (addRole.getValue() == "Doctor") {
            role = new String[]{"ROLE_DOCTOR"};
        } else if (addRole.getValue() == "Patient") {
            role = new String[]{"ROLE_PATIENT"};
        } else role = new String[]{"ROLE_USER"};
        User addeduser = new User(email, hashedPassword, role,name,lastname, local, gender, "x");

        try {
            ps.add(addeduser);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
