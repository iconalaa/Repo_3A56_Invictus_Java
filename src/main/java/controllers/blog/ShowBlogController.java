package controllers.blog;
import controllers.ShowSceen;
import controllers.user.ProfileController;
import controllers.user.SessionManager;
import entities.Article;
import entities.Comment;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.blog.ArticleService;
import services.blog.CommentService;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ShowBlogController implements Initializable {
    User loggedInUser = SessionManager.getLoggedInUser();
    private static final String LIKE_FILE_PATH = "C:\\Users\\friaa\\OneDrive - ESPRIT\\Bureau\\java_v1\\src\\main\\resources\\likes.txt";

    private static final List<String> BAD_WORDS = Arrays.asList("fuck", "shut-up", "stupid", "monkey");
    public ScrollPane commentslist;
    public ImageView backbtn;
    Stage stage;
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
    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profileImg;
    private CommentService commentService;
    private Article article;
    private ArticleService articleService = new ArticleService();
    private boolean isLiked = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        commentService = new CommentService();
        nameLabel.setText(loggedInUser.getName() + " " + loggedInUser.getLastName());
        profileImg.setImage(new Image(new File("C:/Users/Mega-Pc/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + loggedInUser.getBrochure_filename()).toURI().toString()));
        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);
        profileImg.setPreserveRatio(false);
        if (article != null) {
            try {
                initArticleDetails(article);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        loadComments(); // Call loadComments() here to load comments when the controller is initialized
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void initArticleDetails(Article article) throws SQLException {
        this.article = article;

        titre.setText(article.getTitle());
        content.setText(article.getContent());

        if (article.getImage() != null && !article.getImage().isEmpty()) {
            Image image = new Image(article.getImage());
            imageView.setImage(image);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        String formattedDate = article.getCreated_at().format(formatter);
        createdAtLabel.setText(formattedDate);

        // Show current likes when the page loads
        updateLikesLabel();
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
                    }
                    commentTextArea.clear();
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
    private void likeButtonClicked(ActionEvent event) {
        try {
            if (article != null) {
                int likes = articleService.getArticleLikes(article.getId()); // Retrieve current likes from the database
                if (!isLiked) {
                    likes++; // Increment likes
                    recordLike(); // Record the like action in the text file
                    isLiked = true;
                } else {
                    likes = Math.max(0, likes - 1); // Decrement likes, ensuring it's non-negative
                    removeLike(); // Remove the like action from the text file
                    isLiked = false;
                }
                articleService.updateArticleLikes(article.getId(), likes); // Update likes in the database
                article.setLikes(likes); // Update likes in the article object
                updateLikesLabel(); // Update the likes label in the UI
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateLikesLabel() {
        try {
            int likes = articleService.getArticleLikes(article.getId());
            likesLabel.setText("Likes: " + likes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recordLike() {
        try {
            String userId = String.valueOf(loggedInUser.getUser_id());
            String articleId = String.valueOf(article.getId());
            String likeRecord = userId + "," + articleId;

            BufferedWriter writer = new BufferedWriter(new FileWriter(LIKE_FILE_PATH, true));
            writer.write(likeRecord);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeLike() {
        try {
            String userId = String.valueOf(loggedInUser.getUser_id());
            String articleId = String.valueOf(article.getId());
            String likeRecord = userId + "," + articleId;

            Path path = Paths.get(LIKE_FILE_PATH);
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                lines.remove(likeRecord);

                BufferedWriter writer = new BufferedWriter(new FileWriter(LIKE_FILE_PATH));
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComments() {
        if (article != null) {
            try {
                // Retrieve comments associated with the article
                List<Comment> comments = commentService.getCommentsByArticleId(article.getId());

                // Create a container for comments
                VBox commentContainer = new VBox();

                // Add each comment to the container
                for (Comment comment : comments) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/commentaireItem.fxml"));
                        Parent commentaireItem = loader.load();

                        // Get graphical elements from commentaireItem.fxml
                        Text username = (Text) commentaireItem.lookup("#username");
                        Text userComment = (Text) commentaireItem.lookup("#userComment");
                        Label createdAtLabelLocal = (Label) commentaireItem.lookup("#createdAtLabelLocal");

                        // Set values for each graphical element
                        username.setText(comment.getUser().getName());
                        userComment.setText(comment.getContent());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
                        createdAtLabelLocal.setText(comment.getCreated_at().format(formatter));

                        // Add the commentaireItem to the comment container
                        commentContainer.getChildren().add(commentaireItem);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Add the comment container to the ScrollPane
                commentslist.setContent(commentContainer);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void GoReports(MouseEvent mouseEvent) {
    }

    @FXML
    public void fxDonor(MouseEvent event) {
        ShowSceen s = new ShowSceen();
        s.open(event,"dons/newDonateur.fxml","Donor");
    }
    @FXML
    public void fxBlog(MouseEvent event) {
        return;
    } @FXML
    public void fxHome(MouseEvent event) {
        ShowSceen s = new ShowSceen();
        s.open(event,"home.fxml","Home");
    }
    @FXML
    void profileAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/profile.fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();
        controller.initialise(loggedInUser);
        stage = new Stage();
        stage.setTitle("Profile | RadioHub");
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
        stage.setResizable(false);
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

}
