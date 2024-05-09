package controllers.Image;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;

public class Traduction {

    @FXML
    private TextArea result;

    @FXML
    private ComboBox<String> source;

    @FXML
    private ComboBox<String> target;

    @FXML
    private TextField text;

    @FXML
    void initialize() {
        // Initialize ComboBoxes with language options
        String[] languages = {"ar", "fr", "en"}; // Use an array for clarity
        source.getItems().addAll(Arrays.asList(languages));
        target.getItems().addAll(Arrays.asList(languages));

        // Select a default language for source (optional)
        source.getSelectionModel().selectFirst(); // Or choose a specific index
    }

    @FXML
    void translate(MouseEvent event) {
        // Implement your translation logic here
        // Based on source, target, and text input, perform translation

        // Example (replace with your actual translation logic)
        String translatedText = "Translation not implemented yet";
        result.setText(translatedText);
    }
}
