package controllers.blog;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import services.blog.RSSService;

import java.io.IOException;
import java.util.List;

import static services.blog.RSSService.loadArticlesFromURL;

public class RSSController {
    @FXML
    private ListView<RSSService> articleListView;

    @FXML
    private TextField titreArtFront;

    @FXML
    private TextArea contenuArtFront;

    @FXML
    public void initialize() {
        // Autoriser la sélection simple d'un seul article dans la ListView
        articleListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        List<RSSService> articles = null;
        try {
            articles = loadArticlesFromURL("https://rss.app/feeds/v1.1/tyV0IvNTdIOilauf.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Afficher les articles dans la ListView
        if (articles != null && !articles.isEmpty()) {
            articleListView.getItems().addAll(articles);
        }

        // Ajouter un gestionnaire d'événements pour détecter la sélection d'un article
        articleListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (titreArtFront != null) {
                    titreArtFront.setText(newValue.getTitle());
                }
                contenuArtFront.setText(newValue.getContent());
            }
        });
    }
}
