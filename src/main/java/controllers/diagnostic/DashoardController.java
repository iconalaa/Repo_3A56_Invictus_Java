package controllers.diagnostic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class DashoardController {
    @FXML
    private Label reportsListLabel; // Add fx:id attribute here

    @FXML
     private Label historylabel;

    @FXML
    private void initialize() {

        reportsListLabel.setOnMouseClicked(event -> openReports(event));
        historylabel.setOnMouseClicked(event -> openHistory(event));
    }

    private void openHistory(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/history.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openReports(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/reports.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
