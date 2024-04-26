package controllers.blog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
import java.util.ResourceBundle;

public class UpdateArticleController implements iInitializable{
    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentArea;
    @FXML
    private TextField selectedImage;
    private String imagePath;
    private Article article;
    private final ArticleService articleService = new ArticleService();
    public ListArticleController listArticleController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setArticle(Article article) {
        this.article=article;
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
            selectedImage.setText(article.getImage());
        }
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
                Path targetPath = Path.of("C:\\Users\\friaa\\OneDrive - ESPRIT\\Bureau\\Workshop-JDBC-JavaFX-master\\src\\main\\java\\img", fileName);
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = targetPath.toString();
                selectedImage.setText(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void update(MouseEvent event) throws IOException {
        article.setTitle(titleField.getText());
        article.setContent(contentArea.getText());
        if (!selectedImage.getText().isEmpty()) {
            String imagePath = "C:\\Users\\friaa\\OneDrive - ESPRIT\\Bureau\\java_v1\\src\\main\\resources\\img\\ArticleUpload" + selectedImage.getText();
        }
        article.setImage(imagePath);

        articleService.updateArticle(article);
        closeWindow(event);
        if (listArticleController != null) {
            listArticleController.refreshArticleList(); // Rafraîchir la liste des articles dans ListArticleController
        }
    }
    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }



    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }



}
