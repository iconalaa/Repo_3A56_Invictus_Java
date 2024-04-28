package controllers.blog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import entities.Article;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static java.lang.System.err;

public class ShowArticleController implements Initializable {

    public ImageView imageView;
    @FXML
    private Label titleLabel;

    @FXML
    private Label contentLabel;


    @FXML
    public ImageView selectedImageview;
    @FXML
    private Label createdAtLabel; // Ajout de l'annotation @FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Laissez ce code vide ou ajoutez toute logique d'initialisation si nécessaire
    }

    public void initArticleDetails(Article article) {
        // Afficher les détails de l'article
        titleLabel.setText(article.getTitle());
        contentLabel.setText(article.getContent());

        // Convertir la date en chaîne formatée
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        createdAtLabel.setText(article.getCreated_at().format(formatter)); // Ici, l'erreur se produit

        // Charger l'image de l'article
        if (article.getImage() != null && !article.getImage().isEmpty()) {
            File imageFile = new File(article.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);
            }
        }
    }
}
