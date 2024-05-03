package controllers.diagnostic;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class SpeechToTextController {

    @FXML
    private TextArea outputTextArea;

    private LiveSpeechRecognizer recognizer;

    public SpeechToTextController() {
        try {
            Configuration configuration = new Configuration();
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle initialization error
        }
    }

    @FXML
    private void convertToText() {
        if (recognizer != null) {
            recognizer.startRecognition(true);
            try {
                while (true) {
                    var result = recognizer.getResult();
                    if (result == null) {
                        break;
                    }
                    String recognizedText = result.getHypothesis();
                    if (recognizedText != null && !recognizedText.isEmpty()) {
                        outputTextArea.appendText(recognizedText + " ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle recognition error
            } finally {
                recognizer.stopRecognition();
            }
        }
    }
}
