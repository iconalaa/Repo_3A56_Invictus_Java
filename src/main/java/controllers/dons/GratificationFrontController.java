package controllers.dons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import Entities.gratification;
import Services.ServiceGratification;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import Services.PDFGeneration;

public class GratificationFrontController {

    @FXML
    private TextField titre ;

    @FXML
    private Label titreErreur ;

    @FXML
    private TextField desc ;

    @FXML
    private Label descErreur ;

    @FXML
    private TextField montant ;

    @FXML
    private Label montantErreur ;

    @FXML
    private TextField type ;

    @FXML
    private Label typeErreur ;

    /*public void addGrat(ActionEvent event) throws IOException {

        if (validateFields()) {
            int  mont = Integer.parseInt(montant.getText());
            gratification g = new gratification(mont, titre.getText(), desc.getText(), type.getText());
            ServiceGratification service = new ServiceGratification();
            try {
                service.ajouter(g);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations");
                alert.setHeaderText("Your donation has been added successfully'");
                alert.show();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid Inputs");
        }
    }

     */

    public void addGrat(ActionEvent event) throws IOException {
        if (validateFields()) {
            int mont = Integer.parseInt(montant.getText());
            gratification g = new gratification(mont, titre.getText(), desc.getText(), type.getText());
            ServiceGratification service = new ServiceGratification();
            try {
                service.ajouter(g);
                int payable = Integer.parseInt(String.valueOf(mont));


                // Set success URL to the generated PDF file
                String successUrl = "file://" + System.getProperty("user.home") + "/Desktop/" + g.getDonor().getPrenom_donateur() + "_Report.pdf";


                Stripe.apiKey = "sk_test_51OoAAEIWVdhcEaWiVsWuSH8Vvjp2uNLt6clHC56qJNPBSB9QhA4YLRaDFhvAhkzmuvj1vsTFD27b2Rr5Sho916F200mSsFg3iP";

                SessionCreateParams params = SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("https://www.canva.com/design/DAGEk1eYwlQ/P_t0ix4ORAjg06tojnbsAw/view?utm_content=DAGEk1eYwlQ&utm_campaign=designshare&utm_medium=link&utm_source=editor")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount((long) (payable * 100))
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Gratification")
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();
                Session session = Session.create(params);

                String checkoutUrl = session.getUrl();
                //System.out.println("Checkout URL: " + checkoutUrl);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaimentInterface.fxml"));
                Parent root = loader.load();
                PaiementInterfaceController controller = loader.getController();

                controller.loadCheckoutUrl(checkoutUrl);

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.showAndWait();


                PDFGeneration.generatePdfReport(g);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Congratulations");
                alert.setHeaderText("Your donation has been added successfully, a PDF file has been downloaded on your desktop as proof'");
                alert.show();

            } catch (SQLException | StripeException e) {
                System.out.println("Error: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to process donation");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            System.out.println("Invalid Inputs");
        }

    }


    public boolean validateFields() {
        String textPattern = "^[A-Za-z ]+$";
        String numberPattern = "\\d+";
        boolean test = true;
        if (!titre.getText().matches(textPattern)) {
            titreErreur.setText("Invalid title !");
            test = false;
        }

        if (!desc.getText().matches(textPattern)) {
            descErreur.setText("Description must be text !");
            test = false;
        }

        if (!montant.getText().matches(numberPattern)) {
            montantErreur.setText("The amount must be numeric !");
            test = false;
        }

        if (!type.getText().matches(textPattern)) {
            typeErreur.setText("Invalid Type !");
            test = false;
        }



        return test;
    }

}