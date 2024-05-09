package controllers.diagnostic;

import controllers.ShowSceen;
import controllers.user.ProfileController;
import controllers.user.SessionManager;
import entities.Report;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.diagnostic.ReportService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsAdminController {

    private final ReportService reportService = new ReportService();
    private Report report = new Report();

    @FXML
    private VBox mainVBox1;
    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profileImg;
    @FXML
    private TextField searchText;
    User user = SessionManager.getLoggedInUser();
    Stage stage;
    public void initialize() {
        nameLabel.setText(user.getName() + " " + user.getLastName());
        profileImg.setImage(new Image(new File("C:/Users/Mega-Pc/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + user.getBrochure_filename()).toURI().toString()));
        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);
        profileImg.setPreserveRatio(false);
        try {
            List<Report> allReports = reportService.displayAll();
            displayReports(allReports); // Display all reports initially
            searchText.textProperty().addListener((observable, oldValue, newValue) -> {
                String searchTerm = newValue.trim();
                if (!searchTerm.isEmpty()) {
                    List<Report> filteredReports = filterReports(allReports, searchTerm);
                    displayReports(filteredReports); // Display filtered reports
                } else {
                    displayReports(allReports); // Display all reports if search text is empty
                }
            });
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void displayReports(List<Report> reports) {
        mainVBox1.getChildren().clear(); // Clear existing report cards
        mainVBox1.setSpacing(10);
        int maxColumns = 1;
        for (int i = 0; i < reports.size(); i += maxColumns) {
            HBox rowHBox = new HBox();
            for (int j = i; j < Math.min(i + maxColumns, reports.size()); j++) {
                Report report = reports.get(j);
                StackPane reportCard = createReportCard(report);
                rowHBox.getChildren().add(reportCard);
            }
            mainVBox1.getChildren().add(rowHBox);
            mainVBox1.setSpacing(20);
        }
    }

    private StackPane createReportCard(Report report) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/admin/report-card.fxml"));
            StackPane stackPane = loader.load();

            Label nameLabel = (Label) stackPane.lookup("#nameLabel");
            Label doctorLabel = (Label) stackPane.lookup("#doctorLabel");
            Label dateLabel = (Label) stackPane.lookup("#dateLabel");
            Label statusLabel = (Label) stackPane.lookup("#statusLabel");
            Button deleteBtn = (Button) stackPane.lookup("#deleteBtn");

            // Set report information
            nameLabel.setText("Patient Name: " + report.getImage().getPatient().getName() + " " + report.getImage().getPatient().getLastName());
            doctorLabel.setText("Doctor: " + report.getDoctor().getName()); // Assuming you have a 'getDoctor()' method in Report class
            dateLabel.setText("Date: " + report.getDate()); // Assuming you have a 'getDate()' method in Report class

            // Set status label based on the value of 'is_edited'
            if (report.isIs_edited()) {
                statusLabel.setText("Status: Finished");
            } else {
                statusLabel.setText("Status: Unfinished");
            }

            // Handle view details button action
            Button viewBtn = (Button) stackPane.lookup("#viewBtn");
            viewBtn.setOnAction(e -> {
                try {
                    FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/admin/report-details.fxml"));
                    VBox root = detailsLoader.load();

                    // Get the controller and set the report details
                    ReportDetailsController controller = detailsLoader.getController();
                    controller.setReportAndId(report, report.getId(), this);

                    // Create a new stage and set the scene
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Report Details");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
                    stage.initModality(Modality.APPLICATION_MODAL); // Set modality to block input to other windows
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            // Handle delete button action
            deleteBtn.setOnAction(e -> {
                try {
                    reportService.delete(report.getId()); // Assuming 'getId()' returns the ID of the report
                    refreshDisplay();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Error deleting report: " + ex.getMessage());
                    alert.showAndWait();
                }
            });

            return stackPane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private List<Report> filterReports(List<Report> reports, String searchTerm) {
        String searchTermLowerCase = searchTerm.toLowerCase();

        return reports.stream()
                .filter(report -> report.getImage().getPatient().getName().toLowerCase().contains(searchTermLowerCase))
                .collect(Collectors.toList());
    }
    @FXML
    void FxViewDetails(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/admin/report-details.fxml"));
            VBox root = loader.load();

            // Get the controller and set the report details
            ReportDetailsController controller = loader.getController();
            controller.setReportAndId(report, report.getId(), this);

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Report Details");
            stage.initModality(Modality.APPLICATION_MODAL); // Set modality to block input to other windows
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void FxAdd(ActionEvent event) {
        try {
            // Create a new Excel workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Report Data");

            // Create headers
            String[] headers = {"Patient Name", "Doctor", "Date", "Interpretation (Medical)", "Interpretation (Radiology)"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Fetch report data
            List<Report> allReports = reportService.displayAll();

            // Write report data to Excel
            int rowNum = 1;
            for (Report report : allReports) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(report.getImage().getPatient().getName() + " " + report.getImage().getPatient().getLastName());
                row.createCell(1).setCellValue(report.getDoctor().getName() + " " +report.getDoctor().getLastName());
                row.createCell(2).setCellValue(report.getDate().toString()); // Convert date to string or format it as required
                row.createCell(3).setCellValue(report.getInterpretation_med());
                row.createCell(4).setCellValue(report.getInterpretation_rad());
            }

            // Write the workbook content to a file
            String desktopPath = System.getProperty("user.home") + "/Desktop/";
            String filePath = desktopPath + "ReportData.xlsx";
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                workbook.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Report data exported to Excel successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error exporting report data to Excel: " + e.getMessage());
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error fetching report data: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void fxNotification(MouseEvent event) {
        // Logic for notifications
    }

    void refreshDisplay() {
        try {
            List<Report> allReports = reportService.displayAll();
            displayReports(allReports);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error refreshing display: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void GoReports(MouseEvent event) {
        return;
    }

    @FXML
    void fxBlog(MouseEvent event) {

    }

    @FXML
    void fxDashboard(MouseEvent event) {
        ShowSceen s= new ShowSceen();
        s.open(event,"dashboardHome.fxml","Dashboard");
    }

    @FXML
    void fxDonor(MouseEvent event) {
        ShowSceen s = new ShowSceen();
        s.open(event,"dons/Donateurs.fxml","Dashboard");

    }
    @FXML
    void fxUser(MouseEvent event) {
       ShowSceen s = new ShowSceen();
       s.open(event,"dashboard.fxml","Dashboard");
    }

    @FXML
    void profileAction(MouseEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/profile.fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();
        controller.initialise(user);
        stage = new Stage();
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
