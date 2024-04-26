package controllers.blog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import entities.Article;
import services.blog.ArticleService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.lang.System.err;

public class ListArticleController {
    @FXML
    private ListView<Article> articleListView;
    @FXML
    private Label nbReactions;

    @FXML
    private Label nbComments;

    private final ArticleService articleService;
    public Button btnAddArticle;
    public Button btnBlog;

    private List<Article> articles;

    public ListArticleController() {
        articleService = new ArticleService();
    }

    @FXML
    private void initialize() {
        articles = articleService.readAllArticles();
        articleListView.getItems().addAll(articles);

        articleListView.setCellFactory(new Callback<ListView<Article>, ListCell<Article>>() {
            @Override
            public ListCell<Article> call(ListView<Article> listView) {
                return new ListCell<Article>() {
                    @Override
                    protected void updateItem(Article article, boolean empty) {
                        super.updateItem(article, empty);
                        if (empty || article == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Créez une ImageView pour afficher l'image dans la cellule
                            ImageView imageView = new ImageView();
                            imageView.setFitWidth(100); // Définissez la largeur souhaitée
                            imageView.setFitHeight(100); // Définissez la hauteur souhaitée

                            String imagePath = article.getImage();
                            if (imagePath != null && !imagePath.isEmpty()) {
                                File imageFile = new File(imagePath);
                                if (imageFile.exists()) {
                                    Image image = new Image(imageFile.toURI().toString());
                                    imageView.setImage(image); // Afficher l'image dans la cellule
                                } else {
                                    err.println("L'image n'existe pas : " + imagePath);
                                }
                            } else {
                                err.println("Chemin d'image invalide ou non spécifié pour l'article.");
                            }

                            // Créez un label pour afficher le texte
                            Label textLabel = new Label("Titre: " + article.getTitle() + "\nContent: " + article.getContent() +
                                    "\nCreated_at: " + article.getCreated_at());

                            // Créez un conteneur pour placer l'image et le texte côte à côte
                            HBox imageTextContainer = new HBox(10); // Espace de 10 pixels entre l'image et le texte
                            imageTextContainer.getChildren().addAll(imageView, textLabel);

                            HBox buttonsContainer = new HBox(10); // Espace de 10 pixels entre les boutons
                            ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/edit.png"))));
                            ImageView deleteIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/delete.png"))));

                            // Définissez les dimensions des icônes si nécessaire
                            editIcon.setFitWidth(16);
                            editIcon.setFitHeight(16);
                            deleteIcon.setFitWidth(16);
                            deleteIcon.setFitHeight(16);

                            // Créez les boutons avec les icônes
                            Button editButton = new Button();
                            editButton.setGraphic(editIcon);
                            Button deleteButton = new Button();
                            deleteButton.setGraphic(deleteIcon);

                            // Ajoutez les boutons au conteneur HBox
                            buttonsContainer.getChildren().addAll(editButton, deleteButton);

                            // Créez un conteneur global pour combiner les deux parties de la cellule
                            HBox cellContainer = new HBox(450); // Espace de 10 pixels entre les parties de la cellule
                            cellContainer.getChildren().addAll(imageTextContainer, buttonsContainer);

                            // Définissez le conteneur global comme contenu graphique de la cellule
                            setGraphic(cellContainer);

                            editButton.setOnAction(event -> {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/UpdateArticle.fxml"));
                                try {
                                    Parent root = loader.load();
                                    UpdateArticleController updateArticleController = loader.getController();
                                    updateArticleController.setArticle(article);
                                    updateArticleController.setListArticleController(ListArticleController.this); // Passer une référence à ListArticleController
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root));
                                    stage.showAndWait();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            deleteButton.setOnAction(event -> {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/DeleteArticle.fxml"));
                                    Parent root = loader.load();
                                    DeleteArticleController deleteArticleController = loader.getController();
                                    deleteArticleController.setArticleId(article.getId());
                                    Stage stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.setScene(new Scene(root));
                                    stage.showAndWait();
                                    if (deleteArticleController.isConfirmed()) {
                                        int articleIdToDelete = deleteArticleController.getArticleId();
                                        articleService.deleteArticle(articleIdToDelete);
                                        refreshArticleList();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            // Ajoutez les boutons au conteneur HBox

                        }
                    }
                };
            }
        });

        // Gestion de la sélection d'articles
        articleListView.setOnMouseClicked(event -> {
            Article selectedArticle = articleListView.getSelectionModel().getSelectedItem();
            if (selectedArticle != null) {
                showArticle(selectedArticle);
            }
        });
    }

    void refreshArticleList() {
        articleListView.getItems().clear();
        articles = articleService.readAllArticles();
        articleListView.getItems().addAll(articles);
    }

    private void showArticle(Article article) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/ShowArticle.fxml"));
        try {
            Parent root = loader.load();
            ShowArticleController showArticleController = loader.getController();
            showArticleController.initArticleDetails(article);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateToAddArticle(MouseEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/AddArticle.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = (Stage) btnAddArticle.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void navigateToBlog(MouseEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/Blog.fxml"));
        try {
            Parent root;
            root = loader.load();
            Stage stage = (Stage) btnBlog.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
