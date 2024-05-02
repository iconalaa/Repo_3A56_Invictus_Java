package controllers.blog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
import java.util.Optional;

import static java.lang.System.err;

public class ListArticleController {
    public TextArea searchField;
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
    private final ObservableList<Article> displayedArticles; // Déclaration de la variable displayedArticles


    public ListArticleController() {
        articleService = new ArticleService();
        displayedArticles = FXCollections.observableArrayList(); // Initialisation de displayedArticles

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
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/UpdateArticle.fxml"));
                                    Parent root = loader.load();
                                    UpdateArticleController updateArticleController = loader.getController();
                                    updateArticleController.setArticle(article);
                                    updateArticleController.setListArticleController(ListArticleController.this);
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root));
                                    // Utilisez Modality.NONE si vous ne voulez pas bloquer l'interaction avec les autres fenêtres
                                    stage.initModality(Modality.WINDOW_MODAL);
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            deleteButton.setOnAction(event -> {
                                // Show confirmation dialog
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure you want to delete this article?");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    // User confirmed deletion
                                    int articleIdToDelete = article.getId();
                                    articleService.deleteArticle(articleIdToDelete);
                                    refreshArticleList();
                                }
                            });


                            // Ajoutez les boutons au conteneur HBox

                        }
                    }
                };
            }
        });

        // Gestion de la sélection d'articles
        articleListView.setOnMouseClicked(this::handle);
    }

    void refreshArticleList() {
        articleListView.getItems().clear();
        articles = articleService.readAllArticles();
        articleListView.getItems().addAll(articles);
    }

    private void showArticle(Article article) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/ShowArticle.fxml"));

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

    private void handle(MouseEvent event) {
        Article selectedArticle = articleListView.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) showArticle(selectedArticle);
    }

    public void searche(KeyEvent keyEvent) {
        // Récupérer le terme de recherche depuis le champ de recherche
        String searchTerm = searchField.getText().toLowerCase().trim();

        List<Article> filteredArticles = articles.stream()
                .filter(article -> article.getTitle().toLowerCase().contains(searchTerm) ||
                        article.getContent().toLowerCase().contains(searchTerm))
                .toList();

        // Mettre à jour la liste affichée dans le ListView
        displayedArticles.clear(); // Effacer les éléments existants
        displayedArticles.addAll(filteredArticles); // Mise à jour des éléments du ListView
    }


}
