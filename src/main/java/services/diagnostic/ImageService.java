package services.diagnostic;

import entities.Image;
import services.user.PatientService;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageService {

    private Connection connection;

    public ImageService(){
        connection = MyDataBase.getInstance().getConnection();
    }

    public void addImage(Image image) throws SQLException {
        // First, add the patient associated with the image to the database
        PatientService patientService = new PatientService();
        patientService.add(image.getPatient());

        // Get the patient ID
        int patientId = getLastInsertedPatientIdFromDatabase();

        if (patientId != -1) {
            // Now, insert the image using the patientId
            String req = "INSERT INTO image (filename, patient_id) VALUES (?, ?)";
            try (PreparedStatement st = connection.prepareStatement(req)) {
                st.setString(1, image.getFilename());
                st.setInt(2, patientId);
                st.executeUpdate();
                System.out.println("Image Added Successfully");
            } catch (SQLException e) {
                System.err.println("Failed to add image: " + e.getMessage());
                throw e; // Rethrow the exception to be handled by the caller
            }
        } else {
            System.out.println("Patient Not Found!");
        }
    }

    private int getLastInsertedPatientIdFromDatabase() throws SQLException {
        int patientId = -1;
        String query = "SELECT LAST_INSERT_ID() as last_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                patientId = rs.getInt("last_id");
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve last inserted patient ID: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
        return patientId;
    }



}

