package controllers.diagnostic;

import controllers.user.ProfileController;
import controllers.user.SessionManager;
import entities.Report;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import services.diagnostic.PdfReportGenerator;
import services.diagnostic.PrescriptionService;
import services.diagnostic.ReportService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;


public class HistoryController {


    @FXML
    private Label nameLabel;
    @FXML
    private ImageView profileImg;
    @FXML
    private Label reportsListLabel;
    @FXML
    private Label doctorsapcelabel;
    @FXML
    private ListView<Report> HistoryView;
    @FXML
    public TextField searchprompt;
    @FXML
    private Label prescriptionsLabel;



    User loggedInUser = SessionManager.getLoggedInUser();
    int id =loggedInUser.getUser_id();
    private ReportService reportService;
    private PrescriptionService prescriptionService;


    public HistoryController() {
        reportService = new ReportService();
        prescriptionService = new PrescriptionService();

    }
    @FXML
    private void initialize() {
        nameLabel.setText(loggedInUser.getName() + " " + loggedInUser.getLastName());
        profileImg.setImage(new Image(new File("C:/Users/Ala/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + loggedInUser.getBrochure_filename()).toURI().toString()));
        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);
        profileImg.setPreserveRatio(false);
        try {

            // Fetch reports from the database
            List<Report> reports = reportService.displayEditedReports(id);
            // Convert list to ObservableList
            ObservableList<Report> observableReports = FXCollections.observableArrayList(reports);

            // Set the custom cell factory for the ListView
            HistoryView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Report item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null); // Clear graphic if the cell is empty
                    } else {
                        // Create labels for each piece of information
                        Label doctorLabel = new Label("Patient: " + item.getImage().getPatient().getName() + " " + item.getImage().getPatient().getLastName());
                        Label dateLabel = new Label("Date: " + item.getDate());
                        Label medInterpretationLabel = new Label("Interpretation (Medical): " + item.getInterpretation_med());
                        Label radInterpretationLabel = new Label("Interpretation (Radiology): " + item.getInterpretation_rad());

                        // Customize label styles if needed
                        doctorLabel.setStyle("-fx-font-weight: bold;");
                        dateLabel.setStyle("-fx-font-weight: bold;");
                        medInterpretationLabel.setStyle("-fx-font-weight: bold;");
                        radInterpretationLabel.setStyle("-fx-font-weight: bold;");

                        // Create a VBox to hold the labels
                        VBox vbox = new VBox(doctorLabel, dateLabel, medInterpretationLabel, radInterpretationLabel);
                        vbox.setSpacing(10); // Adjust spacing between labels if needed

                        // Check if the report has a prescription
                        try {
                            boolean hasPrescription = prescriptionService.hasPrescription(item.getId());
                            if (hasPrescription) {
                                Label prescriptionLabel = new Label("Prescribed");
                                prescriptionLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                                prescriptionLabel.setTextFill(Color.WHITE);
                                prescriptionLabel.setTextAlignment(TextAlignment.CENTER);
                                prescriptionLabel.setBackground(new Background(new BackgroundFill(Color.web("#3F88C5"), new CornerRadii(15), null)));

                                // Create a StackPane to contain the prescription label
                                StackPane stackPane = new StackPane();
                                stackPane.getChildren().addAll(vbox, prescriptionLabel);
                                StackPane.setAlignment(prescriptionLabel, Pos.CENTER_RIGHT);

                                // Set the StackPane as the graphic of the ListCell
                                setGraphic(stackPane);
                            } else {
                                // Set the VBox as the graphic of the ListCell
                                setGraphic(vbox);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            // Set the items in the ListView
            HistoryView.setItems(observableReports);


        } catch (SQLException e) {
            e.printStackTrace(); // Handle the SQLException properly, such as showing an error message to the user
        }

        reportsListLabel.setOnMouseClicked(this::openReports);
        doctorsapcelabel.setOnMouseClicked(this::openSpace);
        prescriptionsLabel.setOnMouseClicked(this::openPrescriptions );
    }

    @FXML
    private void openPrescription(ActionEvent event) {
        Report selectedReport = HistoryView.getSelectionModel().getSelectedItem();
        if (selectedReport != null) {
            try {
                PrescriptionService prescriptionService = new PrescriptionService();
                boolean hasPrescription = prescriptionService.hasPrescription(selectedReport.getId());

                if (!hasPrescription) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/prescription.fxml"));
                    Parent prescriptionRoot = loader.load();

                    PrescriptionsController prescriptionsController = loader.getController();
                    prescriptionsController.setSelectedReportId(selectedReport.getId());

                    // Get the current scene and replace its root with the prescription form
                    Scene scene = HistoryView.getScene();
                    scene.setRoot(prescriptionRoot);
                } else {
                    // Show an alert indicating that a prescription already exists for this report
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("A prescription has already been created for this report.");
                    alert.showAndWait();
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void openPdfReport(ActionEvent actionEvent) {
        Report selectedReport = HistoryView.getSelectionModel().getSelectedItem();
        if (selectedReport != null) {
            // Create an instance of PdfReportGenerator
            PdfReportGenerator pdfReportGenerator = new PdfReportGenerator();

            // Call the generatePdfReport method on the instance
            pdfReportGenerator.generatePdfReport(selectedReport);

            // Show a pop-up alert after generating the PDF
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("PDF Report has been generated and saved to your desktop.");
            alert.showAndWait();
        }
    }

    private void openSpace(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/dashboard.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openReports(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/reports.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openPrescriptions(javafx.scene.input.MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/prescriptions-list.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the IOException properly, such as showing an error message to the user
        }
    }
    @FXML
    void profileAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/profile.fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();
        controller.initialise(loggedInUser);
        Stage stage = new Stage();
        stage.setTitle("Profile | RadioHub");
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
        stage.setResizable(false);
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }


}
