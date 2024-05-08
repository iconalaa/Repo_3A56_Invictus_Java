package controllers.diagnostic;

import controllers.user.ProfileController;
import controllers.user.SessionManager;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.diagnostic.ReportService;
import entities.Report;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ReportsController {

    @FXML
    private Label doctorSpaceLabel;
    @FXML
    private  Label prescriptionLabel;
    @FXML
    private Label historyLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView profileImg;
    @FXML
    private Label nameLabel;

    private ReportService reportService;
    User loggedInUser = SessionManager.getLoggedInUser();
    int id =loggedInUser.getUser_id();


    public ReportsController() {
        reportService = new ReportService();
    }
    public void initialize() {
        nameLabel.setText(loggedInUser.getName() + " " + loggedInUser.getLastName());
        profileImg.setImage(new Image(new File("C:/Users/Mega-Pc/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + loggedInUser.getBrochure_filename()).toURI().toString()));
        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);
        profileImg.setPreserveRatio(false);
        System.out.println(id);
        doctorSpaceLabel.setOnMouseClicked(this::openDashboard);
        historyLabel.setOnMouseClicked(this::openHistory);
        prescriptionLabel.setOnMouseClicked(this::openPrescriptions);


        try {

            List<Report> allReports = reportService.displayAll(id);
            List<Report> filteredReports = new ArrayList<>();

            // Filter reports where edited is false
            for (Report report : allReports) {
                if (!report.isIs_edited()) {
                    filteredReports.add(report);
                }
            }

            int column = 0;
            int row = 0;

            for (Report report : filteredReports) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/report-item.fxml"));

                try {
                    Node reportItem = loader.load();
                    gridPane.add(reportItem, column, row);

                    // Get the controller associated with the loaded report item
                    ReportsItemController reportItemController = loader.getController();

                    // Set the report object to the report item controller
                    reportItemController.setReport(report);
                    reportItemController.updateReportItem(report);


                    // Add event handler for report selection
                    reportItem.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                        reportItemController.handleReportSelection(event);
                    });

                    // Increment column and row values
                    column++;
                    if (column == 3) {
                        column = 0;
                        row++;
                    }

                    // Adjust grid width and height
                    gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxWidth(Region.USE_PREF_SIZE);

                    gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxHeight(Region.USE_PREF_SIZE);

                    GridPane.setMargin(reportItem, new Insets(10));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions properly
        }
    }
    private void openDashboard(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/dashboard.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the IOException properly, such as showing an error message to the user
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

    private void openHistory(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/history.fxml"));
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