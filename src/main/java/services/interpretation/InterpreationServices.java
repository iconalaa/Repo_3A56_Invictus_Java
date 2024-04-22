package services.interpretation;

import entities.Image;
import entities.Interpretation;
import entities.User;
import services.interpretation.InterpretationService;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;

public class InterpreationServices  {
    private Connection connection;

    // Constructor to initialize the connection
    public InterpreationServices() {
        connection = MyDataBase.getInstance().getConnection();
    }


    public void addInterpretation(Interpretation interpretation) throws SQLException {
        String query = "INSERT INTO interpretation (image_id, radiologist_id, interpretation,sendat,description,urgency) VALUES (?, ?, ?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, interpretation.getImage().getId());
            statement.setInt(2, interpretation.getRadiologist().getUser_id());
            statement.setString(3, interpretation.getInterpretation());
            Date sendAtSql = Date.valueOf(interpretation.getSendAt());

            statement.setDate(4, sendAtSql);
            statement.setString(5, interpretation.getDescriptin());
            statement.setString(6, interpretation.getUrgency());


            statement.executeUpdate();
        }
    }

    public void deleteInterpretation(Interpretation interpretation) throws SQLException {
        String query = "DELETE FROM interpretation WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, interpretation.getId());
            statement.executeUpdate();
        }
    }

    public void EditInterpretation(Interpretation interpretation) throws SQLException {
        String query = "UPDATE interpretation SET interpretation = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, interpretation.getInterpretation());
            statement.setInt(2, interpretation.getId());
            statement.executeUpdate();
        }
    }

    public ArrayList<Interpretation> showInterpreationByImage(int id) throws SQLException {
        ArrayList<Interpretation> interpretations = new ArrayList<>();
        String query = "SELECT * FROM interpretation WHERE image_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Interpretation interpretation = new Interpretation();
                interpretation.setId(resultSet.getInt("id"));
                interpretations.add(interpretation);
            }
        }
        return interpretations;
    }

    public ArrayList<Interpretation> getInterpretationsWithDetails(int imageId) throws SQLException {
        ArrayList<Interpretation> interpretations = new ArrayList<>();
        String sql = "SELECT i.*, u.*, im.* FROM interpretation i " +
                "INNER JOIN user u ON i.radiologist_id = u.id " +
                "INNER JOIN image im ON i.image_id = im.id " +
                "WHERE im.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, imageId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Interpretation interpretation = new Interpretation();
                // Set interpretation details
                interpretation.setId(resultSet.getInt("i.id"));
                    interpretation.setInterpretation(resultSet.getString("i.interpretation"));
                System.out.println(interpretation.getInterpretation());

                // Set user details
                User radiologist = new User();
                radiologist.setUser_id(resultSet.getInt("u.id"));
                radiologist.setEmail(resultSet.getString("u.email"));
                radiologist.setPassword(resultSet.getString("u.password"));
                radiologist.setRole(resultSet.getString("u.roles").split(","));
                radiologist.setName(resultSet.getString("u.name"));
                radiologist.setLastName(resultSet.getString("u.lastname"));
                radiologist.setBirth_date(resultSet.getDate("u.date_birth").toLocalDate());
                radiologist.setGender(resultSet.getString("u.gender"));
                // Set other user properties as needed
                interpretation.setRadiologist(radiologist);

                // Set image details
                Image image = new Image();
                image.setId(resultSet.getInt("im.id"));
                image.setFilename(resultSet.getString("im.filename"));
                image.setBodyPart(resultSet.getString("im.bodyPart"));
                image.setAquisitionDate(resultSet.getDate("im.aquisation_date").toLocalDate());
                // Set other image properties as needed
                interpretation.setImage(image);

                interpretations.add(interpretation);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving interpretations: " + e.getMessage());
            throw e;
        }
        return interpretations;
    }


}
