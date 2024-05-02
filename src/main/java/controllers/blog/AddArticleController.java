//package controllers.blog;
//
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import entities.Article;
//import services.blog.ArticleService;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDateTime;
//import java.util.ResourceBundle;
//
//
//public class AddArticleController implements iInitializable  {
//
//
//
//    public ImageView selectedImageview;
//    @FXML
//    private TextArea contentArea;
//
//    @FXML
//    private TextField titleField;
//
//
//    private String imagePath; // Chemin de l'image sélectionnée
//
//
//    private final ArticleService articleService = new ArticleService();
//
//    @FXML
//    private void backToListArticle(MouseEvent event) {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/ListArticle.fxml"));
//        Parent root = null;
//        try {
//            root = loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(new Scene(root));
//    }
//
//
//
//
//    private void showAlert(String title, String content) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//
//    }
//
//
//    @FXML
//
//    public void addButtonClicked(MouseEvent mouseEvent) {
//
//        String title = titleField.getText();
//        String content = contentArea.getText();
//        LocalDateTime created_at = LocalDateTime.now();
//
//        if (title.isEmpty() || content.isEmpty() || imagePath == null) {
//            showAlert("Missing Information", "Please fill in all fields and select an image.");
//            return;
//        }
//        Article newArticle = new Article(title, content, imagePath, created_at);
//        articleService.addArticle(newArticle);
//        titleField.clear();
//        contentArea.clear();
//        imagePath = null;
//
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Blog Added");
//        alert.setHeaderText(null);
//        alert.setContentText("The blog has been successfully added.");
//        alert.showAndWait();
//        backToListArticle(mouseEvent);
//    }
//    @FXML
//    public void selectImageButton(MouseEvent mouseEvent) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select Image");
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
//        );
//        Stage stage = (Stage) titleField.getScene().getWindow();
//        File selectedFile = fileChooser.showOpenDialog(stage);
//
//        if (selectedFile != null) {
//            try {
//                Path sourcePath = selectedFile.toPath();
//                String fileName = selectedFile.getName();
//                Path targetPath = Path.of("C:\\Users\\friaa\\OneDrive - ESPRIT\\Bureau\\java_v1\\src\\main\\resources\\img\\ArticleUpload", fileName);
//                Path copy = Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//
//                // Afficher l'image dans l'ImageView selectedImageview
//                Image image = new Image(selectedFile.toURI().toString());
//                selectedImageview.setImage(image);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//}
//
//
//
package controllers.blog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entities.Article;
import services.blog.ArticleService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddArticleController implements Initializable {

    public Rectangle selectImageButton;
    public Label nomErreur;
    public Label TypeError;
    public Label TypeError2;
    @FXML
    private ImageView selectedImageview;

    @FXML
    private TextArea contentArea;

    @FXML
    private TextField titleField;

    private String imagePath; // Chemin de l'image sélectionnée

    private final ArticleService articleService = new ArticleService();

    @FXML
    private void backToListArticle(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/ListArticle.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation des éléments si nécessaire
    }

    @FXML
    public void addButtonClicked(MouseEvent mouseEvent) {
        String title = titleField.getText();
        String content = contentArea.getText();
        LocalDateTime created_at = LocalDateTime.now();

        if (title.isEmpty() || content.isEmpty() || imagePath == null) {
            showAlert("Missing Information", "Please fill in all fields and select an image.");
            return;
        }
        Article newArticle = new Article(title, content, imagePath, created_at);
        articleService.addArticle(newArticle);
        titleField.clear();
        contentArea.clear();
        imagePath = null;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Blog Added");
        alert.setHeaderText(null);
        alert.setContentText("The blog has been successfully added.");
        alert.showAndWait();
        backToListArticle(mouseEvent);
    }

    @FXML
    public void selectImageButton(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) titleField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                Path sourcePath = selectedFile.toPath();
                String fileName = selectedFile.getName();
                Path targetPath = Path.of("C:\\Users\\friaa\\OneDrive - ESPRIT\\Bureau\\java_v1\\src\\main\\resources\\img\\ArticleUpload", fileName);
                Path copy = Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Afficher l'image dans l'ImageView selectedImageview
                Image image = new Image(selectedFile.toURI().toString());
                selectedImageview.setImage(image);

                // Définir le chemin de l'image
                imagePath = targetPath.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

