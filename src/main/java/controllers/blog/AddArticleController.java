package controllers.blog;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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


public class AddArticleController implements iInitializable  {


    public DatePicker datepicker;
    @FXML
    private TextArea contentArea;

    @FXML
    private TextField titleField;
    @FXML
    private TextField selectedImage;
    private String imagePath; // Chemin de l'image sélectionnée


    private final ArticleService articleService = new ArticleService();

    @FXML
    private void backToListArticle(ActionEvent event) {
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


    @FXML
    void addButtonClicked(ActionEvent event) {

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
        backToListArticle(event);

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void selectImageButton(ActionEvent event) {
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
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = targetPath.toString();
                selectedImage.setText(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}



