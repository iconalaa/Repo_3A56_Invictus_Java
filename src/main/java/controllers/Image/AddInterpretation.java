package controllers.Image;

import controllers.user.SessionManager;
import entities.Image;
import entities.Interpretation;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.diagnostic.ImageService;
import services.interpretation.InterpreationServices;

import java.sql.SQLException;

public class AddInterpretation {

    @FXML
    private TextField desc;

    @FXML
    private TextField interpretationt;

    int iduser= SessionManager.getLoggedInUser().getUser_id();
    User user= SessionManager.getLoggedInUser();


    private ContainerController consultController = new ContainerController();
    public void setConsultController(ContainerController consultController) {
        this.consultController = consultController;
    }
    @FXML
    void addInterpretation(ActionEvent event) throws SQLException {

        if(interpretationt.getText().isEmpty() || desc.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;

        }


        InterpreationServices es = new InterpreationServices();
        ImageService imageService = new ImageService();

        Image image = ImageDashboard.selectedImage;

        Interpretation interpretation = new Interpretation();

        interpretation.setImage(image);

        interpretation.setRadiologist(user);

        interpretation.setInterpretation(interpretationt.getText());
        interpretation.setDescriptin(desc.getText());

        es.addInterpretation(interpretation);


            System.out.println("efezfezfez");



        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();




    }




}
