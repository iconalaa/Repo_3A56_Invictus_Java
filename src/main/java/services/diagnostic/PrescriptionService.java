package services.diagnostic;

import entities.Image;
import entities.Prescription;
import entities.Report;
import entities.User;
import services.PrescriptionCrud;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionService implements PrescriptionCrud<Prescription> {
    private final Connection connection;
    public PrescriptionService(){
        connection = MyDataBase.getInstance().getConnection();
    }


    @Override
    public void add(Prescription entity, Report report) throws SQLException {
        String sql = "INSERT INTO prescription (report_id,contenu, date, signature_filename) " +
                "VALUES (?, ?, ?, ?)";

        int reportId = getReportIdByInterpretation(report.getInterpretation_med());

        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reportId);
            statement.setString(2, entity.getContenu());
            statement.setDate(3,sqlDate);
            statement.setString(4, entity.getSignature_filename());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Prescription added successfully.");
            } else {
                System.out.println("Failed to add prescription.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to add prescription: " + e.getMessage());
            throw e;
        }

    }
    @Override
    public void add2(Prescription entity,int reportId) throws SQLException {
        String sql = "INSERT INTO prescription (report_id,contenu, date, signature_filename) " +
                "VALUES (?, ?, ?, ?)";


        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reportId);
            statement.setString(2, entity.getContenu());
            statement.setDate(3,sqlDate);
            statement.setString(4, entity.getSignature_filename());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Prescription added successfully.");
            } else {
                System.out.println("Failed to add prescription.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to add prescription: " + e.getMessage());
            throw e;
        }


    }
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM prescription WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Prescription deleted successfully.");
            } else {
                System.out.println("Prescription not found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Failed to delete prescription with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    @Override
    public void update(Prescription entity, int id) throws SQLException {
        String sql = "UPDATE prescription SET contenu = ?, signature_filename = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getContenu());
            statement.setString(2, entity.getSignature_filename());
            statement.setInt(3, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Prescription with ID " + id + " updated successfully.");
            } else {
                System.out.println("Prescription with ID " + id + " not found, update failed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to update prescription with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    @Override
    public List<Prescription> displayAll(int id) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();

        String sql = "SELECT p.id AS prescription_id, p.report_id, p.contenu, p.date, p.signature_filename, " +
                "r.id AS report_id, r.doctor_id, " +
                "u.name AS doctor_name, u.lastname AS doctor_lastName, " +
                "i.id AS image_id, " +
                "patient.id AS patient_id, patient.name AS patient_name, patient.lastname AS patient_lastName " +
                "FROM prescription p " +
                "INNER JOIN report r ON p.report_id = r.id " +
                "INNER JOIN user u ON r.doctor_id = u.id " +
                "INNER JOIN image i ON r.image_id = i.id " +
                "LEFT JOIN user patient ON i.patient_id = patient.id " +
                "WHERE r.doctor_id = ?"; // Add WHERE clause to filter by doctor ID

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id); // Set the doctor ID parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int prescriptionId = resultSet.getInt("prescription_id");
                    int reportId = resultSet.getInt("report_id");
                    int doctorId = resultSet.getInt("doctor_id");
                    String doctorName = resultSet.getString("doctor_name");
                    String doctorLastName = resultSet.getString("doctor_lastName");
                    int imageId = resultSet.getInt("image_id");
                    int patientId = resultSet.getInt("patient_id");
                    String patientName = resultSet.getString("patient_name");
                    String patientLastName = resultSet.getString("patient_lastName");
                    String contenu = resultSet.getString("contenu");
                    Date date = resultSet.getDate("date");
                    String signatureFilename = resultSet.getString("signature_filename");

                    User doctor = new User();
                    doctor.setUser_id(doctorId);
                    doctor.setName(doctorName);
                    doctor.setLastName(doctorLastName);

                    User patient = new User();
                    patient.setUser_id(patientId);
                    patient.setName(patientName);
                    patient.setLastName(patientLastName);

                    Image image = new Image();
                    image.setId(imageId);
                    image.setPatient(patient);

                    Report report = new Report();
                    report.setId(reportId);
                    report.setDoctor(doctor);
                    report.setImage(image);

                    Prescription prescription = new Prescription(contenu, signatureFilename);
                    prescription.setId(prescriptionId);
                    prescription.setReport(report);
                    prescription.setContenu(contenu);
                    prescription.setDate(date);
                    prescription.setSignature_filename(signatureFilename);

                    prescriptions.add(prescription);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to display prescriptions: " + e.getMessage());
            throw e;
        }

        return prescriptions;
    }

    public int getReportIdByInterpretation(String interpretation) throws SQLException {
        String sql = "SELECT id FROM report WHERE interpretation_med = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, interpretation);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Report not found for interpretation: " + interpretation);
                }
            }
        }
    }
    public boolean hasPrescription(int reportId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM prescription WHERE report_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reportId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
    public int getAllPrescriptionsCount(int doctorId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM prescription p " +
                "INNER JOIN report r ON p.report_id = r.id " +
                "WHERE r.doctor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, doctorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        }
        return 0;
    }



}
