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


public class UpdateArticleController implements Initializable {
    @FXML
    public Rectangle selectImageButton;
    public Label nomErreur;
    public Label TypeError;
    public ImageView backbtn;
    public Label TypeError2;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentArea;

    private String imagePath;
    @FXML
    private TextField selectedImage;

    private Article article;
    @FXML
    public ImageView selectedImageview;

    private final ArticleService articleService = new ArticleService();
    public ListArticleController listArticleController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setArticle(Article article) {
        this.article = article;
        System.out.println("Blog object: " + article);

        initializeArticleFields();
    }

    public void setListArticleController(ListArticleController listArticleController) {
        this.listArticleController = listArticleController;
    }

    private void initializeArticleFields() {
        if (article != null) {
            titleField.setText(article.getTitle());
            contentArea.setText(article.getContent());
            if (article.getImage() != null && !article.getImage().isEmpty()) {
                File imageFile = new File(article.getImage());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    selectedImageview.setImage(image);
                    imagePath = article.getImage();
                }
            }
        }
    }

    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }



    @FXML
    public void update(MouseEvent mouseEvent) {
        String title = titleField.getText();
        String content = contentArea.getText();
        if (title.isEmpty() || content.isEmpty()) {
            showAlert("Missing Information", "Please fill in all fields.");
            return;
        }
        article.setTitle(titleField.getText());
        article.setContent(contentArea.getText());
        article.setImage(imagePath);
        article.setCreated_at(LocalDateTime.now());

        articleService.updateArticle(article);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Article Updated");
        alert.setHeaderText(null);
        alert.setContentText("The article has been successfully updated.");
        alert.showAndWait();
        closeWindow(mouseEvent);
        if (listArticleController != null) {
            listArticleController.refreshArticleList(); // Rafraîchir la liste des articles dans ListArticleController
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    public void selectImageButton(MouseEvent mouseEvent) {  FileChooser fileChooser = new FileChooser();
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
                Path targetPath = Path.of("C:\\Users\\Ala\\Desktop\\Repo_3A56_Invictus_Symfony\\public\\articles", fileName);
                Path copy = Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Afficher l'image dans l'ImageView selectedImageview
                Image image = new Image(selectedFile.toURI().toString());
                selectedImageview.setImage(image);
                imagePath = targetPath.toString(); // Mettre à jour le chemin de l'image
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void backbtn(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/ListArticle.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}

