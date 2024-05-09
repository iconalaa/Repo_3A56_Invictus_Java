package controllers.blog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import entities.Article;
import services.blog.ArticleService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlogController {

    @FXML
    private GridPane articleGridPane;

    private final ArticleService articleService;

    public BlogController() {
        articleService = new ArticleService();
    }

    @FXML
    private void initialize() {
        List<Article> articles = articleService.readAllArticles();
        int row = 0;
        int column = 0;

        String frameColor = "#EAEAEA"; // Couleur du cadre
        String borderColor = "#CCCCCC"; // Couleur de la bordure
        articleGridPane.setHgap(10);
        articleGridPane.setVgap(10);

        for (Article article : articles) {
            // Créer une ImageView pour afficher l'image dans la carte
            ImageView imageView = new ImageView();
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            String imagePath = article.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    imageView.setImage(image);
                } else {
                    Logger.getLogger(BlogController.class.getName()).log(Level.SEVERE, "Image does not exist: " + imagePath);
                }
            } else {
                Logger.getLogger(BlogController.class.getName()).log(Level.WARNING, "Invalid or unspecified image path for the article.");
            }

            // Créer des labels pour le titre, le contenu et created_at
            Label titleLabel = new Label(article.getTitle());
            Label contentLabel = new Label(article.getContent());
            Label createdAtLabel = new Label("Created_at: " + article.getCreated_at());

            // Créer un VBox pour contenir les labels
            VBox textVBox = new VBox(10);
            textVBox.getChildren().addAll(titleLabel, contentLabel, createdAtLabel);

            // Créer un conteneur pour contenir l'image et le VBox
            VBox articleContainer = new VBox(10);
            articleContainer.getChildren().addAll(imageView, textVBox);
            articleContainer.setStyle("-fx-background-color: " + frameColor + "; -fx-border-color: " + borderColor + ";");

            // Ajouter une classe CSS au conteneur d'article
            articleContainer.getStyleClass().add("article-container");

            // Ajouter une marge autour du conteneur d'article
            Insets marginInsets = new Insets(10);
            GridPane.setMargin(articleContainer, marginInsets);

            // Ajouter une marge interne au conteneur d'article pour séparer son contenu de ses bords
            articleContainer.setPadding(new Insets(10));

            // Ajouter le conteneur d'article à la grille
            articleGridPane.add(articleContainer, column, row);
            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }

        // Gérer l'événement de clic de souris pour chaque article
        for (Node node : articleGridPane.getChildren()) {
            node.setOnMouseClicked(event -> {
                VBox articleContainer = (VBox) node;
                VBox textVBox = (VBox) articleContainer.getChildren().get(1);
                Label titleLabel = (Label) textVBox.getChildren().get(0);
                Article selectedArticle = getArticleByTitle(titleLabel.getText(), articles);
                if (selectedArticle != null) {
                    showArticle(selectedArticle);
                }
            });
        }
    }

    private Article getArticleByTitle(String title, List<Article> articles) {
        for (Article article : articles) {
            if (article.getTitle().equals(title)) {
                return article;
            }
        }
        return null;
    }

    private void showArticle(Article article) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/ShowBlog.fxml"));
        try {
            Parent root = loader.load();
            ShowBlogController showBlogController = loader.getController();
            showBlogController.initArticleDetails(article);

            // Get the current stage
            Stage currentStage = (Stage) articleGridPane.getScene().getWindow();

            // Set the scene with the new content
            currentStage.setScene(new Scene(root));

        } catch (IOException e) {
            Logger.getLogger(BlogController.class.getName()).log(Level.SEVERE, "Error loading ShowArticle.fxml", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void fxBlog(MouseEvent mouseEvent) {
    }

    public void GoReports(MouseEvent mouseEvent) {
    }

    public void fxDonor(MouseEvent mouseEvent) {
    }
}