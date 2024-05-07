package services.diagnostic;

import controllers.user.SessionManager;
import entities.Droit;
import entities.Image;

import entities.User;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import services.Image.DroitServices;
import services.interpretation.InterpreationServices;
import utils.MyDataBase;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageService {

private int iduser=SessionManager.getLoggedInUser().getUser_id();

    private Connection connection;

    public ImageService(){
        connection = MyDataBase.getInstance().getConnection();
    }

    public void addImage(Image image) throws SQLException {
        // Now, insert the image using the patientId
        String req = "INSERT INTO image (filename, patient_id, radiologist_id, bodypart,aquisation_date ,dateajout ) VALUES (?, ?, ?, ?,?,?)";
        try (PreparedStatement st = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, image.getFilename());
            st.setInt(2, image.getPatient().getUser_id()); // Use the patientId obtained above
            st.setInt(3, iduser); // Assuming user_id is an integer
            st.setString(4, image.getBodyPart());
            st.setDate(5, java.sql.Date.valueOf(image.getAquisitionDate())); // Set acquisition date
            st.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now())); // Set current date as dateajout
            System.out.println(req);
            int rowsInserted = st.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    image.setId(id); // Set the ID to the Image object
                    System.out.println("Image Added Successfully with ID: " + id);

                    // Create Droit object and add it
                    User rad = new User();

                    rad.setUser_id(iduser);
                    Droit droit = new Droit(rad, image, "owner");
                    DroitServices droitServices = new DroitServices();
                    droitServices.add(droit);
                } else {
                    System.err.println("Failed to retrieve the image ID");
                }
            } else {
                System.err.println("Failed to add image");
            }
        } catch (SQLException e) {
            System.err.println("Failed to add image: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
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
            throw e;
        }
        return patientId;




    }

    public void deleteImageById(int imageId) throws SQLException {

        DroitServices droitServices = new DroitServices();
        droitServices.delete(imageId);
        InterpreationServices interpreationServices = new InterpreationServices();
        interpreationServices.deleteInterpretation(imageId);

        String query = "DELETE FROM image WHERE id = ?";
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





    // Method to retrieve a list of Image entities
    public List<Image> getImages() throws SQLException {
        List<Image> imageList = new ArrayList<>();
        String query = "SELECT i.*, p.*, r.* " +
                "FROM image i " +
                "INNER JOIN user p ON i.patient_id = p.id AND p.roles LIKE '%ROLE_PATIENT%' " +
                "INNER JOIN user r ON i.radiologist_id = r.id AND r.roles LIKE '%ROLE_RADIOLOGIST%' and i.radiologist_id="+iduser;
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                // Retrieve image properties
                int id = resultSet.getInt("id");
                String filename = resultSet.getString("filename");
                String bodyPart = resultSet.getString("bodyPart");
                LocalDate acquisitionDate = resultSet.getDate("aquisation_date").toLocalDate();
                LocalDate dateAjout = resultSet.getDate("dateajout").toLocalDate();

                // Retrieve patient information
                int patientId = resultSet.getInt("p.id");
                String patientName = resultSet.getString("p.name");
                String patientLastName = resultSet.getString("p.lastname");
                LocalDate patientBirthDate = resultSet.getDate("p.date_birth").toLocalDate();
                User patient = new User();
                patient.setUser_id(patientId);
                patient.setName(patientName);
                patient.setLastName(patientLastName);
                patient.setBirth_date(patientBirthDate);

                // Retrieve radiologist information
                int radiologistId = resultSet.getInt("r.id");
                String radiologistName = resultSet.getString("r.name");
                String radiologistLastName = resultSet.getString("r.lastname");
                LocalDate radiologistBirthDate = resultSet.getDate("r.date_birth").toLocalDate();
                User radiologist = new User();
                radiologist.setUser_id(radiologistId);
                radiologist.setName(radiologistName);
                radiologist.setLastName(radiologistLastName);
                radiologist.setBirth_date(radiologistBirthDate);

                // Create Image object
                Image image = new Image(id, filename, bodyPart, acquisitionDate);
                image.setRadiologist(radiologist);
                image.setPatient(patient);

                // Add Image object to the list
                imageList.add(image);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving images: " + e.getMessage());
            throw e;
        }
        return imageList;
    }
    public void editImage(Image image) throws SQLException {
        String query = "UPDATE image SET filename = ?, bodypart = ?, patient_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, image.getFilename());
            stmt.setString(2, image.getBodyPart());
            stmt.setInt(3, image.getPatient().getUser_id()); // Assuming image has a reference to the patient
            stmt.setInt(4, image.getId());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Image updated successfully");
            } else {
                System.err.println("Failed to update image");
            }
        } catch (SQLException e) {
            System.err.println("Failed to update image: " + e.getMessage());
            throw e;
        }
    }

    public Image getImageById(int imageId) throws SQLException {
        String query = "SELECT i.*, p.*, r.* " +
                "FROM image i " +
                "INNER JOIN user p ON i.patient_id = p.id AND p.roles LIKE '%ROLE_PATIENT%' " +
                "INNER JOIN user r ON i.radiologist_id = r.id AND r.roles LIKE '%ROLE_RADIOLOGIST%' " +
                "WHERE i.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, imageId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve image properties
                    int id = resultSet.getInt("id");
                    String filename = resultSet.getString("filename");
                    String bodyPart = resultSet.getString("bodyPart");
                    LocalDate acquisitionDate = resultSet.getDate("aquisation_date").toLocalDate();
                    LocalDate dateAjout = resultSet.getDate("dateajout").toLocalDate();

                    // Retrieve patient information
                    int patientId = resultSet.getInt("p.id");
                    String patientName = resultSet.getString("p.name");
                    String patientLastName = resultSet.getString("p.lastname");
                    LocalDate patientBirthDate = resultSet.getDate("p.date_birth").toLocalDate();
                    User patient = new User();
                    patient.setUser_id(patientId);
                    patient.setName(patientName);
                    patient.setLastName(patientLastName);
                    patient.setBirth_date(patientBirthDate);


                    // Retrieve radiologist information
                    int radiologistId = resultSet.getInt("r.id");
                    String radiologistName = resultSet.getString("r.name");
                    String radiologistLastName = resultSet.getString("r.lastname");
                    LocalDate radiologistBirthDate = resultSet.getDate("r.date_birth").toLocalDate();
                    User radiologist = new User();

                    radiologist.setUser_id(radiologistId);
                    radiologist.setName(radiologistName);
                    radiologist.setLastName(radiologistLastName);
                    radiologist.setBirth_date(radiologistBirthDate);

                    // Create and return Image object

                    Image image = new Image(id ,filename, bodyPart,acquisitionDate);
                    image.setRadiologist(radiologist);
                    image.setPatient(patient);






                    return image;



                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving image by ID: " + e.getMessage());
            throw e;
        }
        return null; // Return null if no image found with the specified ID
    }











    public   ArrayList<entities.Image> getSharedImageById(int radId) throws SQLException {
        ArrayList<Image> imagelist = new ArrayList<>();
        String query = "SELECT i.*, u1.*, u2.* FROM image i INNER JOIN droit d ON d.image_id = i.id INNER JOIN user u1 ON i.radiologist_id = u1.id INNER JOIN user u2 ON i.patient_id = u2.id WHERE d.radioloqist_id = ? AND d.role = 'guest';";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, iduser);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Inside the loop, retrieve image data and create Image objects
                    int id = resultSet.getInt("i.id");
                    String filename = resultSet.getString("i.filename");
                    String bodyPart = resultSet.getString("i.bodyPart");
                    LocalDate acquisitionDate = resultSet.getDate("i.aquisation_date").toLocalDate();
                    LocalDate dateAjout = resultSet.getDate("i.dateajout").toLocalDate();

                    // Retrieve patient information
                    int patientId = resultSet.getInt("u2.id");
                    String patientName = resultSet.getString("u2.name");
                    String patientLastName = resultSet.getString("u2.lastname");
                    LocalDate patientBirthDate = resultSet.getDate("u2.date_birth").toLocalDate();
                    User patient = new User();
                    patient.setUser_id(patientId);
                    patient.setName(patientName);
                    patient.setLastName(patientLastName);
                    patient.setBirth_date(patientBirthDate);

                    // Retrieve radiologist information
                    int radiologistId = resultSet.getInt("u1.id");
                    String radiologistName = resultSet.getString("u1.name");
                    String radiologistLastName = resultSet.getString("u1.lastname");
                    LocalDate radiologistBirthDate = resultSet.getDate("u1.date_birth").toLocalDate();
                    User radiologist = new User();
                    radiologist.setUser_id(radiologistId);
                    radiologist.setName(radiologistName);
                    radiologist.setLastName(radiologistLastName);
                    radiologist.setBirth_date(radiologistBirthDate);

                    // Create Image object and set patient and radiologist
                    Image image = new Image(id, filename, bodyPart, acquisitionDate);
                    image.setPatient(patient);
                    image.setRadiologist(radiologist);

                    imagelist.add(image);
                }
                return imagelist;
            }}}
    public static void convertDicomToPng(String inputFilePath, String outputFilePath) {
        try {
            // Read DICOM file
            File inputFile = new File(inputFilePath);
            ImageInputStream iis = ImageIO.createImageInputStream(inputFile);

            // Get DICOM image reader
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            ImageReader reader = iter.next();
            reader.setInput(iis, false);

            // Read DICOM image
            BufferedImage image = reader.read(0, new DicomImageReadParam());

            // Close image input stream
            iis.close();

            // Write PNG file
            File outputFile = new File(outputFilePath);
            ImageIO.write(image, "png", outputFile);

            System.out.println("Conversion complete.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
