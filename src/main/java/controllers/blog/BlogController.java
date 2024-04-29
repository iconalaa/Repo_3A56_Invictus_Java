package controllers.blog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import entities.Article;
import services.blog.ArticleService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlogController {


    public HBox likeContainer;
    public ImageView imgReaction;
    public Label reactionName;
    public HBox reactionsContainer;
    public ImageView imgLike;
    public ImageView imgLove;
    public ImageView imgCare;
    public ImageView imgHaha;
    public ImageView imgWow;
    public ImageView imgSad;
    public ImageView imgAngry;
    public Label nbComments;
    public Label nbShares;
    @FXML
    private GridPane articleGridPane;

    private final ArticleService articleService;
    private long startTime = 0L;
    private Reactions currentReaction;
    private final Article article = new Article();
    @FXML
    private Label nbReactions;

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
            articleContainer.setStyle("-fx-background-color: " + frameColor + "; -fx-padding: 10px; -fx-border-color: " + borderColor + "; -fx-border-width: 3px;");

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
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(BlogController.class.getName()).log(Level.SEVERE, "Error loading ShowArticle.fxml", e);
        }
    }

    private String getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return String.format("#%02x%02x%02x", red, green, blue);
    }


    public void onReactionImgPressed(MouseEvent mouseEvent) {
        switch (((ImageView)mouseEvent.getSource()).getId()) {
            case "imgLove":
                this.setReaction(Reactions.LOVE);
                break;
            case "imgCare":
                this.setReaction(Reactions.CARE);
                break;
            case "imgHaha":
                this.setReaction(Reactions.HAHA);
                break;
            case "imgWow":
                this.setReaction(Reactions.WOW);
                break;
            case "imgSad":
                this.setReaction(Reactions.SAD);
                break;
            case "imgAngry":
                this.setReaction(Reactions.ANGRY);
                break;
            default:
                this.setReaction(Reactions.LIKE);
        }

        this.reactionsContainer.setVisible(false);
    }

    public void onLikeContainerPressed(MouseEvent mouseEvent) {
        this.startTime = System.currentTimeMillis();
    }

    public void onLikeContainerMouseReleased(MouseEvent mouseEvent) {
        if (System.currentTimeMillis() - this.startTime > 500L) {
            this.reactionsContainer.setVisible(true);
        } else {
            if (this.reactionsContainer.isVisible()) {
                this.reactionsContainer.setVisible(false);
            }

            if (this.currentReaction == Reactions.NON) {
                this.setReaction(Reactions.LIKE);
            } else {
                this.setReaction(Reactions.NON);
            }
        }
    }
    public void setReaction(Reactions reaction) {
        Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream(reaction.getImgSrc())));
        this.imgReaction.setImage(image);
        this.reactionName.setText(reaction.getName());
        this.reactionName.setTextFill(Color.web(reaction.getColor()));
        if (this.currentReaction == Reactions.NON) {
            this.article.setTotalReactions(this.article.getTotalReactions() + 1);
        }

        this.currentReaction = reaction;
        if (this.currentReaction == Reactions.NON) {
            this.article.setTotalReactions(this.article.getTotalReactions() - 1);
        }

        this.nbReactions.setText(String.valueOf(this.article.getTotalReactions()));
    }

}
