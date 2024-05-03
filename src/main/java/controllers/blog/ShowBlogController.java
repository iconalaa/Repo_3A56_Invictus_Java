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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    private static final List<String> BAD_WORDS = Arrays.asList("fuck", "shut-up", "stupid","monkey");

    public TextArea commentTextArea;
    public ListView<Comment> commentsListView;
    public HBox likesContainer;
    public Label likesLabel;
    @FXML
    public ImageView selectedImageview;

    public TextField TextFieldTitre;
    public TextField TextFieldContent;
    public Label titre;
    public Label content;
    public ImageView imageView;
    public Label errorLabel;
    public ImageView btnback;


    @FXML
    private Label createdAtLabel;
    private CommentService commentService;
    private Article article;
    private int likesCount = 0;
    private boolean liked = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        commentService = new CommentService();
        // Create a cell factory to customize the display of comments in the ListView
        commentsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Comment comment, boolean empty) {
                super.updateItem(comment, empty);
                if (comment != null) {
                    // Create a layout for the comment
                    VBox commentLayout = new VBox();
                    Label commentContent = new Label(comment.getContent());
                    Label commentUsername = new Label(comment.getUser().getName());
                    Label commentDate = new Label(comment.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                    // Add CSS styles if necessary
                    commentContent.getStyleClass().add("comment-content");
                    commentUsername.getStyleClass().add("comment-username");
                    commentDate.getStyleClass().add("comment-date");

                    // Add elements to the layout
                    commentLayout.getChildren().addAll(commentContent, commentUsername, commentDate);
                    setGraphic(commentLayout);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    public void initArticleDetails(Article article) throws SQLException {
        this.article = article;
        // Display article details
        titre.setText(article.getTitle());
        content.setText(article.getContent());
// Charger l'image de l'article
        if (article.getImage() != null && !article.getImage().isEmpty()) {
            File imageFile = new File(article.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);
            }
        }
        // Convert date to formatted string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = article.getCreated_at().format(formatter);
        createdAtLabel.setText(formattedDate);

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
            // Check if the comment contains inappropriate words
            if (!containsBadWords(content)) {
                if (article != null) {
                    // Create a new comment with the current user and article
                    User user = new User(); // Replace this with the actual user object
                    Comment comment = new Comment(user, article, content, created_at);

                    // Add the comment using the comment service
                    try {
                        commentService.addComment(comment, user, article);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle the exception
                    }

                    // Clear the comment text area
                    commentTextArea.clear();

                    // Update the comments list view
                    List<Comment> comments = commentService.getCommentsByArticleId(article.getId());
                    commentsListView.getItems().setAll(comments);
                } else {
                    err.println("The article is null. Make sure the initArticleDetails() method is called to initialize the article before adding a comment.");
                }
            } else {
                // Display an error message or take appropriate action
                errorLabel.setText("Your comment contains inappropriate words.");
            }
        }
    }

    @FXML
    private void likeButtonClicked(ActionEvent event) {
        if (liked) {
            likesCount--;
        } else {
            likesCount++;
        }
        liked = !liked;
        updateLikesCount();
    }

    private void updateLikesCount() {
        likesLabel.setText("Likes: " + likesCount);
    }

    public void btnback(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/Blog.fxml"));
        try {
            Parent root;
            root = loader.load();
            Stage stage = (Stage) btnback.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
