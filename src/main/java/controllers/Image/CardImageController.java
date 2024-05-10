package controllers.Image;

import entities.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReader;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardImageController {
    // Define a method to receive the image data
    @FXML
    private Label titleLabel; // Assuming you have a label for the title
    @FXML
    private Label patient;
    private Image imageData;
    @FXML
    private AnchorPane cardPane;
    @FXML
    private Label pateintlabel;
    private   boolean nb=false;
    private ImageDashboard dashboardController;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView check;
    @FXML
    private VBox con;
    @FXML
    private Text bodypart;
    private javafx.scene.image.Image originalImage;
    @FXML
    void initialize() {
        check.setVisible(false);

        cardPane.getStyleClass().add("hover-grow");
        ImageView previewImage = (ImageView) cardPane.lookup("#imageView");

        // Apply styles to the image node
        con.setStyle("-fx-background-radius: 20px 20px 0 0; -fx-background-insets: 0; -fx-border-color: black; -fx-border-width: 0px;");

        cardPane.setOnMouseEntered(event -> {
         pateintlabel.setStyle("-fx-text-fill: green;");

            cardPane.setStyle("-fx-background-color: black; -fx-border-color: black;");
            cardPane.getChildren().forEach(node -> {
                if (node instanceof Text) {
                    ((Text) node).setFill(Color.GREEN);
                }
            });
        });

        cardPane.setOnMouseExited(event -> {
            cardPane.setStyle("-fx-background-color: white; -fx-border-color: white;");
            pateintlabel.setStyle("-fx-text-fill: black;");
            bodypart.setStyle("-fx-text-fill: black;");

            cardPane.getChildren().forEach(node -> {
                if (node instanceof Text ) {
                    ((Text) node).setFill(Color.BLACK);

                }
            });
        });

    }



    public void setImageData(Image image) throws IOException {
        // Update the UI elements with the image data
        titleLabel.setText(image.getBodyPart());
        setImageDatax(image);
        patient.setText(image.getPatient().getName());

        File dicomFile = new File("C:\\Users\\Mega-Pc\\Desktop\\Repo_3A56_Invictus_Symfony-main\\public\\uploads\\images\\"+image.getId()+".dcm");
        ImageInputStream dicomStream = ImageIO.createImageInputStream(dicomFile);
        DicomImageReader dicomReader = new DicomImageReader(null);
        dicomReader.setInput(dicomStream);

        BufferedImage bufferedImage = dicomReader.read(0);

        originalImage = SwingFXUtils.toFXImage(bufferedImage, null);

            imageView.setPreserveRatio(true);
            imageView.setImage(originalImage);

        dicomStream.close();
    }

    public void setImageDatax(Image imageData) {
        this.imageData = imageData;
    }

    // Method to receive the reference to the AnchorPane from ImageDashboard

    public void setDashboardController(ImageDashboard dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    void onDeleteClicked(ActionEvent event) {
        System.out.println("selcted");
        // When the delete button is clicked, notify the dashboard controller to delete the image
        if (dashboardController != null && imageData != null) {
            dashboardController.deleteImage(imageData);
        }
    }

    public Image getImageData() {
        return imageData;
    }


    @FXML
    void cardselect(MouseEvent event) {
        setDashboardController(dashboardController);
        System.out.println(imageData.getId());
        ImageDashboard.selectedImage=imageData;
        ImageDashboard.ol();


        check.setVisible(true);
    }
}
