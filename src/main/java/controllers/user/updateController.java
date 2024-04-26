package controllers.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Label emailError;


    @FXML
    private Label lastNameError;

    @FXML
    private Label matriculeError;

    @FXML
    private Label nameError;

    @FXML
    private Label passwordError;

    @FXML
    private Label roleError;

    @FXML
    private ComboBox<String> updateRoles;

    @FXML
    private TextField addName;

    @FXML
    private TextField addPassword;
    @FXML
    private ComboBox<String> addRole;
    @FXML
    private ComboBox<String> updateGender;
    @FXML
    private DatePicker updateDate;
    private final UserService ps = new UserService();
    private User user = new User();

    public void setModifyUser(User user) {
        this.user = user;
        fxName.setText(user.getName());
        fxLastName.setText(user.getLastName());
        fxEmail.setText(user.getEmail());
        fxPassword.setText(user.getPassword());

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
        String[] role;
        if (addRole.getValue().equals("Radiologist")) {
            role = new String[]{"ROLE_RADIOLOGIST"};
        } else if (addRole.getValue().equals("Doctor")) {
            role = new String[]{"ROLE_DOCTOR"};
        } else if (addRole.getValue().equals("Patient")) {
            role = new String[]{"ROLE_PATIENT"};
        } else {
            role = new String[]{"ROLE_USER"};
            System.out.println(addRole.getValue());
        }
        ;
        User addeduser = new User(email, hashedPassword, role, name, lastname, local, gender, "x");

        try {
            ps.add(addeduser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Created");
            alert.setHeaderText("Account has been created successfully");
            alert.show();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    void updateAction(ActionEvent event) {
        if (validateFields()) {
            String[] role;
            if (updateRoles.getValue().equals("Radiologist")) {
                role = new String[]{"ROLE_RADIOLOGIST"};
            } else if (updateRoles.getValue().equals("Doctor")) {
                role = new String[]{"ROLE_DOCTOR"};
            } else if (updateRoles.getValue().equals("Patient")) {
                role = new String[]{"ROLE_PATIENT"};
            } else {
                role = new String[]{"ROLE_USER"};
                System.out.println(updateRoles.getValue());
            }
            User u = new User(fxEmail.getText(), user.getPassword(), role, fxName.getText(), fxLastName.getText(), updateDate.getValue(), updateGender.getValue(), "x");
            try {
                ps.update(u, user.getUser_id());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Updated");
                alert.setHeaderText("This account has been updated successfully");
                alert.show();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else System.out.println("Invalid Inputs !");
    }

    @FXML
    public boolean validateFields() {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        String namePattern = "^[A-Za-z]{3,}(?:['-][A-Za-z]+)*$";
        boolean test = true;
        if (!fxName.getText().matches(namePattern)) {
            nameError.setText("Invalid Name !");
            test = false;
        }
        if (!fxLastName.getText().matches(namePattern)) {
            lastNameError.setText("Invalid Last Name !");
            test = false;
        }
        if (!fxEmail.getText().matches(emailPattern)) {
            emailError.setText("Invalid Email !");
            test = false;
        }
        if (!fxPassword.getText().matches(passwordPattern)) {
            passwordError.setText("Invalid Password Minimum 6 Characters !");
            test = false;
        }
//        if (gender.getValue() == null) {
//            genderError.setText("Chose a gender !");
//            test = false;
//        }
//        if (datePicker.getValue() == null) {
//            dateError.setText("Chose Date of birth please !");
//            test = false;
//        }

        return test;
    }
}
