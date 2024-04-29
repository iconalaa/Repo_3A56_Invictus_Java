package controllers.blog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import entities.Article;
import entities.Comment;
import services.blog.CommentService;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.System.err;

public class ShowBlogController implements Initializable {

    public TextArea commentTextArea;
    public ListView<Comment> commentsListView;
    public HBox likesContainer;
    public Label likesLabel;
    public TextArea textAreaContent;
    public TextArea textAreaTitle;

    @FXML
    private Label titleLabel;
    @FXML
    private Label errorLabel;

    @FXML
    private Label contentLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Label createdAtLabel;
    private CommentService commentService;
    private Article article;
    private int likesCount = 0;

    private static ListCell<Comment> call(ListView<Comment> param) {
        ListCell<Comment> listCell = new ListCell<Comment>() {
            @Override
            protected void updateItem(Comment comment, boolean empty) {
                super.updateItem(comment, empty);
                if (comment != null) {
                    // Formater la date du commentaire
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = comment.getCreated_at().format(formatter);
                    setText(comment.getContent() + " - " + formattedDate);
                } else setText(null);
            }
        };
        return listCell;
    }

    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        commentService = new CommentService();
//        // Créer une cell factory pour personnaliser l'affichage des commentaires dans la ListView
//        commentsListView.setCellFactory(ShowBlogController::call);
//    }
//
//    public void initArticleDetails(Article article) {
//        this.article = article;
//        // Afficher les détails de l'article
//        textAreaTitle.setText(article.getTitle());
//        textAreaContent.setText(article.getContent());
//
//        // Convertir la date en chaîne formatée
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = article.getCreated_at().format(formatter);
//        createdAtLabel.setText(formattedDate);
//
//        // Charger l'image de l'article
//        String imagePath = article.getImage();
//        if (imagePath != null && !imagePath.isEmpty()) {
//            File imageFile = new File(imagePath);
//            if (imageFile.exists()) {
//                Image image = new Image(imageFile.toURI().toString());
//                imageView.setImage(image);
//            } else {
//                err.println("L'image n'existe pas : " + imagePath);
//            }
//        } else {
//            err.println("Chemin d'image invalide ou non spécifié pour l'article.");
//        }
//        List<Comment> comments = commentService.readCommentsByArticleId(article.getId());
//        commentsListView.getItems().setAll(comments);
//    }
    public void initialize(URL url, ResourceBundle rb) {
        commentService = new CommentService();
        // Créer une cell factory pour personnaliser l'affichage des commentaires dans la ListView
        commentsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Comment comment, boolean empty) {
                super.updateItem(comment, empty);
                if (comment != null) {
                    // Récupérer les détails de l'utilisateur à partir de son ID
                    String username = getUserUsername(comment.getId_user_id());

                    // Formater la date du commentaire
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = comment.getCreated_at().format(formatter);

                    setText(comment.getContent() + " - " + username + " - " + formattedDate);
                } else {
                    setText(null);
                }
            }
        });
    }
    private String getUserUsername(int userId) {
        // Implémentez la logique pour obtenir le nom d'utilisateur à partir de l'ID de l'utilisateur
        // Cette méthode est un exemple, vous devrez l'implémenter selon vos besoins.
        return "Utilisateur" + userId;
    }

    public void initArticleDetails(Article article) {
        this.article = article;
        // Afficher les détails de l'article
        textAreaTitle.setText(article.getTitle());
        textAreaContent.setText(article.getContent());

        // Convertir la date en chaîne formatée
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = article.getCreated_at().format(formatter);
        createdAtLabel.setText(formattedDate);

        List<Comment> comments = commentService.readCommentsByArticleId(article.getId());
        commentsListView.getItems().setAll(comments);
    }
    @FXML
    private void addCommentButtonClicked(ActionEvent event) {
        String content = commentTextArea.getText().trim();
        LocalDateTime created_at = LocalDateTime.now();

        if (!content.isEmpty()) {
            if (article != null) {
                Comment comment = new Comment(article.getId(), content, created_at);
                commentService.addComment(comment);
                commentTextArea.clear();
                List<Comment> comments = commentService.readCommentsByArticleId(article.getId());
                commentsListView.getItems().setAll(comments);
            } else {
                err.println("L'article est null. Assurez-vous que la méthode initArticleDetails() est appelée pour initialiser l'article avant d'ajouter un commentaire.");
            }
        }
    }

    @FXML
    private void likeButtonClicked(ActionEvent event) {
        likesCount++;
        updateLikesCount();
    }

    private void updateLikesCount() {
        likesLabel.setText("Likes: " + likesCount);
    }
}
