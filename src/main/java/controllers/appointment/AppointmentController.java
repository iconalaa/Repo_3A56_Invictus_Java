package controllers.appointment;

import com.twilio.rest.api.v2010.account.incomingphonenumber.Local;
import controllers.Image.ImageDashboard;
import controllers.user.ProfileController;
import controllers.user.SessionManager;
import entities.RendezVous;
import entities.Salle;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import services.RendezVous.RendezVouService;
import services.Salle.SalleServices;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class AppointmentController {
    SalleServices salleServices =new SalleServices();
    RendezVouService rendezVouService = new RendezVouService();
    User loggedInUser = SessionManager.getLoggedInUser();



    @FXML
    private DatePicker daterdv;

    @FXML
    private ComboBox<String> exam;



    @FXML
    private ComboBox<Salle> room;

    @FXML
    void fxBlog(MouseEvent event) {
        showScene(event, "blog/Blog.fxml", "Blog");}

    @FXML
    void fxDonor(MouseEvent event) {
        showScene(event, "dons/newDonateur.fxml", "Donor");
    }

    @FXML
    void fxHome(MouseEvent event) {
        showScene(event, "home.fxml", "Home");
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


    @FXML
    void submitrdv(MouseEvent event) throws SQLException {
        String examTaken = exam.getSelectionModel().getSelectedItem().toString();
        Salle selectedSalle = room.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = daterdv.getValue();

        System.out.println(examTaken);
        System.out.println(selectedSalle);
        System.out.println(selectedDate);

        RendezVous rendezVous = new RendezVous();
        rendezVous.setSalle(selectedSalle);
        rendezVous.setDate_rv(selectedDate);
        rendezVous.setType_exam(examTaken);
        rendezVous.setUser_id(loggedInUser);

        try {
            rendezVouService.addRendezVous(rendezVous);
            System.out.println("Appointment submitted successfully!");

            // Show success message using Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Submitted");
            alert.setHeaderText("Success!");
            alert.setContentText("Your appointment has been submitted successfully.");
            alert.showAndWait();


        } catch (SQLException e) {
            System.err.println("Error submitting appointment: " + e.getMessage());

            // Show error message using Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Submission Failed");
            alert.setContentText("An error occurred while submitting your appointment. Please try again later.");
            alert.showAndWait();


        }
    }


    @FXML
    public void initialize(){

        String[] examen = {"Echographie", "Scanner", "IRM"};


        exam.getItems().addAll(Arrays.asList(examen));

        exam.getSelectionModel().selectFirst(); // O


        try {
            List<Salle> allPatients = salleServices.getSallesList();

            ObservableList<Salle> doctors = FXCollections.observableArrayList();

            Salle associatedDoctor = null;

            if (associatedDoctor != null) {
                for (Salle patient : allPatients) {
                    if (!doctors.equals(associatedDoctor)) {
                        doctors.add(patient);
                    }
                }
            } else {
                doctors.addAll(allPatients);
            }

            room.setItems(doctors);


            if (!doctors.isEmpty()) {
                room.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void showScene(MouseEvent event, String path, String title) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
            stage.setTitle(title + " | RadioHub");
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
