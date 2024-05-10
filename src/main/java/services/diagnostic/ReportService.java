package services.diagnostic;

import entities.Image;
import entities.Report;
import java.sql.Date;
import java.time.LocalDate;

import entities.User;
import services.ReportCrud;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ReportService implements ReportCrud<Report> {
    private final Connection connection;

    public ReportService() {
        connection = MyDataBase.getInstance().getConnection();

    }
    @Override
    public void add(Report report, User doctor, Image image) throws SQLException {
        String sql = "INSERT INTO report (doctor_id, image_id, interpretation_rad, is_edited) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, doctor.getUser_id()); // Assuming doctor.getId() returns the doctor's ID
            statement.setInt(2, image.getId());
            statement.setString(3, report.getInterpretation_rad());
            statement.setBoolean(4, false); // Set is_edited to false by default

            statement.executeUpdate();
            System.out.println("Report added successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to add report: " + e.getMessage());
            throw e;
        }
    }
    @Override
    public List<Report> displayAll(int id) throws SQLException {
        List<Report> reports = new ArrayList<>();

        String sql = "SELECT r.id, r.interpretation_med, r.interpretation_rad, r.doctor_id, r.image_id, r.is_edited, r.date, " +
                "u.id AS doctor_id, u.name AS doctor_name, u.lastname AS doctor_lastName, " +
                "i.id AS image_id, i.filename AS image_filename, i.bodypart AS image_bodypart, " +
                "p.id AS patient_id, p.name AS patient_name, p.lastname AS patient_lastName, " +
                "rad.id AS radiologist_id, rad.name AS radiologist_name, rad.lastname AS radiologist_lastName " +
                "FROM report r " +
                "INNER JOIN user u ON r.doctor_id = u.id " +
                "INNER JOIN image i ON r.image_id = i.id " +
                "LEFT JOIN user p ON i.patient_id = p.id " +
                "LEFT JOIN user rad ON i.radiologist_id = rad.id " +
                "WHERE r.doctor_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int reportId = resultSet.getInt("id");
                String interpretationMed = resultSet.getString("interpretation_med");
                String interpretationRad = resultSet.getString("interpretation_rad");
                int doctorId = resultSet.getInt("doctor_id");
                String doctorName = resultSet.getString("doctor_name");
                String doctorLastName = resultSet.getString("doctor_lastName");
                int imageId = resultSet.getInt("image_id");
                String imageFilename = resultSet.getString("image_filename");
                String imageBodyPart = resultSet.getString("image_bodypart");
                boolean isEdited = resultSet.getBoolean("is_edited");
                Date date = resultSet.getDate("date");

                User doctor = new User();
                doctor.setUser_id(doctorId);
                doctor.setName(doctorName);
                doctor.setLastName(doctorLastName);

                Image image = new Image();
                image.setId(imageId);
                image.setFilename(imageFilename);
                image.setBodyPart(imageBodyPart);

                int patientId = resultSet.getInt("patient_id");
                if (!resultSet.wasNull()) {
                    String patientName = resultSet.getString("patient_name");
                    String patientLastName = resultSet.getString("patient_lastName");
                    User patient = new User();
                    patient.setUser_id(patientId);
                    patient.setName(patientName);
                    patient.setLastName(patientLastName);
                    image.setPatient(patient);
                }

                int radiologistId = resultSet.getInt("radiologist_id");
                if (!resultSet.wasNull()) {
                    String radiologistName = resultSet.getString("radiologist_name");
                    String radiologistLastName = resultSet.getString("radiologist_lastName");
                    User radiologist = new User();
                    radiologist.setUser_id(radiologistId);
                    radiologist.setName(radiologistName);
                    radiologist.setLastName(radiologistLastName);
                    image.setRadiologist(radiologist);
                }

                Report report = new Report(reportId, interpretationMed, interpretationRad, doctor, image, date, isEdited);
                reports.add(report);
            }
        } catch (SQLException e) {
            System.err.println("Failed to display reports: " + e.getMessage());
            throw e;
        }

        return reports;
    }
    @Override
    public void update(Report report, int id) throws SQLException {
        String sql = "UPDATE report SET interpretation_med = ?, interpretation_rad = ?, is_edited = ?, date = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, report.getInterpretation_med());
            statement.setString(2, report.getInterpretation_rad());
            statement.setBoolean(3, report.isIs_edited());
            statement.setDate(4, new java.sql.Date(report.getDate().getTime()));
            statement.setInt(5, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Report with ID " + id + " updated successfully.");
            } else {
                System.out.println("Report with ID " + id + " not found, update failed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to update report with ID " + id + ": " + e.getMessage());
            throw e;
        }
    }
    @Override
    public void delete(int id) throws SQLException {

        deletePrescriptionsByReportId(id);

        String sql = "DELETE FROM report WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Report deleted successfully.");
            } else {
                System.out.println("Report not found with ID: " + id);
            }
        }
    }

    /////////////////////////////second-use-methods/////////////////////////
    public List<Report> displayAll() throws SQLException {
        List<Report> reports = new ArrayList<>();

        String sql = "SELECT r.id, r.interpretation_med, r.interpretation_rad, r.doctor_id, r.image_id, r.is_edited, r.date, " +
                "u.id AS doctor_id, u.name AS doctor_name, u.lastname AS doctor_lastName, " +
                "i.id AS image_id, i.filename AS image_filename, i.bodypart AS image_bodypart, " +
                "p.id AS patient_id, p.name AS patient_name, p.lastname AS patient_lastName, " +
                "rad.id AS radiologist_id, rad.name AS radiologist_name, rad.lastname AS radiologist_lastName " +
                "FROM report r " +
                "INNER JOIN user u ON r.doctor_id = u.id " +
                "INNER JOIN image i ON r.image_id = i.id " +
                "LEFT JOIN user p ON i.patient_id = p.id " +
                "LEFT JOIN user rad ON i.radiologist_id = rad.id " ;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int reportId = resultSet.getInt("id");
                String interpretationMed = resultSet.getString("interpretation_med");
                String interpretationRad = resultSet.getString("interpretation_rad");
                int doctorId = resultSet.getInt("doctor_id");
                String doctorName = resultSet.getString("doctor_name");
                String doctorLastName = resultSet.getString("doctor_lastName");
                int imageId = resultSet.getInt("image_id");
                String imageFilename = resultSet.getString("image_filename");
                String imageBodyPart = resultSet.getString("image_bodypart");
                boolean isEdited = resultSet.getBoolean("is_edited");
                Date date = resultSet.getDate("date");

                User doctor = new User();
                doctor.setUser_id(doctorId);
                doctor.setName(doctorName);
                doctor.setLastName(doctorLastName);

                Image image = new Image();
                image.setId(imageId);
                image.setFilename(imageFilename);
                image.setBodyPart(imageBodyPart);

                int patientId = resultSet.getInt("patient_id");
                if (!resultSet.wasNull()) {
                    String patientName = resultSet.getString("patient_name");
                    String patientLastName = resultSet.getString("patient_lastName");
                    User patient = new User();
                    patient.setUser_id(patientId);
                    patient.setName(patientName);
                    patient.setLastName(patientLastName);
                    image.setPatient(patient);
                }

                int radiologistId = resultSet.getInt("radiologist_id");
                if (!resultSet.wasNull()) {
                    String radiologistName = resultSet.getString("radiologist_name");
                    String radiologistLastName = resultSet.getString("radiologist_lastName");
                    User radiologist = new User();
                    radiologist.setUser_id(radiologistId);
                    radiologist.setName(radiologistName);
                    radiologist.setLastName(radiologistLastName);
                    image.setRadiologist(radiologist);
                }

                Report report = new Report(reportId, interpretationMed, interpretationRad, doctor, image, date, isEdited);
                reports.add(report);
            }
        } catch (SQLException e) {
            System.err.println("Failed to display reports: " + e.getMessage());
            throw e;
        }

        return reports;
    }
    private void deletePrescriptionsByReportId(int reportId) throws SQLException {
        String sql = "DELETE FROM prescription WHERE report_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reportId);
            statement.executeUpdate();
            System.out.println("Prescriptions linked to the report deleted successfully.");
        }
    }
    public List<Report> displayEditedReports(int id) throws SQLException {
        List<Report> reports = new ArrayList<>();

        String sql = "SELECT r.id, r.interpretation_med, r.interpretation_rad, r.doctor_id, r.image_id, r.is_edited, r.date, " +
                "u.id AS doctor_id, u.name AS doctor_name, u.lastname AS doctor_lastName, " +
                "i.id AS image_id, i.filename AS image_filename, i.bodypart AS image_bodypart, " +
                "p.id AS patient_id, p.name AS patient_name, p.lastname AS patient_lastName, " +
                "rad.id AS radiologist_id, rad.name AS radiologist_name, rad.lastname AS radiologist_lastName " +
                "FROM report r " +
                "INNER JOIN user u ON r.doctor_id = u.id " +
                "INNER JOIN image i ON r.image_id = i.id " +
                "LEFT JOIN user p ON i.patient_id = p.id " +
                "LEFT JOIN user rad ON i.radiologist_id = rad.id " +
                "WHERE r.doctor_id = ? AND r.is_edited = true"; // Added condition for doctor_id

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id); // Set the doctor's ID parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int reportId = resultSet.getInt("id");
                    String interpretationMed = resultSet.getString("interpretation_med");
                    String interpretationRad = resultSet.getString("interpretation_rad");
                    int doctorId = resultSet.getInt("doctor_id");
                    String doctorName = resultSet.getString("doctor_name");
                    String doctorLastName = resultSet.getString("doctor_lastName");
                    int imageId = resultSet.getInt("image_id");
                    String imageFilename = resultSet.getString("image_filename");
                    String imageBodyPart = resultSet.getString("image_bodypart");
                    boolean isEdited = resultSet.getBoolean("is_edited");
                    Date date = resultSet.getDate("date");

                    User doctor = new User();
                    doctor.setUser_id(doctorId);
                    doctor.setName(doctorName);
                    doctor.setLastName(doctorLastName);

                    Image image = new Image();
                    image.setId(imageId);
                    image.setFilename(imageFilename);
                    image.setBodyPart(imageBodyPart);

                    int patientId = resultSet.getInt("patient_id");
                    if (!resultSet.wasNull()) {
                        String patientName = resultSet.getString("patient_name");
                        String patientLastName = resultSet.getString("patient_lastName");
                        User patient = new User();
                        patient.setUser_id(patientId);
                        patient.setName(patientName);
                        patient.setLastName(patientLastName);
                        image.setPatient(patient);
                    }

                    int radiologistId = resultSet.getInt("radiologist_id");
                    if (!resultSet.wasNull()) {
                        String radiologistName = resultSet.getString("radiologist_name");
                        String radiologistLastName = resultSet.getString("radiologist_lastName");
                        User radiologist = new User();
                        radiologist.setUser_id(radiologistId);
                        radiologist.setName(radiologistName);
                        radiologist.setLastName(radiologistLastName);
                        image.setRadiologist(radiologist);
                    }

                    Report report = new Report(reportId, interpretationMed, interpretationRad, doctor, image, date, isEdited);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to display edited reports: " + e.getMessage());
            throw e;
        }

        return reports;
    }

    public int getAwaitingReportsCount(int id) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM report WHERE is_edited = false AND doctor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id); // Set the doctor's ID parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to get awaiting reports count: " + e.getMessage());
            throw e;
        }
        return 0;
    }

    public int getReportsDoneCount(int id) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM report WHERE is_edited = true AND doctor_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id); // Set the doctor's ID parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to get reports done count: " + e.getMessage());
            throw e;
        }
        return 0;
    }

    public boolean checkReportExists(int imageId) {
        try {
            // Execute a query to check if the image ID exists in the report table
            String sql = "SELECT COUNT(*) FROM report WHERE image_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, imageId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    // If there's at least one row in the result set, a report exists for the image
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return false by default if there's an error or no report exists
        return false;
    }

    public Report getReportWithImageId(int id) throws SQLException {

        String sql = "SELECT * FROM report WHERE image_id = ? "; // Added condition for doctor_id
        Report report = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id); // Set the doctor's ID parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet !=null) {
                    while (resultSet.next()) {
                        int reportId = resultSet.getInt("id");


                        report = new Report();
                        report.setId(reportId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to display edited reports: " + e.getMessage());
            throw e;
        }

        return report;
    }
    public void deleteReportByImage(int id) throws SQLException {
        Report report = getReportWithImageId(id);
        if (report != null) {
            deletePrescriptionsByReportId(report.getId());

            String sql = "DELETE FROM report WHERE image_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Report deleted successfully.");
                } else {
                    System.out.println("Report not found with ID: " + id);
                }
            }
        }
    }
}



