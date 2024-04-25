package controllers.Image;

import entities.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.diagnostic.ImageService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ImageDashboard {

    @FXML
    private VBox container;
    @FXML
    private Button edit;
    @FXML
    private Button revoke;

    @FXML
    private Button share;

    @FXML
    private Button delete;

    @FXML
    public  BorderPane dash;
    @FXML
    private ListView<Image> listview;
    @FXML
    private Button addImageButton;
    @FXML
    private FlowPane flowpaneimages;
    public static  Image selectedImage;
    private boolean isSelected = false;
static  FlowPane x;
    private ImageService ps = new ImageService();

    @FXML
    void initialize() {
        try {
            selectedImage=null;
            edit.setVisible(true);
            revoke.setVisible(true);
            share.setVisible(true);
            delete.setVisible(true);
            addImageButton.setVisible(true);
            flowpaneimages.getChildren().clear();
                List<Image> images = ps.getImages();
                for (Image image : images) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/cardImage.fxml"));
                    AnchorPane pane = loader.load();
                    // Get the controller of the cardImage.fxml file
                    cardImageController controller = loader.getController();
                    // Pass the image data to the controller
                    controller.setImageData(image);
                    // Add the pane to the flow pane
                    flowpaneimages.getChildren().addAll(pane);
                    x=flowpaneimages;
                }
            ObservableList<Image> imageList = FXCollections.observableArrayList(images);
            listview.setItems(imageList);

            listview.setCellFactory(param -> new ListCell<Image>() {
                @Override
                protected void updateItem(Image image, boolean empty) {
                    super.updateItem(image, empty);
                    if (empty || image == null) {
                        setText(null);
                    } else {
                        setText(image.getFilename());
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void deleteRecord(MouseEvent event) throws SQLException, IOException {
        Image selectedImage = this.selectedImage;
        if(selectedImage==null)
        {


                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("please select an image");
                alert.showAndWait();
                return ;


        }
        if (selectedImage != null) {
            try {
                System.out.println("selected image is "+selectedImage);

                System.out.println("enter to selectedi 77");
                // Remove the selected item from the data source
                ps.deleteImageById(selectedImage.getId());
                // Remove the selected item from the ListView
                listview.getItems().remove(selectedImage);

                // Clear the children of flowpaneimages
                flowpaneimages.getChildren().clear();
                ps.deleteImageById(selectedImage.getId());
                // Refresh the content of flowpaneimages
                List<Image> images = ps.getImages();
                System.out.println(images.size());
                for (Image image : images) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/cardImage.fxml"));
                    AnchorPane pane = loader.load();

                    // Get the controller of the cardImage.fxml file
                    cardImageController controller = loader.getController();

                    // Pass the image data to the controller
                    controller.setImageData(image);

                    // Add the pane to the flow pane
                    flowpaneimages.getChildren().addAll(pane);
                    ImageDashboard.selectedImage=null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle error
            }
        }
    }

    @FXML
    void EditRecord(MouseEvent event) {
        Image selectedImage = this.selectedImage;
        if(selectedImage==null)
        {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("please select an image");
            alert.showAndWait();
            return ;


        }
        System.out.println("from image dashboard"+selectedImage.getPatient().getName());
        if (selectedImage != null) {
            try {
                // Load the edit scene
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/image/EditImage.fxml"));
                Parent root = fxmlLoader.load();
                EditImage editImageController = fxmlLoader.getController();
                // Pass the selected image to the edit image controller
                editImageController.setImage(selectedImage);
                // Display the popup dialog
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.showAndWait();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/dashboard.fxml"));
                root = loader.load();
                ImageDashboard imageDashboard = loader.getController();

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error
            }
        } else {
            // No item selected, display a message or handle it accordingly
        }
    }

    @FXML
    void handleAddButtonClick(ActionEvent event) {
if(selectedImage != null)
{
    System.out.println(selectedImage);


}

        try {
            // Load the FXML file for adding an image
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/image/addImage.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new scene with the root node
            Scene scene = new Scene(root);

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(scene);

            // Show the stage
            stage.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/dashboard.fxml"));
            root = loader.load();
            ImageDashboard imageDashboard = loader.getController();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
        }

    }


    @FXML
    void share(ActionEvent event) {
        try {
            if(selectedImage==null)
            {


                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("please select an image");
                alert.showAndWait();
                return ;


            }
            // Load the share.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/image/share.fxml"));
            Parent root = fxmlLoader.load();

            // Get the controller instance
            Share shareController = fxmlLoader.getController();
            Image selectedImage = listview.getSelectionModel().getSelectedItem();
             selectedImage = this.selectedImage;

            // Set the selected image
            shareController.setSelectedImage(selectedImage);

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Share");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void revokeAcess(ActionEvent event) throws SQLException {

        try {
            if(selectedImage==null)
            {


                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("please select an image");
                alert.showAndWait();
                return ;


            }
            // Load the share.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/image/revoke.fxml"));
            Parent root = fxmlLoader.load();

            revoke shareController = fxmlLoader.getController();
            Image selectedImage = listview.getSelectionModel().getSelectedItem();

            selectedImage = this.selectedImage;

            shareController.setSelectedImage(selectedImage);

            Stage stage = new Stage();
            stage.setTitle("Share");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void consult(ActionEvent event) {
        try {
            if(selectedImage==null)
            {


                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("please select an image");
                alert.showAndWait();
                return ;


            }
            // Load the consult.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/container.fxml"));

            // Create a new instance of the ContainerController
            ContainerController controller = new ContainerController();

            // Set the controller for the FXMLLoader
            loader.setController(controller);

            // Load the FXML file
            Parent root = loader.load();

            // Pass the selected image to the consult controller

            // Add the loaded root node to the container's children
            dash.getChildren().clear(); // Clear existing content
            dash.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }








    public void deleteImage(Image imageToDelete) {
        // Implement logic to delete the selected image
        // For example:
        flowpaneimages = new FlowPane();

        flowpaneimages.getChildren().removeIf(node -> {
            if (node instanceof AnchorPane) {
                cardImageController cardController = (cardImageController) ((AnchorPane) node).getProperties().get("controller");
                if (cardController != null && cardController.getImageData() == imageToDelete) {
                    return true; // Remove the node if it corresponds to the image to delete
                }
            }
            return false;
        });
    }




   public static  void ol()
   {


       try {
           // Get all the cards in the flow pane
           ObservableList<Node> cards = x.getChildren();


           // Iterate over each card and set its background color to gray
           for (Node card : cards) {
               if (card instanceof AnchorPane) {
                   AnchorPane cardPane = (AnchorPane) card;
                   cardPane.setStyle("-fx-background-color: white");
                   Node checkNode = cardPane.lookup("#check");
                   checkNode.setVisible(false);


               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }




    @FXML
    void gatShared(MouseEvent event) throws IOException, SQLException {


        flowpaneimages.getChildren().clear();


        List<Image> images = ps.getSharedImageById(8);
        for (Image image : images) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/image/cardImage.fxml"));
            AnchorPane pane = loader.load();

            // Get the controller of the cardImage.fxml file
            cardImageController controller = loader.getController();

            // Pass the image data to the controller
            controller.setImageData(image);

            // Add the pane to the flow pane
            flowpaneimages.getChildren().addAll(pane);
            x=flowpaneimages;
            edit.setVisible(false);
            revoke.setVisible(false);
            share.setVisible(false);
            delete.setVisible(false);
            addImageButton.setVisible(false);
        }


    }



    @FXML
    void myImages(MouseEvent event) {
        initialize();

    }




   }





