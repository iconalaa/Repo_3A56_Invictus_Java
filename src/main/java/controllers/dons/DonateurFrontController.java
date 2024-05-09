package controllers.dons;

import controllers.user.HomeController;
import controllers.user.SessionManager;
import entities.Donateur;
import entities.User;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import services.dons.ServiceDonateur;

import com.twilio.Twilio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class DonateurFrontController {


        @FXML
        private TextField nom;

        @FXML
        private Label nomErreur;

        @FXML
        private TextField prenom;

        @FXML
        private Label prenomError;

        @FXML
        private TextField email;

        @FXML
        private Label emailError;

        @FXML
        private TextField telephone;

        @FXML
        private Label telephoneError;

        @FXML
        private TextField Type;

        @FXML
        private Label TypeError;

        User user = SessionManager.getLoggedInUser();


        public void addDonor(ActionEvent event) throws IOException {

            if (validateFields()) {
                int  tel = Integer.parseInt(telephone.getText());
                String recipientNumber = "+216" + tel;
                Donateur d = new Donateur(tel, nom.getText(), prenom.getText(), email.getText(), Type.getText());
                ServiceDonateur service = new ServiceDonateur();
                try {
                    service.ajouter(d);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Congratulations");
                    alert.setHeaderText("You have been added as a donor");
                    alert.show();

                    final String ACCOUNT_SID = "ACe1c72f1ecdb0a7c816847cf580845632";
                    final String AUTH_TOKEN = "8a5027b615b248680d2556425a185df2";
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

                    String messageBody = "Thank you for becoming a donor!";
                    Message message = Message.creator(
                                    new PhoneNumber(recipientNumber), // replace with the recipient's phone number
                                    new PhoneNumber("+15089284103"), // replace with your Twilio phone number
                                    messageBody)
                            .create();

                    System.out.println("SMS sent successfully. SID: " + message.getSid());

                    navigateTonewgrat();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Invalid Inputs");
            }
        }

        public boolean validateFields() {
            String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            String namePattern = "^[A-Za-z]{3,}(?:['-][A-Za-z]+)*$";
            String numberPattern = "\\b\\d{8}\\b";
            boolean test = true;
            if (!nom.getText().matches(namePattern)) {
                nomErreur.setText("Invalid Name !");
                test = false;
            }

            if (!prenom.getText().matches(namePattern)) {
                prenomError.setText("Invalid FirstName !");
                test = false;
            }

            if (!email.getText().matches(emailPattern)) {
                emailError.setText("Invalid Email !");
                test = false;
            }

            if (!Type.getText().matches(namePattern)) {
                TypeError.setText("Invalid Type !");
                test = false;
            }

            if (!telephone.getText().matches(numberPattern)) {
                telephoneError.setText("Invalid Phone Number !");
                test = false;
            }


            return test;
        }

    private void navigateTonewgrat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dons/newGratification.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage(); // Create a new stage
            stage.setScene(new  Scene(root));
            stage.show(); // Show the new stage
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void backToHome(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
            stage.setTitle("RadioHub | Home");
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

