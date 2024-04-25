package controllers.Image;

import controllers.Image.ImageDashboard;
import entities.Interpretation;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReader;
import services.interpretation.InterpreationServices;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContainerController {

    private Image originalImage;
    private double scale = 1.0;
    private double mouseX;
    private double mouseY;
    private boolean zoom=false;
    private boolean span=false;
    @FXML
    private ImageView op;

    @FXML
    private StackPane fx;
    @FXML
    private FlowPane inter_con;
    private InterpreationServices interpreationServices = new InterpreationServices();

    @FXML
    void initialize() throws IOException {

        try {

            List<entities.Interpretation> images = interpreationServices.getInterpretationsWithDetails(ImageDashboard.selectedImage.getId());
            for (entities.Interpretation image : images) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/interpretationCard.fxml"));
                AnchorPane pane = loader.load();
                // Set bottom margin using CSS directly in the FXML file
                pane.setStyle("-fx-margin-bottom: 10px;");
                // Or set bottom margin programmatically
                AnchorPane.setBottomAnchor(pane, 10.0);
                // Get the controller of the cardImage.fxml file
                InterpretationController controller = loader.getController();
                // Pass the image data to the controller
                controller.setInterpretationData(image);
                // Add the pane to the flow pane
               this.inter_con.getChildren().addAll(pane);
            }






        // Load DICOM image
            File dicomFile = new File("src/main/java/dicom/" + ImageDashboard.selectedImage.getId() + ".dcm");
            ImageInputStream dicomStream = ImageIO.createImageInputStream(dicomFile);
            DicomImageReader dicomReader = new DicomImageReader(null);
            dicomReader.setInput(dicomStream);

            BufferedImage bufferedImage = dicomReader.read(0);
            originalImage = SwingFXUtils.toFXImage(bufferedImage, null);

            // Set the original image to the ImageView
            op.setImage(originalImage);
            fx.setPrefWidth(500); // Set your desired width
            fx.setPrefHeight(500); // Se
            // Add mouse event handlers for moving the image
            op.setOnMousePressed(this::handleMousePressed);
            op.setOnMouseDragged(this::handleMouseDragged);

            // Add scroll event handler for zooming
            op.setOnScroll(this::handleScroll);

            dicomStream.close();
        }

    catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMousePressed(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        if (span) {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            op.setTranslateX(op.getTranslateX() + deltaX);
            op.setTranslateY(op.getTranslateY() + deltaY);

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        }
    }
    private void handleScroll(ScrollEvent event) {
        // Zooming
        if (zoom) {
            double delta = event.getDeltaY();
            if (delta > 0) {
                // Zoom in
                scale *= 1.1;
            } else {
                // Zoom out
                scale /= 1.1;
            }

            op.setScaleX(scale);
            op.setScaleY(scale);
        }
    }
    @FXML
    void zoom(MouseEvent event) {
        zoom=true;
        span=false;

    }
    @FXML
    void span(MouseEvent event) {
        zoom=false;
        span=true;


    }





    @FXML
    void returnToIm(MouseEvent event) {
        try {
            // Load the imagedshbord.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/dashboard.fxml"));
            Parent root = loader.load();

            // Get the scene of the StackPane
            Scene scene = fx.getScene();

            // Set the root of the scene to the loaded root
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addInterpreataion(ActionEvent event) {
        try {
            // Load AddInterpretation.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/AddInterpretation.fxml"));
            Parent root = loader.load();

            // Create a new stage for AddInterpretation.fxml
            Stage addInterpretationStage = new Stage();
            addInterpretationStage.setScene(new Scene(root));
            addInterpretationStage.setTitle("Add Interpretation");

            // Show the Add Interpretation stage and wait for it to close
            addInterpretationStage.showAndWait();

            // After the Add Interpretation stage is closed, reload the container.fxml
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/fxml/image/container.fxml"));
            ContainerController controller = new ContainerController(); // Instantiate your controller
            loader1.setController(controller); // Set the controller for loader1
            root = loader1.load();

            // Get the current stage and scene
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Set the new scene to the current stage
            currentStage.setScene(scene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
