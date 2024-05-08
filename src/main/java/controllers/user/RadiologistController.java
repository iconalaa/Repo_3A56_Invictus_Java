package controllers.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.Radiologist;
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
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.user.RadiologistService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
    private Label matError;
    private File selectedImageFile;

    @FXML
    public void addRadiologist(ActionEvent event) throws IOException {
        if (validateFields()) {
            LocalDate date = datePicker.getValue();
            //            ******** cryPtage *********
            char[] bcryptChars = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToChar(13, password.getText().toCharArray());
            StringBuilder sb = new StringBuilder();
            for (char c : bcryptChars) {
                sb.append(c);
            }
            String hashedPassword = sb.toString();
            String imageName = selectedImageFile != null ? selectedImageFile.getName() : "x";

            Radiologist R = new Radiologist(email.getText(), hashedPassword, new String[]{"ROLE_WAITING_RADIOLOGIST"}, name.getText(), lastname.getText(), date, gender.getValue(), imageName, mat_cnom.getText());
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
        } else System.out.println("Invalid Inputs");
    }
    @FXML
    public void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Image");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        selectedImageFile = fileChooser.showOpenDialog(((Stage) ((Button) event.getSource()).getScene().getWindow()));
        if (selectedImageFile != null) {
            System.out.println("Selected Image: " + selectedImageFile.getName());
            File destination = new File("C:/Users/Mega-Pc/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + selectedImageFile.getName());
            try {
                copyFile(selectedImageFile, destination);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void copyFile(File source, File dest) throws IOException {
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(dest);
             FileChannel sourceChannel = fis.getChannel();
             FileChannel destChannel = fos.getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    @FXML
    public boolean validateFields() {
        /*String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
       // String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        String namePattern = "^[A-Za-z]{3,}(?:['-][A-Za-z]+)*$";
        String mat_cnomPattern = "^[A-Za-z0-9]{6,}$";
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
        if (!password.getText().isEmpty()) {
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
        if (!mat_cnom.getText().matches(mat_cnomPattern)) {
            matError.setText("Invalid Matricule !");
            test = false;
        }
        return test;*/
        return true;
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