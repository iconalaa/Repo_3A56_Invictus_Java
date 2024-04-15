package controllers.user;


import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import utils.MyDataBase;

public class LoginController {
    private Stage stage;
    private Scene scene;

    @FXML
    private Label emailError;

    @FXML
    private TextField emailField;

    @FXML
    private Label passwordError;

    @FXML
    private PasswordField passwordField;

    private Connection connection;


    @FXML
    public void login(ActionEvent event) throws IOException {
        if (validateFields()) {
            connection = MyDataBase.getInstance().getConnection();

            String req = "SELECT * FROM user WHERE email = ?";


            try {
                PreparedStatement ps = connection.prepareStatement(req);
                ps.setString(1, emailField.getText());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    BCrypt.Result result = BCrypt.verifyer().verify(passwordField.getText().toCharArray(), rs.getString("password"));
                    if (result.verified) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
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
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Invalid Cordentials !");
                        alert.show();
                    }
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        } else System.out.println("Invalid Inputs !");

    }

    public boolean validateFields() {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        boolean test = true;
        if (!emailField.getText().matches(emailPattern)) {
            emailError.setText("Invalid Email !");
            test = false;
        }
        if (!passwordField.getText().matches(passwordPattern)) {
            passwordError.setText("Invalid Password Minimum 6 Characters !");
            test = false;
        }
        return test;
    }

    @FXML
    public void signup(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/signUp.fxml"));
            stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


}