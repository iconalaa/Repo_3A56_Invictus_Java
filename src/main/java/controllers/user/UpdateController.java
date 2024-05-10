package controllers.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.Doctor;
import entities.Patient;
import entities.Radiologist;
import entities.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.user.DoctorService;
import services.user.PatientService;
import services.user.RadiologistService;
import services.user.UserService;

import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateController {
    @FXML
    private TextField fxEmail;

    @FXML
    private TextField fxLastName;

    @FXML
    private TextField fxName;

    @FXML
    private PasswordField fxPassword;

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
    private Label nameError;

    @FXML
    private Label passwordError;

    @FXML
    private TextField addAssurance;

    @FXML
    private TextField addAssuranceNum;


    @FXML
    private TextField addMatricule;

    @FXML
    private TextField addMedical;

    @FXML
    private TextField addMedicalNum;

    @FXML
    private TextField addName;

    @FXML
    private PasswordField addPassword;

    @FXML
    private SplitMenuButton addRole;
    @FXML
    private MenuItem doctorRole;
    @FXML
    private MenuItem patientRole;
    @FXML
    private MenuItem radiologistRole;

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
        updateGender.setValue(user.getGender());
        addRole.setText(user.getRole()[0].substring(7, user.getRole()[0].length() - 2).toLowerCase());
        updateDate.setValue(user.getBirth_date());
    }


    public void initialize() {

        doctorRole.setOnAction(event -> {
            addRole.setText("Doctor");
            addMatricule.setVisible(true);
            addMedical.setVisible(false);
            addMedicalNum.setVisible(false);
            addAssurance.setVisible(false);
            addAssuranceNum.setVisible(false);
        });

        patientRole.setOnAction(event -> {
            addRole.setText("Patient");
            addMedical.setVisible(true);
            addMedicalNum.setVisible(true);
            addAssurance.setVisible(true);
            addAssuranceNum.setVisible(true);
            addMatricule.setVisible(false);
        });

        radiologistRole.setOnAction(event -> {
            addRole.setText("Radiologist");
            addMatricule.setVisible(true);
            addMedical.setVisible(false);
            addMedicalNum.setVisible(false);
            addAssurance.setVisible(false);
            addAssuranceNum.setVisible(false);
        });
    }

    @FXML
    void addAction(ActionEvent event) throws SQLException {

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

        if (addRole.getText().equals("Radiologist")) {
            role = new String[]{"ROLE_RADIOLOGIST"};
        } else if (addRole.getText().equals("Doctor")) {
            role = new String[]{"ROLE_DOCTOR"};
        } else if (addRole.getText().equals("Patient")) {
            role = new String[]{"ROLE_PATIENT"};
        } else {
            role = new String[]{"ROLE_USER"};
        };
        User addeduser = new User(email, hashedPassword, role, name, lastname, local, gender, "x");

        if (addRole.getText().equals("Radiologist")) {
            RadiologistService rad_Serv = new RadiologistService();
            Radiologist r = new Radiologist(addeduser,addMatricule.getText());
            rad_Serv.add(r);
            addAlert();
        } else if (addRole.getText().equals("Doctor")) {
            DoctorService doc_Serv = new DoctorService();
            Doctor d = new Doctor(addeduser,addMatricule.getText());
            doc_Serv.add(d);
            addAlert();
        } else if (addRole.getText().equals("Patient")) {
            PatientService patient_Serv = new PatientService();
            Patient p = new Patient(addeduser,addMedical.getText(),Integer.parseInt(addMedicalNum.getText()),Integer.parseInt(addAssuranceNum.getText()),addAssurance.getText());
            patient_Serv.add(p);
            addAlert();
        } else {
            ps.add(addeduser);
            addAlert();
        }
        ;



    }

    public void addAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Created");
        alert.setHeaderText("Account has been created successfully");
        alert.show();
    }
    @FXML
    void updateAction(ActionEvent event) {
        if (validateFields()) {
            String[] role;
            if (addRole.getText().equals("Radiologist")) {
                role = new String[]{"[\"ROLE_RADIOLOGIST\"]"};
            } else if (addRole.getText().equals("Doctor")) {
                role = new String[]{"[\"ROLE_DOCTOR\"]"};
            } else if (addRole.getText().equals("Patient")) {
                role = new String[]{"[\"ROLE_PATIENT\"]"};
            } else {
                role = new String[]{"[\"ROLE_USER\"]"};
                System.out.println(addRole.getText());
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
