package controllers.blog;

import entities.Article;
import entities.Comment;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.blog.ArticleService;
import services.blog.CommentService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.System.err;

public class ShowBlogController implements Initializable {
    private static final List<String> BAD_WORDS = Arrays.asList("fuck", "shut-up", "stupid", "monkey");

    @FXML
    private TextArea commentTextArea;
    @FXML
    private ListView<Comment> commentsListView;
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

    private CommentService commentService;
    private Article article;
    private ArticleService articleService = new ArticleService();
    private boolean isLiked = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        commentService = new CommentService();
        commentsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Comment comment, boolean empty) {
                super.updateItem(comment, empty);
                if (comment != null) {
                    HBox commentLayout = new HBox();
                    Label commentUsername = new Label(comment.getUser().getName());
                    Label commentContent = new Label(comment.getContent());
                    Label commentDate = new Label(comment.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                    commentUsername.getStyleClass().add("comment-username");
                    commentDate.getStyleClass().add("comment-date");

                    commentLayout.getChildren().addAll(commentUsername, commentContent, commentDate);
                    setGraphic(commentLayout);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    public void initArticleDetails(Article article) throws SQLException {
        this.article = article;
        System.out.println(articleService.getArticleLikes(article.getId()));

        titre.setText(article.getTitle());
        likesLabel.setText("Likes: " + article.getLikes()); // No need to convert to String
        System.out.println(article);
        content.setText(article.getContent());
        try {
            // Retrieve the likes for the article from the database
            isLiked = articleService.getArticleLikes(article.getId()) > 0;
            updateLikesLabel(); // Update likes UI components based on retrieved likes
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }

        // Load and display the image associated with the article
        if (article.getImage() != null && !article.getImage().isEmpty()) {
            File imageFile = new File(article.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);
            }
        }

        // Format and display the creation date of the article
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = article.getCreated_at().format(formatter);
        createdAtLabel.setText(formattedDate);

        // Load and display comments associated with the article
        List<Comment> comments = commentService.getCommentsByArticleId(article.getId());
        commentsListView.getItems().setAll(comments);
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
                    List<Comment> comments = commentService.getCommentsByArticleId(article.getId());
                    commentsListView.getItems().setAll(comments);
                } else {
                    err.println("The article is null. Make sure the initArticleDetails() method is called to initialize the article before adding a comment.");
                }
            } else {
                errorLabel.setText("Your comment contains inappropriate words.");
                commentTextArea.clear();
            }
        }
    }

    public void backbtn(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/Blog.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
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
