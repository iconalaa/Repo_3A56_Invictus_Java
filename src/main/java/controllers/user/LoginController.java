    package controllers.user;


    import at.favre.lib.crypto.bcrypt.BCrypt;
    import entities.User;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Label;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;
    import javafx.scene.image.Image;
    import javafx.stage.Stage;

    import java.io.IOException;
    import java.sql.*;

    import javafx.event.ActionEvent;
    import javafx.geometry.Rectangle2D;
    import javafx.stage.Screen;
    import services.user.UserService;
    import utils.MyDataBase;

    public class LoginController {
        private Stage stage;
        private Scene scene;

        @FXML
        private Label emailError;

        @FXML
        private TextField emailField;

        @FXML
        private Label passwordError;

        @FXML
        private PasswordField passwordField;

        private Connection connection;
        private final UserService service = new UserService();


        @FXML
        public void login(ActionEvent event) throws IOException {
            if (validateFields()) {
                connection = MyDataBase.getInstance().getConnection();
                String req = "SELECT * FROM user WHERE email = ?";

                try {
                    PreparedStatement ps = connection.prepareStatement(req);
                    ps.setString(1, emailField.getText());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        BCrypt.Result result = BCrypt.verifyer().verify(passwordField.getText().toCharArray(), rs.getString("password"));
                        if (result.verified) {
                            User u =service.getUserById(rs.getInt("id"));
                            // Inside your login method after successful authentication
                            User loggedInUser = service.getUserById(rs.getInt("id"));
                            SessionManager.setLoggedInUser(loggedInUser);

                            String[] userRoles = rs.getString("roles").split(",");
                            for (String role : userRoles) {
                                if (role.trim().replace("[", "").replace("]", "").equals("\"ROLE_ADMIN\"")) {
                                    showScene(event,"dashboard.fxml","Dashboard");
                                    return;
                                }
                                if (role.trim().replace("[", "").replace("]", "").equals("\"ROLE_DOCTOR\"")) {
                                    showScene(event, "diagnostic/reports.fxml", "Diagnostic");

                                    return;
                                }
                                if (role.trim().replace("[", "").replace("]", "").equals("\"ROLE_RADIOLOGIST\"")) {
//                                    showScene(event, "image/dashboard.fxml", "Diagnostic");
                                    return;
                                }
                            }

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
                                Parent root = loader.load();
                                HomeController controller = loader.getController();
                                controller.setHome(u);
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setResizable(false);
                                stage.setScene(scene);
                                stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
                                stage.setTitle("RadioHub");
                                stage.show();
                                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                currentStage.close();
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("Password Doesn't Match !");
                            alert.show();
                        }
                    } else {
                        emailError.setText("Email doesn't Exist in DB !");
                    }

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            } else System.out.println("Invalid Inputs !");

        }

        public void showScene(ActionEvent event, String x,String title) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + x));
            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setScene(scene);
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
                stage.setTitle(title+" | RadioHub");
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

        public boolean validateFields() {
            String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
            String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            boolean test = true;
            if (emailField.getText().length() == 0) {
                emailError.setText("Email can't be empty !");
                test = false;
            } else if (!emailField.getText().matches(emailPattern)) {
                emailError.setText("Invalid Email !");
                test = false;
            }
            if (passwordField.getText().length() == 0) {
                passwordError.setText("Write your password !");
                test = false;
            } else if (!passwordField.getText().matches(passwordPattern)) {
                passwordError.setText("Invalid Password Minimum 6 Characters !");
                test = false;
            }
            return test;
        }

        @FXML
        public void signup(ActionEvent event) throws IOException {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/user/signUp.fxml"));
                stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                scene = new Scene(root);
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        @FXML
        void redirect_passwordpage(ActionEvent event) throws IOException {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/forgetPassword.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
                stage.show();
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                stage.setTitle("Rest Password");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }


    }