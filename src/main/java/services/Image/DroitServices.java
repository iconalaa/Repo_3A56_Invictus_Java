package services.Image;

import entities.Droit;

import entities.User;
import services.user.UserService;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DroitServices  implements  DroitCrud<Droit>{
    private Connection connection;

    public DroitServices()
    {connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(Droit droit) throws SQLException {
        String query = "INSERT INTO droit (radioloqist_id, image_id, role) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, droit.getRadiologist().getUser_id()); // Assuming Radiologist has an id field
            statement.setInt(2, droit.getImage().getId()); // Assuming Image has an id field
            statement.setString(3, droit.getRole());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Droit added successfully.");
            } else {
                System.out.println("Failed to add droit.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding droit: " + e.getMessage());
        }
    }


    @Override
        public void delete(int imageId) throws SQLException {
            String query = "DELETE FROM droit WHERE image_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, imageId);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Image with ID " + imageId + " deleted successfully.");
                } else {
                    System.out.println("No image found with ID " + imageId);
                }
            } catch (SQLException e) {
                System.err.println("Error deleting image: " + e.getMessage());
            }
        }


    @Override
    public ArrayList<Droit> showAll() throws SQLException {
        return null;
    }

    @Override
    public void deleteShraedRoleById(int id) throws SQLException
    {
        String query = "DELETE FROM droit WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("the Droit of guesr  with ID " + id + " deleted successfully.");
            } else {
                System.out.println("No Droit  found with ID " +id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting image: " + e.getMessage());
        }
    }


    @Override
    public void addRoleToImage(int idimage, List<Integer> ids) throws SQLException {
        String sql = "INSERT INTO droit (radioloqist_id, image_id, role) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int id : ids) {
                statement.setInt(1, id); // Assuming id is the radiologist_id
                statement.setInt(2, idimage); // Assuming image has an id field
                statement.setString(3, "guest"); // Set your desired role here
                statement.addBatch(); // Add the current statement to the batch
            }
            int[] result = statement.executeBatch(); // Execute all statements in the batch
            System.out.println("Inserted " + result.length + " droits successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding droits: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public ArrayList<User> getRadwithGeustRole(int imageId) throws SQLException {
        String sql = "SELECT * FROM user r, droit i WHERE r.id = i.radioloqist_id AND i.image_id = ? AND i.role = 'guest'";
        ArrayList<User> rads = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, imageId);
            UserService radiologistService = new UserService();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Process the result set
                    int radId = resultSet.getInt("r.id");
                    int userId = resultSet.getInt("r.user_id");
                    String name = resultSet.getString("r.name");

                    System.out.println(userId);
                    User u1 =radiologistService.findUserById(userId);
                    // Retrieve other radiologist attributes as needed
                    // For example:
                    // Construct a Radiologist object
                    User rad = new User();
                    rad.setUser_id(radId);
                    rad.setUser_id(u1.getUser_id());
                    rad.setName(name);

                    rads.add(rad);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving radiologists with guest role: " + e.getMessage());
            throw e;
        }
        System.out.println(rads);
        return rads;
    }

    @Override
    public ArrayList<User> getRadwithoutGuest(int imageId) throws SQLException {
        String sql = "SELECT r.* FROM user r LEFT JOIN droit d ON r.id = d.radioloqist_id AND d.image_id = ? WHERE d.image_id IS NULL OR d.role NOT IN ('guest', 'owner');\n";
        ArrayList<User> rads = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, imageId);
            UserService radiologistService = new UserService();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Process the result set

                    int userId = resultSet.getInt("r.id");
                    String name = resultSet.getString("r.name");

                    System.out.println(userId);
                    User u1 =radiologistService.findUserById(userId);
                    // Retrieve other radiologist attributes as needed
                    // For example:
                    // Construct a Radiologist object
                    User rad = new User();
                    rad.setUser_id(userId);rad.setName(name);
                    rads.add(rad);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving radiologists with guest role: " + e.getMessage());
            throw e;
        }

        return rads;
    }

    public ArrayList<Droit> getDroitWithImage(int imageId) throws SQLException {
        ArrayList<Droit> droits = new ArrayList<>();
        String sql = "SELECT droit.*, user.* FROM droit " +
                "INNER JOIN user ON droit.radioloqist_id = user.id " +
                "WHERE droit.image_id = ? AND droit.role = 'guest'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, imageId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Droit droit = new Droit();
                // Set properties of Droit object
                droit.setId(resultSet.getInt("id"));
                // Set properties of User object (assuming User is included in Droit)
                User user = new User();
                user.setUser_id(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                // Set other properties of User as needed
                droit.setRadiologist(user);
                // Add Droit object to the list
                droits.add(droit);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving droits: " + e.getMessage());
            throw e;
        }
        return droits;
    }




}
