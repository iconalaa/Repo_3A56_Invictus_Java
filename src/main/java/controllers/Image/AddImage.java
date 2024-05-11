package controllers.Image;

import entities.Image;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import services.diagnostic.ImageService;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.user.UserService;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
public class AddImage {

    @FXML
    private DatePicker aqDate;

    @FXML
    private TextField bodyPart;

    @FXML
    private TextField fileName;

    @FXML
    private ComboBox<User> patientCol;
    private File selectedFile; // Store the selected file

    Stage stage;
    Scene scene;
    private final UserService patientService = new UserService(); // Service to retrieve patients
    private  final ImageService imageService = new ImageService();
    @FXML
    void initialize() {
        try {
            List<User> allPatients = patientService.showAllp();

            ObservableList<User> patients = FXCollections.observableArrayList();

            User associatedPatient = null;

            if (associatedPatient != null) {
                for (User patient : allPatients) {
                    if (!patient.equals(associatedPatient)) {
                        patients.add(patient);
                    }
                }
            } else {
                patients.addAll(allPatients);
            }

            patientCol.setItems(patients);


            if (!patients.isEmpty()) {
                patientCol.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addimage(MouseEvent event) throws IOException {
        try {
            User selectedPatient = patientCol.getValue();

            if (selectedPatient == null) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a patient.");
                alert.showAndWait();
                return;            }

            String imageName = fileName.getText();
            String bodyPartValue = bodyPart.getText();
            LocalDate acquisitionDate = aqDate.getValue();
            System.out.println(acquisitionDate);
            if(bodyPartValue.length() < 3)
            {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please insert at least 3 caracter in bodypart field");
                alert.showAndWait();
                return ;
            }
            if (imageName.isEmpty() || bodyPartValue.isEmpty() || acquisitionDate == null) {


                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the nessarary fields.");
                alert.showAndWait();



                return;
            }

            Image newImage = new Image();
            newImage.setFilename(imageName);
            newImage.setBodyPart(bodyPartValue);
            newImage.setAquisitionDate(acquisitionDate);
            newImage.setPatient(selectedPatient);

            imageService.addImage(newImage);

            System.out.println("Image added successfully.");




            if (selectedFile != null) {

                String destFilePath = "C:\\Users\\Ala\\Desktop\\Repo_3A56_Invictus_Symfony-main\\public\\uploads\\images\\" + newImage.getId() + ".dcm";

                File destFile = new File(destFilePath);
                System.out.println(destFile);

                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


                convertDicomToPng("C:\\Users\\Ala\\Desktop\\Repo_3A56_Invictus_Symfony-main\\public\\uploads\\images\\" + newImage.getId()+".dcm", "C:\\Users\\Ala\\Desktop\\Repo_3A56_Invictus_Symfony-main\\public\\uploads\\images\\" + newImage.getId());
                Stage stage = (Stage) fileName.getScene().getWindow();
                stage.close();

                stage.close();

            }
            else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a  dicom  image");
                alert.showAndWait();
                return;
            }





        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }











    public static void convertDicomToPng(String inputFilePath, String outputFilePath) {
        try {
            // Read DICOM file
            File inputFile = new File(inputFilePath);
            ImageInputStream iis = ImageIO.createImageInputStream(inputFile);

            // Get DICOM image reader
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            ImageReader reader = iter.next();
            reader.setInput(iis, false);

            // Read DICOM image
            BufferedImage image = reader.read(0, new DicomImageReadParam());

            // Close image input stream
            iis.close();

            // Write PNG file
            File outputFile = new File(outputFilePath);
            ImageIO.write(image, "png", outputFile);

            System.out.println("Conversion complete.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    void uploadImage() {
        System.out.println("Upload image function is invoked.");

        // Create a FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        // Set the file extension filters to allow only image files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.dcm)", "*.dcm");
        fileChooser.getExtensionFilters().add(extFilter);
        // Show the file chooser dialog
        selectedFile = fileChooser.showOpenDialog(null); // Pass null instead of an event target
    }










}