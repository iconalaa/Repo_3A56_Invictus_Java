package services.Salle;

import entities.Salle;
import services.diagnostic.ReportService;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleServices {

    private Connection connection;
    public ReportService reps=new ReportService();

    public SalleServices(){
        connection = MyDataBase.getInstance().getConnection();
    }













    public void addSalle(Salle salle) throws SQLException {
        // Define the SQL statement to insert salle data
        String sql = "INSERT INTO salle (num_salle, num_dep, etat_salle, type_salle) VALUES (?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, salle.getNum_salle());
            st.setString(2, salle.getNum_dep());
            st.setString(3, salle.getEtat_salle());
            st.setString(4, salle.getType_salle());

            System.out.println(sql);
            int rowsInserted = st.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    salle.setId(id); // Set the ID to the Salle object
                    System.out.println("Salle Added Successfully with ID: " + id);
                } else {
                    System.err.println("Failed to retrieve the salle ID");
                }
            } else {
                System.err.println("Failed to add salle");
            }
        } catch (SQLException e) {
            System.err.println("Failed to add salle: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }








    public void updateSalle(Salle salle) throws SQLException {
        // Define the SQL statement for update
        String sql = "UPDATE salle " +
                "SET num_salle = ?, " +
                "num_dep = ?, " +
                "etat_salle = ?, " +
                "type_salle = ? " +
                "WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, salle.getNum_salle());
            st.setString(2, salle.getNum_dep());
            st.setString(3, salle.getEtat_salle());
            st.setString(4, salle.getType_salle());
            st.setInt(5, salle.getId()); // Use the ID from the Salle object

            System.out.println(sql);
            int rowsUpdated = st.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Salle updated successfully with ID: " + salle.getId());
            } else {
                System.err.println("No Salle found with ID: " + salle.getId());
            }
        } catch (SQLException e) {
            System.err.println("Failed to update salle: "  + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }





    public void deleteSalleById(int id) throws SQLException {
        // 1. Delete associated rendezvous (optional, depending on your needs)
        String deleteRendezVousSql = "DELETE FROM rendez_vous WHERE salle_id = ?";
        try (PreparedStatement st1 = connection.prepareStatement(deleteRendezVousSql)) {
            st1.setInt(1, id);
            st1.executeUpdate(); // Execute the deletion without checking rows deleted
        } catch (SQLException e) {
            System.err.println("Failed to delete associated rendezvous: " + e.getMessage());
            // You can choose to rethrow the exception here or handle it differently
        }

        // 2. Delete the salle itself
        String sql = "DELETE FROM salle WHERE id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            System.out.println(sql);
            int rowsDeleted = st.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Salle deleted successfully with ID: " + id);
            } else {
                System.err.println("No Salle found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Failed to delete salle: " + e.getMessage());
            throw e; // Rethrow the exception to the caller
        }
    }




    public List<Salle> getSallesList() throws SQLException {
        List<Salle> sallesList = new ArrayList<>();

        // Define the SQL statement to select all salles
        String sql = "SELECT * FROM salle";

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            System.out.println(sql);

            while (rs.next()) {
                Salle salle = new Salle();
                salle.setId(rs.getInt("id"));
                salle.setNum_salle(rs.getString("num_salle"));
                salle.setNum_dep(rs.getString("num_dep"));
                salle.setEtat_salle(rs.getString("etat_salle"));
                salle.setType_salle(rs.getString("type_salle"));
                sallesList.add(salle);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve salles list: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }

        return sallesList;
    }









    public Salle getSalleById(int id) throws SQLException {
        Salle salle = null;

        // Define the SQL statement to select by ID
        String sql = "SELECT * FROM salle WHERE id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            System.out.println(sql);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                salle = new Salle();
                salle.setId(rs.getInt("id"));
                salle.setNum_salle(rs.getString("num_salle"));
                salle.setNum_dep(rs.getString("num_dep"));
                salle.setEtat_salle(rs.getString("etat_salle"));
                salle.setType_salle(rs.getString("type_salle"));
            } else {
                System.err.println("No Salle found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve salle by ID: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }

        return salle;
    }



}
