package controllers.Image;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.user.SessionManager;
import entities.Image;
import entities.Interpretation;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.diagnostic.ImageService;
import services.interpretation.InterpreationServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AddInterpretation {
    @FXML
    private TextField interpretationt;

    @FXML
    private TextArea result;

    @FXML
    private ComboBox<String> source;

    @FXML
    private ComboBox<String> target;

    @FXML
    private TextArea text;

    @FXML
    private ImageView translate;

    @FXML
    private TextField transresult;

    @FXML
    private TextField desc;



    int iduser= SessionManager.getLoggedInUser().getUser_id();
    User user= SessionManager.getLoggedInUser();


    private ContainerController consultController = new ContainerController();
    public void setConsultController(ContainerController consultController) {
        this.consultController = consultController;
    }
    @FXML

    void initialize()    {



        // Initialize ComboBoxes with language options
        String[] languages = {"ar", "fr", "en"}; // Use an array for clarity
        source.getItems().addAll(Arrays.asList(languages));
        target.getItems().addAll(Arrays.asList(languages));
        result.setVisible(false);

        // Select a default language for source (optional)
        source.getSelectionModel().selectFirst(); // Or choose a specific index


    }
    @FXML
    void addInterpretation(ActionEvent event) throws SQLException {




        if(interpretationt.getText().isEmpty() || desc.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;

        }


        InterpreationServices es = new InterpreationServices();
        ImageService imageService = new ImageService();

        Image image = ImageDashboard.selectedImage;

        Interpretation interpretation = new Interpretation();

        interpretation.setImage(image);

        interpretation.setRadiologist(user);

        interpretation.setInterpretation(interpretationt.getText());
        interpretation.setDescriptin(desc.getText());

        es.addInterpretation(interpretation);


            System.out.println("efezfezfez");



        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();




    }


    @FXML
    void translate(MouseEvent event) throws IOException {
        result.setVisible(true);
        System.out.println("hi enter into function traduction");
        String userText = text.getText(); // Get user input
        String sourceLang = source.getValue(); // Get selected source language
        String targetLang = target.getValue(); // Get selected target language

        // Validate input (optional)
        if (userText.isEmpty() || sourceLang == null || targetLang == null) {
            // Handle empty input or missing selections (e.g., display error message)
            result.setText("Please enter text and select both source and target languages.");
            return;
        }

        // Call your ContainerController's translate function
        String translatedText = ContainerController.translate(userText, targetLang, sourceLang);

        // Handle translation result
        if (translatedText != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Object> responseMap = mapper.readValue(translatedText, Map.class);
                Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                List<Map<String, String>> translations = (List<Map<String, String>>) data.get("translations");

                if (translations != null && !translations.isEmpty()) {
                    String x = translations.get(0).get("translatedText");
                    System.out.println(x);
                    result.setText(x);
                } else {
                    result.setText("Translation failed (no translations found).");
                }
            } catch (JsonProcessingException e) {
                result.setText("Error parsing translation response: " + e.getMessage());
            }
        } else {
            // Handle potential translation errors (e.g., network issues, API errors)
            result.setText("Translation failed. Please try again later.");
        }
    }
    }

