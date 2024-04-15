//package services.diagnostic;
//
//import entities.Doctor;
//import entities.Image;
//import entities.Report;
//import java.sql.Date;
//import java.time.LocalDate;
//
//import entities.User;
//import services.ReportCrud;
//import utils.MyDataBase;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class ReportService implements ReportCrud<Report> {
//    private final Connection connection;
//
//    public ReportService() {
//        connection = MyDataBase.getInstance().getConnection();
//
//    }
//    @Override
//    public void add(Report report, Doctor doctor, Image image) throws SQLException {
//        String sql = "INSERT INTO report (doctor_id,image_id,interpretation_med, interpretation_rad, is_edited,date) " +
//                "VALUES (?, ?, ?, ?, ?,?)";
//
//        int doctorId = getDoctorIdByMatricule(doctor.getMatricule());
//        int imageId = getImageIdByFilename(image.getFilename());
//
//        LocalDate currentDate = LocalDate.now();
//        Date sqlDate = Date.valueOf(currentDate);
//
//
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, doctorId);
//            statement.setInt(2, imageId);
//            statement.setString(3, report.getInterpretation_med());
//            statement.setString(4, report.getInterpretation_rad());
//            statement.setBoolean(5, report.isIs_edited());
//            statement.setDate(6, sqlDate);
//
//            statement.executeUpdate();
//            System.out.println("Report added successfully.");
//        } catch (SQLException e) {
//            System.err.println("Failed to add report: " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Override
//    public void delete(int id) throws SQLException {
//
//        deletePrescriptionsByReportId(id);
//
//        String sql = "DELETE FROM report WHERE id = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, id);
//            int rowsDeleted = statement.executeUpdate();
//            if (rowsDeleted > 0) {
//                System.out.println("Report deleted successfully.");
//            } else {
//                System.out.println("Report not found with ID: " + id);
//            }
//        }
//    }
//
//    @Override
//    public void update(Report report, int id) throws SQLException {
//        String sql = "UPDATE report SET interpretation_med = ?, interpretation_rad = ?, is_edited = ?, date = ? WHERE id = ?";
//
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, report.getInterpretation_med());
//            statement.setString(2, report.getInterpretation_rad());
//            statement.setBoolean(3, report.isIs_edited());
//            statement.setDate(4, new java.sql.Date(report.getDate().getTime()));
//            statement.setInt(5, id);
//
//            int rowsUpdated = statement.executeUpdate();
//            if (rowsUpdated > 0) {
//                System.out.println("Report with ID " + id + " updated successfully.");
//            } else {
//                System.out.println("Report with ID " + id + " not found, update failed.");
//            }
//        } catch (SQLException e) {
//            System.err.println("Failed to update report with ID " + id + ": " + e.getMessage());
//            throw e;
//        }
//    }
//
//    @Override
//    public List<Report> displayAll() throws SQLException {
//        List<Report> reports = new ArrayList<>();
//
//        String sql = "SELECT * FROM report";
//
//        try (PreparedStatement statement = connection.prepareStatement(sql);
//             ResultSet resultSet = statement.executeQuery()) {
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String interpretation_med = resultSet.getString("interpretation_med");
//                String interpretation_rad = resultSet.getString("interpretation_rad");
//                int doctorId = resultSet.getInt("doctor_id");
//                int imageId = resultSet.getInt("image_id");
//                boolean isEdited = resultSet.getBoolean("is_edited");
//                Date date = resultSet.getDate("date");
//
//                // Assuming you have constructor in Report class that accepts all these parameters
//                Doctor doctor = getDoctorById(doctorId);
//                Image image = getImageById(imageId);
//                Report report = new Report(id, interpretation_med, interpretation_rad, doctor, image, date, isEdited);
//
//                // Add report to the list
//                reports.add(report);
//            }
//        }
//
//        return reports;
//    }
//
//
//
///////////////////////////////second-use-methods/////////////////////////
//    public int getImageIdByFilename(String filename) throws SQLException {
//        String sql = "SELECT id FROM image WHERE filename = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, filename);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    return resultSet.getInt("id");
//                } else {
//                    throw new SQLException("Image not found for filename: " + filename);
//                }
//            }
//        }
//    }
//
//    public int getDoctorIdByMatricule(String matricule) throws SQLException {
//        String sql = "SELECT id FROM doctor WHERE matricule = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, matricule);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    return resultSet.getInt("id");
//                } else {
//                    throw new SQLException("Doctor not found for matricule: " + matricule);
//                }
//            }
//        }
//
//
//    }
//
//    private Doctor getDoctorById(int doctorId) throws SQLException {
//        String sql = "SELECT * FROM doctor d INNER JOIN user u ON d.user_id = u.id WHERE d.id = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, doctorId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    Doctor doctor = new Doctor();
//                    doctor.setId(resultSet.getInt("id"));
//                    doctor.setMatricule(resultSet.getString("matricule"));
//
//                    // Fetch user details
//                    User user = new User();
//                    user.setUser_id(resultSet.getInt("user_id"));
//                    user.setName(resultSet.getString("name"));
//                    user.setLastName(resultSet.getString("lastname"));
//                    user.setEmail(resultSet.getString("email"));
//                    user.setGender(resultSet.getString("gender"));
//
//                    doctor.setUser(user);
//
//                    return doctor;
//                } else {
//                    throw new SQLException("Doctor not found for ID: " + doctorId);
//                }
//            }
//        }
//    }
//
//    private Image getImageById(int imageId) throws SQLException {
//        String sql = "SELECT * FROM image WHERE id = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, imageId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    Image image = new Image();
//                    image.setId(resultSet.getInt("id"));
//                    image.setFilename(resultSet.getString("filename"));
//                    return image;
//                } else {
//                    throw new SQLException("Image not found for ID: " + imageId);
//                }
//            }
//        }
//    }
//
//    private void deletePrescriptionsByReportId(int reportId) throws SQLException {
//        String sql = "DELETE FROM prescription WHERE report_id = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, reportId);
//            statement.executeUpdate();
//            System.out.println("Prescriptions linked to the report deleted successfully.");
//        }
//    }
//
//
//}
//
//
//
//
