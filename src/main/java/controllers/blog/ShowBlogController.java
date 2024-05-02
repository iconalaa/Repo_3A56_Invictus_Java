package controllers.blog;

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
import entities.Article;
import entities.Comment;
import javafx.stage.Stage;
import services.blog.CommentService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
        // Créer une cell factory pour personnaliser l'affichage des commentaires dans la ListView
        commentsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Comment comment, boolean empty) {
                super.updateItem(comment, empty);
                if (comment != null) {
                    // Créer une mise en page pour le commentaire
                    VBox commentLayout = new VBox();
                    Label commentContent = new Label(comment.getContent());
                    Label commentUsername = new Label(getUserUsername(comment.getId_user_id()));
                    Label commentDate = new Label(comment.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                    // Ajouter des styles CSS si nécessaire
                    commentContent.getStyleClass().add("comment-content");
                    commentUsername.getStyleClass().add("comment-username");
                    commentDate.getStyleClass().add("comment-date");

                    // Ajouter les éléments à la mise en page
                    commentLayout.getChildren().addAll(commentContent, commentUsername, commentDate);
                    setGraphic(commentLayout);
                } else {
                    setGraphic(null);
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
        titre.setText(article.getTitle());
        content.setText(article.getContent());

        // Convertir la date en chaîne formatée
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = article.getCreated_at().format(formatter);
        createdAtLabel.setText(formattedDate);

        List<Comment> comments = commentService.readCommentsByArticleId(article.getId());
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
    private void addCommentButtonClicked(ActionEvent event) {
        String content = commentTextArea.getText().trim();
        LocalDateTime created_at = LocalDateTime.now();

        if (!content.isEmpty()) {
            // Vérifiez si le commentaire contient des mots inappropriés
            if (!containsBadWords(content)) {
                if (article != null) {
                    Comment comment = new Comment(article.getId(), content, created_at);
                    commentService.addComment(comment);
                    commentTextArea.clear();
                    List<Comment> comments = commentService.readCommentsByArticleId(article.getId());
                    commentsListView.getItems().setAll(comments);
                } else {
                    err.println("L'article est null. Assurez-vous que la méthode initArticleDetails() est appelée pour initialiser l'article avant d'ajouter un commentaire.");
                }
            } else {
                // Affichez un message d'erreur ou prenez une autre action appropriée
                errorLabel.setText("Votre commentaire contient des mots inappropriés.");
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

//package controllers.blog;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import entities.Article;
//import entities.Comment;
//import services.blog.CommentService;
//
//import java.io.File;
//import java.net.URL;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.ResourceBundle;
//
//import static java.lang.System.err;
//
//public class ShowBlogController implements Initializable {
//
//    public TextArea commentTextArea;
//    public ListView<Comment> commentsListView;
//    public HBox likesContainer;
//    public Label likesLabel;
//    @FXML
//    public ImageView selectedImageview;
//
//    public TextField TextFieldTitre;
//    public TextField TextFieldContent;
//    public Label titre;
//    public Label content;
//    public ImageView imageView;
//
//
//    @FXML
//    private Label createdAtLabel;
//    private CommentService commentService;
//    private Article article;
//    private int likesCount = 0;
//
//    private static ListCell<Comment> call(ListView<Comment> param) {
//        ListCell<Comment> listCell = new ListCell<Comment>() {
//            @Override
//            protected void updateItem(Comment comment, boolean empty) {
//                super.updateItem(comment, empty);
//                if (comment != null) {
//                    // Formater la date du commentaire
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                    String formattedDate = comment.getCreated_at().format(formatter);
//                    setText(comment.getContent() + " - " + formattedDate);
//                } else setText(null);
//            }
//        };
//        return listCell;
//    }
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        commentService = new CommentService();
//        // Créer une cell factory pour personnaliser l'affichage des commentaires dans la ListView
//        commentsListView.setCellFactory(param -> new ListCell<>() {
//            @Override
//            protected void updateItem(Comment comment, boolean empty) {
//                super.updateItem(comment, empty);
//                if (comment != null) {
//                    // Récupérer les détails de l'utilisateur à partir de son ID
//                    String username = getUserUsername(comment.getId_user_id());
//
//                    // Formater la date du commentaire
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                    String formattedDate = comment.getCreated_at().format(formatter);
//
//                    setText(comment.getContent() + " - " + username + " - " + formattedDate);
//                } else {
//                    setText(null);
//                }
//            }
//        });
//    }
//    private String getUserUsername(int userId) {
//        // Implémentez la logique pour obtenir le nom d'utilisateur à partir de l'ID de l'utilisateur
//        // Cette méthode est un exemple, vous devrez l'implémenter selon vos besoins.
//        return "Utilisateur" + userId;
//    }
//
//    public void initArticleDetails(Article article) {
//        this.article = article;
//        // Afficher les détails de l'article
//        titre.setText(article.getTitle());
//        content.setText(article.getContent());
//
//        // Convertir la date en chaîne formatée
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = article.getCreated_at().format(formatter);
//        createdAtLabel.setText(formattedDate);
//
//        List<Comment> comments = commentService.readCommentsByArticleId(article.getId());
//        commentsListView.getItems().setAll(comments);
//    }
//    @FXML
//    private void addCommentButtonClicked(ActionEvent event) {
//        String content = commentTextArea.getText().trim();
//        LocalDateTime created_at = LocalDateTime.now();
//
//        if (!content.isEmpty()) {
//            if (article != null) {
//                Comment comment = new Comment(article.getId(), content, created_at);
//                commentService.addComment(comment);
//                commentTextArea.clear();
//                List<Comment> comments = commentService.readCommentsByArticleId(article.getId());
//                commentsListView.getItems().setAll(comments);
//            } else {
//                err.println("L'article est null. Assurez-vous que la méthode initArticleDetails() est appelée pour initialiser l'article avant d'ajouter un commentaire.");
//            }
//        }
//    }
//
//    @FXML
//    private void likeButtonClicked(ActionEvent event) {
//        likesCount++;
//        updateLikesCount();
//    }
//
//    private void updateLikesCount() {
//        likesLabel.setText("Likes: " + likesCount);
//    }
//}
//
