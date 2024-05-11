package services.RendezVous;

import controllers.user.SessionManager;
import entities.RendezVous;
import entities.Salle;
import entities.User;
import services.diagnostic.ReportService;
import services.user.UserService;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendezVouService {

    private int iduser=SessionManager.getLoggedInUser().getUser_id();

    private Connection connection;
    public ReportService reps=new ReportService();
    public UserService userService = new UserService();
    public RendezVouService(){
        connection = MyDataBase.getInstance().getConnection();
    }

    public void addRendezVous(RendezVous rendezVous) throws SQLException {
        // Define the SQL statement to insert rendez-vous data
        String sql = "INSERT INTO rendez_vous (salle_id, date_rv, type_exam, user_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set the values for each parameter
            st.setInt(1, rendezVous.getSalle().getId()); // Assuming getSalle() returns the Salle object
            st.setDate(2, java.sql.Date.valueOf(rendezVous.getDate_rv()));
            st.setString(3, rendezVous.getType_exam());
            st.setInt(4,iduser); // Assuming getUser_id() returns the User object

            System.out.println(sql);
            int rowsInserted = st.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    rendezVous.setId(id); // Set the ID to the RendezVous object
                    System.out.println("Rendez-Vous Added Successfully with ID: " + id);
                } else {
                    System.err.println("Failed to retrieve the rendez-vous ID");
                }
            } else {
                System.err.println("Failed to add rendez-vous");
            }
        } catch (SQLException e) {
            System.err.println("Failed to add rendez-vous: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }





    public RendezVous getRendezVousById(int id) throws SQLException {
        RendezVous rendezVous = null;

        // Define the SQL statement with inner join
        String sql = "SELECT r.*, s.* " +
                "FROM rendez_vous r " +
                "INNER JOIN salle s ON r.salle_id = s.id " +
                "WHERE r.id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            System.out.println(sql);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                rendezVous = new RendezVous();
                rendezVous.setId(rs.getInt("id"));

                Salle salle = new Salle();
                salle.setId(rs.getInt("s.id")); // Use alias for Salle table fields
                salle.setNum_salle(rs.getString("s.num_salle"));
                salle.setNum_dep(rs.getString("s.num_dep"));
                salle.setEtat_salle(rs.getString("s.etat_salle"));
                salle.setType_salle(rs.getString("s.type_salle"));
                rendezVous.setSalle(salle);

                rendezVous.setDate_rv(rs.getDate("date_rv").toLocalDate());
                rendezVous.setType_exam(rs.getString("type_exam"));


               int user= (rs.getInt("user_id")); // Assuming user_id field exists
                userService.getUserById(user);
                rendezVous.setUser_id(userService.getUserById(user));

            } else {
                System.err.println("No Rendez-Vous found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve rendez-vous by ID: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }

        return rendezVous;
    }








        public void deleteRendezVousById(int id) throws SQLException {
        // Define the SQL statement to delete by ID
        String sql = "DELETE FROM rendez_vous WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            System.out.println(sql);
            int rowsDeleted = st.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Rendez-Vous deleted successfully with ID: " + id);
            } else {
                System.err.println("No Rendez-Vous found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Failed to delete rendez-vous: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }




    public void updateRendezVous(RendezVous rendezVous) throws SQLException {
        // Define the SQL statement for update (excluding user_id)
        String sql = "UPDATE rendez_vous " +
                "SET salle_id = ?, " +
                "date_rv = ?, " +
                "type_exam = ? " +
                "WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, rendezVous.getSalle().getId()); // Assuming getSalle() returns the Salle object
            st.setDate(2, java.sql.Date.valueOf(rendezVous.getDate_rv()));
            st.setString(3, rendezVous.getType_exam());
            st.setInt(4, rendezVous.getId()); // Use the ID from the RendezVous object

            System.out.println(sql);
            int rowsUpdated = st.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Rendez-Vous updated successfully with ID: " + rendezVous.getId());
            } else {
                System.err.println("No Rendez-Vous found with ID: " + rendezVous.getId());
            }
        } catch (SQLException e) {
            System.err.println("Failed to update rendez-vous: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }




    public List<RendezVous> getRendezVousList() throws SQLException {
        List<RendezVous> rendezVousList = new ArrayList<>();

        // Define the SQL statement to select all rendezvous
        String sql = "SELECT * FROM rendez_vous r inner join  salle s on s.id=r.salle_id";

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            System.out.println(sql);

            while (rs.next()) {
                RendezVous rendezVous = new RendezVous();
                rendezVous.setId(rs.getInt("id"));

                // Assuming Salle and User have getters for each column
                Salle salle = new Salle();
                salle.setId(rs.getInt("salle_id"));
                salle.setNum_salle(rs.getString("num_salle"));
                // ... set other Salle fields based on the result set
                rendezVous.setSalle(salle);

                rendezVous.setDate_rv(rs.getDate("date_rv").toLocalDate());
                rendezVous.setType_exam(rs.getString("type_exam"));

                User user = new User();
                user.setUser_id(rs.getInt("user_id"));
                // ... set other User fields based on the result set (optional)
                rendezVous.setUser_id(user);

                rendezVousList.add(rendezVous);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve rendez-vous list: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }

        return rendezVousList;
    }






}
