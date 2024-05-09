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
    public ImageView backbtn;



    @FXML
    public ImageView selectedImageview;
    @FXML
    private Label createdAtLabel; // Ajout de l'annotation @FXML
    private Article article;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(article);

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
        // Set the article received from ListArticleController
        this.article = article;
        initArticleDetails(article); // Initialize article details
    }


    public void btnPDF(MouseEvent mouseEvent) {
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

                    // Ajouter le titre centré et en gras
                    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                    Paragraph title = new Paragraph(article.getTitle(), titleFont);
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);

                    document.add(new Paragraph("Contenu: " + article.getContent()));

                    // Ajouter l'image à partir du chemin spécifié dans l'article
                    if (article.getImage() != null && !article.getImage().isEmpty()) {
                        try {
                            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(article.getImage());
                            // Redimensionner l'image selon vos besoins
                            image.scaleToFit(400, 400);
                            // Encadrer l'image
                            image.setBorder(Rectangle.BOX);
                            image.setBorderWidth(1);
                            document.add(image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // Ajouter le logo en haut à droite
                    com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance("C:\\Users\\friaa\\OneDrive - ESPRIT\\Bureau\\java_v1\\src\\main\\resources\\img\\logo\\logo 2.png");
                    logo.scaleToFit(50, 50); // Changer les dimensions selon vos besoins
                    logo.setAbsolutePosition(document.getPageSize().getWidth() - 70, document.getPageSize().getHeight() - 70);
                    document.add(logo);


                    // Ajouter la date en bas à droite
                    Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
                    Paragraph date = new Paragraph("Date: " + createdAtLabel.getText(), dateFont);
                    date.setAlignment(Element.ALIGN_RIGHT);
                    date.setSpacingBefore(10);
                    document.add(date);

                    document.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Article Recharger");
                    alert.setHeaderText(null);
                    alert.setContentText("Article bien téléchargé.");
                    alert.showAndWait();
                    backToListArticle(mouseEvent);
                } else {
                    System.out.println("L'enregistrement du PDF a été annulé.");
                }
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
                err.println("Erreur lors de la création du PDF.");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    public void backbtn(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/blog/ListArticle.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}