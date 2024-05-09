package controllers.blog;

import entities.Article;
import entities.Comment;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.blog.ArticleService;
import services.blog.CommentService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ShowBlogController implements Initializable {
    private static final List<String> BAD_WORDS = Arrays.asList("fuck", "shut-up", "stupid", "monkey");
    public ScrollPane commentslist;
    public ImageView backbtn;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private HBox likesContainer;
    @FXML
    private Label likesLabel;
    @FXML
    private ImageView selectedImageView;
    @FXML
    private TextField TextFieldTitre;
    @FXML
    private TextField TextFieldContent;
    @FXML
    private Label titre;
    @FXML
    private Label content;
    @FXML
    private ImageView imageView;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView btnback;
    @FXML
    private HBox commenterHbox;
    @FXML
    private TextField inputTextComment;
    @FXML
    private ImageView commenterArt;
    @FXML
    private TextField username;
    @FXML
    private ImageView userimg;
    @FXML
    private Label createdAtLabel;
    @FXML
    private Label createdAt;
    private CommentService commentService;
    private Article article;
    private ArticleService articleService = new ArticleService();
    private boolean isLiked = false;
    public void setArticle(Article article) {
        this.article = article;
    }
    public void initArticleDetails(Article article) throws SQLException {
        this.article = article;
        System.out.println(articleService.getArticleLikes(article.getId()));

        titre.setText(article.getTitle());
        likesLabel.setText("Likes: " + article.getLikes());
        content.setText(article.getContent());

        // Load and display the image associated with the article
        if (article.getImage() != null && !article.getImage().isEmpty()) {
            Image image = new Image(article.getImage());
            imageView.setImage(image);
        }

        // Format and display the creation date of the article
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = article.getCreated_at().format(formatter);
        createdAtLabel.setText(formattedDate);
    }
    private void loadComments() {
        if (article != null) {
            try {
                // Récupérez les commentaires associés à l'article
                List<Comment> comments = commentService.getCommentsByArticleId(article.getId());
                System.out.println("Nombre de commentaires récupérés : " + comments.size());

                // Créez un conteneur pour les commentaires
                VBox commentContainer = new VBox();

                // Ajoutez chaque commentaire au conteneur
                for (Comment comment : comments) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/commentaireItem.fxml"));
                        Parent commentaireItem = loader.load();

                        // Récupérez les éléments graphiques du commentaireItem.fxml
                        Text username = (Text) commentaireItem.lookup("#username");
                        Text userComment = (Text) commentaireItem.lookup("#userComment");
                        Label createdAtLabelLocal = (Label) commentaireItem.lookup("#createdAtLabelLocal");


                        // Définissez les valeurs appropriées pour chaque élément graphique
                        username.setText(comment.getUser().getName());
                        userComment.setText(comment.getContent());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        createdAtLabelLocal.setText(comment.getCreated_at().format(formatter));

                        // Ajoutez le commentaireItem au conteneur de commentaires
                        commentContainer.getChildren().add(commentaireItem);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Ajoutez le conteneur de commentaires à votre ScrollPane
                commentslist.setContent(commentContainer);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize method called");
        commentService = new CommentService();
        // Assurez-vous que l'article est initialisé avant d'accéder à son ID
        if (article != null) {
            try {
                // Initialisez l'article
                initArticleDetails(article);

                // Chargez les commentaires associés à l'article
                loadComments();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        loadComments();

    }
    @FXML
    private void addCommentButtonClicked(ActionEvent event) throws SQLException {
        String content = commentTextArea.getText().trim();
        LocalDateTime created_at = LocalDateTime.now();
        if (!content.isEmpty()) {
            if (!containsBadWords(content)) {
                if (article != null) {
                    User user = new User();
                    Comment comment = new Comment(user, article, content, created_at);
                    try {
                        commentService.addComment(comment, user, article);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Handle the exception
                    }
                    commentTextArea.clear();
                    // Reload comments after adding a new comment
                    initialize(null, null);
                } else {
                    errorLabel.setText("The article is null. Make sure the initArticleDetails() method is called to initialize the article before adding a comment.");
                }
            } else {
                errorLabel.setText("Your comment contains inappropriate words.");
                commentTextArea.clear();
            }
        }
    }
    private boolean containsBadWords(String content) {
        for (String word : BAD_WORDS) {
            if (content.toLowerCase().contains(word)) {
                return true;
            }
        }
        return false;
    }
    @FXML
    public void backbtn(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/Blog.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    private void likeButtonClicked(ActionEvent event) {
        try {
            if (article != null) {
                int likes = articleService.getArticleLikes(article.getId()); // Retrieve current likes from the database
                if (!isLiked) {
                    likes++; // Increment likes
                    isLiked = true;
                } else {
                    likes = Math.max(0, likes - 1); // Decrement likes, ensuring it's non-negative
                    isLiked = false;
                }
                articleService.updateArticleLikes(article.getId(), likes); // Update likes in the database
                article.setLikes(likes); // Update likes in the article object
                updateLikesLabel(); // Update the likes label in the UI
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }


    private void updateLikesLabel() throws SQLException {
        likesLabel.setText("Likes: " + articleService.getArticleLikes(article.getId()));
    }
}
