package controllers.Image;

import entities.Image;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.diagnostic.ImageService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.user.UserService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
public class EditImage {

    private Image image;

    @FXML
    private TextField bodypart;

    @FXML
    private TextField fileName;

    @FXML
    private ComboBox<User> patientCol;

    @FXML
    private Button submitButton;

    private Button uploadImageButton;
    private File selectedFile;


    private final UserService patientService = new UserService();
    private final ImageService imageService = new ImageService();
    private User selectedPatient;



    public void setImage(Image image) {
        this.image = image;
        bodypart.setText(image.getBodyPart());
        fileName.setText(image.getFilename());

        try {
            List<User> patients = patientService.showAllp();

            patientCol.getItems().setAll(patients);

            if (image.getPatient() != null) {
                patientCol.getSelectionModel().select(image.getPatient());
                selectedPatient=image.getPatient();
            } else if (!patients.isEmpty()) {
                patientCol.getSelectionModel().selectFirst();
                selectedPatient = patients.get(0);
                System.out.println("patient not empty");
            }

            patientCol.setOnAction(event -> selectedPatient = patientCol.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitButtonClick() {
        updateImagePatient();
    }

    @FXML
    private void handleEditButtonClick() throws SQLException, IOException {
        System.out.println("edited function dhfbuiezfi ez");
        updateImagePatient();


        if (selectedFile != null) {

            String destFilePath = "src/main/java/dicom/" + image.getId()+".dcm";
            File destFile = new File(destFilePath);
            System.out.println(destFile);

            Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


        }




        Image editedImage = new Image();
        editedImage.setId(image.getId());
        editedImage.setFilename(fileName.getText());
        editedImage.setBodyPart(bodypart.getText());
        editedImage.setPatient(selectedPatient);

        System.out.println("edit function " + fileName.getText() + " \n  bodypart"+bodypart.getText()+"\n select"+selectedPatient);



        if (fileName.getText().isEmpty() || bodypart.getText().isEmpty()|| selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields. edit ");
            alert.showAndWait();
            return;
        }

        imageService.editImage(editedImage);
        Stage stage = (Stage) fileName.getScene().getWindow();
        stage.close();

        stage.close();
    }

    private void updateImagePatient() {
        if (selectedPatient != null) {
            image.setPatient(selectedPatient);
            System.out.println("Updated Patient: " + selectedPatient); // Debug statement
        }
    }





    @FXML
    void uploadImage(MouseEvent event) {
        System.out.println("Upload image function is invoked.");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.dcm)", "*.dcm");
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(null); // Pass null instead of an event target
    }
}
