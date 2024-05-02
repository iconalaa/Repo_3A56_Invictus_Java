package controllers.blog;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import java.io.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import entities.Article;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static java.lang.System.err;


public class ShowArticleController implements Initializable {

    public ImageView imageView;

    public Label content;
    public Label titre;
    private Article article;


    @FXML
    public ImageView selectedImageview;
    @FXML
    private Label createdAtLabel; // Ajout de l'annotation @FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Laissez ce code vide ou ajoutez toute logique d'initialisation si nécessaire
    }

    public void initArticleDetails(Article article) {
        this.article = article;

        // Afficher les détails de l'article
        titre.setText(article.getTitle());
        content.setText(article.getContent());

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

    public void setArticle(Article article) {
        this.article = article;
    }


    public void btnPDF(MouseEvent mouseEvent) {
        // Vérifier si l'article est initialisé
        if (article != null) {
            Document document = new Document();
            try {
                // Spécifier le chemin où le PDF sera enregistré
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Enregistrer le PDF");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
                File file = fileChooser.showSaveDialog(new Stage());

                if (file != null) {
                    // Écrire le contenu de l'article dans le document PDF
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();
                    document.add(new Paragraph("Titre: " + article.getTitle()));
                    document.add(new Paragraph("Contenu: " + article.getContent()));

                    // Ajouter l'image à partir du chemin spécifié dans l'article
                    if (article.getImage() != null && !article.getImage().isEmpty()) {
                        try {
                            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(article.getImage());
                            // Redimensionner l'image selon vos besoins
                            image.scaleToFit(400, 400);
                            document.add(image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    document.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Article Recharger");
                    alert.setHeaderText(null);
                    alert.setContentText("Article bien telechargé.");
                    alert.showAndWait();
                    backToListArticle(mouseEvent);
                } else {
                    System.out.println("L'enregistrement du PDF a été annulé.");
                }
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
                err.println("Erreur lors de la création du PDF.");
            }
        } else {
            err.println("L'article n'est pas initialisé.");
        }

    }
    @FXML
    private void backToListArticle(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/ListArticle.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}