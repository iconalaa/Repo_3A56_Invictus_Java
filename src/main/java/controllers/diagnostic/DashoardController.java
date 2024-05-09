package controllers.diagnostic;


import controllers.user.ProfileController;
import controllers.user.SessionManager;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import services.diagnostic.PrescriptionService;
import services.diagnostic.ReportService;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;


public class DashoardController {
    @FXML
    public Label prescriptionList;
    @FXML
    private Label reportsListLabel; // Add fx:id attribute here
    @FXML
    private Label historylabel;
    @FXML
    private Label reportsdone;
    @FXML
    private Label prescriptionsLabel;

    @FXML
    private Label reportsLabel;
    @FXML
    private Label coranaLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private ImageView profileImg;
    @FXML
    private Label nameLabel;

    User loggedInUser = SessionManager.getLoggedInUser();


    @FXML
    private void initialize() {
        System.out.println(loggedInUser);
        nameLabel.setText(loggedInUser.getName() + " " + loggedInUser.getLastName());
        profileImg.setImage(new Image(new File("C:/Users/Mega-Pc/Desktop/Repo_3A56_Invictus_Symfony-main/public/uploads/pdp/" + loggedInUser.getBrochure_filename()).toURI().toString()));
        profileImg.setFitWidth(30);
        profileImg.setFitHeight(30);
        profileImg.setPreserveRatio(false);
        fetchAndDisplayCounts();
        updateCoronaLabel();
        updateCityLabel();
        reportsListLabel.setOnMouseClicked(event -> openReports(event));
        historylabel.setOnMouseClicked(event -> openHistory(event));
        prescriptionList.setOnMouseClicked(this::openPrescriptions);



    }
    private void updateCityLabel() {
        org.json.JSONObject locationInfo = MachineLocation.getMachineLocation();
        if (locationInfo != null) {
            try {
                String[] loc = locationInfo.getString("loc").split(",");
                double latitude = Double.parseDouble(loc[0]);
                double longitude = Double.parseDouble(loc[1]);
                String cityName = MachineLocation.getCityName(latitude, longitude);
                cityLabel.setText(cityName);
            } catch (JSONException e) {
                e.printStackTrace();
                cityLabel.setText("Unknown");
            }
        } else {
            cityLabel.setText("Unknown");
        }
    }
    private void openHistory(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/history.fxml"));
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
    private void openPrescriptions(javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/diagnostic/prescriptions-list.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(loader.load()));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void fetchAndDisplayCounts() {
        PrescriptionService prescriptionService = new PrescriptionService();
        ReportService reportService = new ReportService();

        try {
            // Get count of awaiting reports
            int awaitingReportsCount = reportService.getAwaitingReportsCount(loggedInUser.getUser_id());
            reportsLabel.setText(String.valueOf(awaitingReportsCount));

            // Get count of reports done
            int reportsDoneCount = reportService.getReportsDoneCount(loggedInUser.getUser_id());
            reportsdone.setText(String.valueOf(reportsDoneCount));

            // Get count of prescriptions
            int prescriptionsCount = prescriptionService.getAllPrescriptionsCount(loggedInUser.getUser_id());
            prescriptionsLabel.setText(String.valueOf(prescriptionsCount));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }
    private void updateCoronaLabel() {
        try {
            URL url = new URL("https://covid-193.p.rapidapi.com/statistics");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Key", "f1d05417e6msh5913861b8c12ac2p1fffdajsn05d61070e277");
            conn.setRequestProperty("X-RapidAPI-Host", "covid-193.p.rapidapi.com");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                 //Print the response for debugging
                //System.out.println("Response from API: " + response.toString());

                // Parse JSON response
                JSONObject jsonResponse = (JSONObject) JSONValue.parse(response.toString());
                JSONArray responseArray = (JSONArray) jsonResponse.get("response");
                if (responseArray != null) {
                    // Find Tunisia data
                    for (Object obj : responseArray) {
                        JSONObject country = (JSONObject) obj;
                        String countryName = (String) country.get("country");
                        if ("Tunisia".equalsIgnoreCase(countryName)) {
                            JSONObject cases = (JSONObject) country.get("cases");
                            if (cases != null) {
                                // Corrected line to avoid ClassCastException
                                long infected = (long) cases.get("total");
                                // Update the label on the JavaFX Application Thread
                                javafx.application.Platform.runLater(() -> coranaLabel.setText(String.valueOf(infected)));
                                System.out.println("Infected in Tunisia: " + infected); // Debugging
                                break;
                            }
                        }
                    }
                }
            } else {
                //System.out.println("HTTP request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
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