package services.diagnostic;

import entities.Doctor;
import entities.Image;
import entities.Report;
import java.sql.Date;
import java.time.LocalDate;

import services.ReportCrud;
import utils.MyDataBase;

import java.sql.*;
import java.util.List;


public class ReportService implements ReportCrud<Report> {
    private final Connection connection;

    public ReportService() {
        connection = MyDataBase.getInstance().getConnection();

    }
    @Override
    public void add(Report report, Doctor doctor, Image image) throws SQLException {
        String sql = "INSERT INTO report (doctor_id,image_id,interpretation_med, interpretation_rad, is_edited,date) " +
                "VALUES (?, ?, ?, ?, ?,?)";

        int doctorId = getDoctorIdByMatricule(doctor.getMatricule());
        int imageId = getImageIdByFilename(image.getFilename());

        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);


        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, doctorId);
            statement.setInt(2, imageId);
            statement.setString(3, report.getInterpretation_med());
            statement.setString(4, report.getInterpretation_rad());
            statement.setBoolean(5, report.isIs_edited());
            statement.setDate(6, sqlDate);

            statement.executeUpdate();
            System.out.println("Report added successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to add report: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(int id) throws SQLException {


    }

    @Override
    public void update(Report entity) throws SQLException {

    }

    @Override
    public List<Report> displayAll() throws SQLException {
        return null;
    }

    public int getImageIdByFilename(String filename) throws SQLException {
        String sql = "SELECT id FROM image WHERE filename = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, filename);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Image not found for filename: " + filename);
                }
            }
        }
    }

    public int getDoctorIdByMatricule(String matricule) throws SQLException {
        String sql = "SELECT id FROM doctor WHERE matricule = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricule);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Doctor not found for matricule: " + matricule);
                }
            }
        }
    }



}
