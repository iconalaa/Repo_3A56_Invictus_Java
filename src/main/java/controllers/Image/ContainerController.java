package controllers.Image;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javafx.scene.web.*;
import javafx.scene.web.WebView;
import controllers.Image.ImageDashboard;
import entities.Interpretation;
import javafx.animation.PauseTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReader;
import org.json.JSONObject;
import services.diagnostic.ReportService;
import services.interpretation.InterpreationServices;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.net.URLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
public class ContainerController {
    @FXML
    private Button zoombutton;
    @FXML
    private AnchorPane playground;

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
    private double contrastFactor = 1.0;

    private InterpreationServices interpreationServices = new InterpreationServices();
    private ReportService reportService = new ReportService();

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



            //Image iconImage = new Image(getClass().getResourceAsStream("fxml/image/assets/OIP.jpg"));

            // Create an ImageView with the icon image
            //ImageView iconView = new ImageView(iconImage);

            // Set the size of the icon
            // iconView.setFitWidth(16); // Set your desired width
            //iconView.setFitHeight(16); // Set your desired height

            // Set the icon as graphic for the button
            //zoombutton.setGraphic(iconView);

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
        //if (playground.contains(event.getX(), event.getY())) {
        // }
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
        playground.setCursor(Cursor.CROSSHAIR);


    }

    @FXML
    void span(MouseEvent event) {
        zoom=false;
        span=true;
        playground.setCursor(Cursor.MOVE);



    }
    @FXML
    public void affect(MouseEvent event) throws IOException {
        try {
            // Check if a report exists for the selected image
            boolean hasReport = reportService.checkReportExists(ImageDashboard.selectedImage.getId());

            // If a report exists, display an alert
            if (hasReport) {
                // Display an alert indicating a report already exists for the selected image
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Report Already Exists");
                alert.setHeaderText(null);
                alert.setContentText("A report has already been done for this image.");
                alert.showAndWait();
            } else {
                // Load the FXML file for the new window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/link.fxml"));
                // Load the root node of the new scene
                Parent root = loader.load();

                // Create a new stage for the new window
                Stage newStage = new Stage();
                // Set the scene with the loaded root node
                newStage.setScene(new Scene(root));
                newStage.setTitle("Report Affectation");
                newStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));

                // Show the new window
                newStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    void addInterpreataion(MouseEvent event) {
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
    @FXML
    void rotate(MouseEvent event) {
        op.setRotate(op.getRotate() + 90);
        playground.setCursor(Cursor.CLOSED_HAND);

    }


    private void adjustContrast() {
        // Get the image's pixel reader
        javafx.scene.image.PixelReader pixelReader = originalImage.getPixelReader();

        // Create a new writable image with the same dimensions as the original image
        javafx.scene.image.WritableImage adjustedImage = new javafx.scene.image.WritableImage(
                (int) originalImage.getWidth(),
                (int) originalImage.getHeight()
        );

        // Get the image's pixel writer
        javafx.scene.image.PixelWriter pixelWriter = adjustedImage.getPixelWriter();

        // Iterate over each pixel in the image
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                // Get the color of the pixel at (x, y)
                javafx.scene.paint.Color color = pixelReader.getColor(x, y);

                // Adjust the contrast of the pixel color
                double red = adjustComponent(color.getRed(), contrastFactor);
                double green = adjustComponent(color.getGreen(), contrastFactor);
                double blue = adjustComponent(color.getBlue(), contrastFactor);

                // Write the adjusted color to the new image
                pixelWriter.setColor(x, y, javafx.scene.paint.Color.color(red, green, blue));
            }
        }

        // Update the imageView to display the adjusted image
        op.setImage(adjustedImage);
    }

    private double adjustComponent(double component, double contrastFactor) {
        // Adjust the component using the contrast factor
        double adjustedComponent = (component - 0.5) * contrastFactor + 0.5;

        // Ensure the adjusted component is within the valid range [0, 1]
        return Math.min(Math.max(adjustedComponent, 0), 1);
    }
    @FXML
    void addContrast(MouseEvent event) {
        contrastFactor += 0.1;
        adjustContrast();
    }
    @FXML
    private void reduceContrast(MouseEvent event ) {
        contrastFactor -= 0.1;
        adjustContrast();
    }
    /*private void changeCursorForContrastAction() {
        // Change cursor to a custom image or predefined cursor type
        Scene scene = decreaseContrastButton.getScene();
        scene.setCursor(Cursor.CROSSHAIR);

        // Revert cursor back to default after a short delay (for demonstration)
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> scene.setCursor(Cursor.DEFAULT));
        pause.play();
    }*/








    @FXML
    private void callBrain(MouseEvent event) {
        String imageUrl = "https://raw.githubusercontent.com/iconalaa/Repo_3A56_Invictus_Java/dev_merge/src/main/resources/fxml/image/assets/OIP.jpg";
        String text = "ffezfezfez";
        String apiUrl = "https://textoverimage.moesif.com/image?image_url=" + imageUrl + "&text=" + text;

        try {
            URL url = new URL(apiUrl);
            URLConnection connection = url.openConnection();
            connection.connect();

            // Open input stream to read data
            try (InputStream inputStream = connection.getInputStream()) {
                // Save the image to disk
                Path outputPath = Paths.get("image_wirthoi_text.jpg");
                Files.copy(inputStream, outputPath);
                System.out.println("Image saved successfully: " + outputPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error while downloading image: " + e.getMessage());
        }
    }

    private String getJavaScriptCode() {
        // JavaScript code to make the API call and create a popup window
        return "<script>console.log('hi');</script>";
    }










}
