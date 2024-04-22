package controllers.Image;

import entities.Interpretation;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReader;
import services.interpretation.InterpreationServices;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class consult {

    @FXML
    private VBox zone;
    private ImageView imageView;
    private Image originalImage;
    private double scale = 1.0;
    private double contrastFactor = 1.0;
    private double mouseX;
    @FXML
    private AnchorPane root;
    @FXML
    private ListView<String> container;
    private double mouseY;
    private boolean zoomActivated = false;
private  Image selectedImage;

    private List<Interpretation> interpretations; // Assuming you have a list of interpretations
private InterpreationServices interpreationServices= new InterpreationServices();
    public void setInterpretations(List<Interpretation> interpretations) {
        this.interpretations = interpretations;
    }


    @FXML
    public void initialize() throws SQLException {

        setInterpretations(interpreationServices.getInterpretationsWithDetails(13));


        container.getItems().clear(); // Clear existing items

        if (interpretations != null && !interpretations.isEmpty()) {
            for (Interpretation interpretation : interpretations) {
                String interpretationText = interpretation.getInterpretation();
                container.getItems().add(interpretationText);
            }
        }

        try {


            File dicomFile = new File("src/main/java/dicom/"+ImageDashboard.selectedImage.getId()+".dcm");
            ImageInputStream dicomStream = ImageIO.createImageInputStream(dicomFile);
            DicomImageReader dicomReader = new DicomImageReader(null);
            dicomReader.setInput(dicomStream);

            BufferedImage bufferedImage = dicomReader.read(0);

            originalImage = SwingFXUtils.toFXImage(bufferedImage, null);

            imageView = new ImageView(originalImage);
            imageView.setPreserveRatio(true);

            zone.getChildren().add(imageView);

            dicomStream.close();





        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addInterpreataion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/AddInterpretation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Interpretation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void refreshListView() throws SQLException {
        setInterpretations(interpreationServices.getInterpretationsWithDetails(13));



        container.getItems().clear(); // Clear existing items

        if (interpretations != null && !interpretations.isEmpty()) {
            for (Interpretation interpretation : interpretations) {
                String interpretationText = interpretation.getInterpretation();
                container.getItems().add(interpretationText);
            }
        }

    }
    @FXML
    void returnToIm(MouseEvent event) {
        try {
            // Load the imagedahboardfxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/dashboard.fxml"));
            Parent root = loader.load();

            // Set the loaded FXML root as a child of the AnchorPane
            this.root.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
}
