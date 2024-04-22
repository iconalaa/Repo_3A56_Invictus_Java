package services.diagnostic;

import entities.Prescription;
import entities.Report;
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
    public List<Prescription> displayAll() throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescription";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int reportId = resultSet.getInt("report_id");
                String contenu = resultSet.getString("contenu");
                Date date = resultSet.getDate("date");
                String signatureFilename = resultSet.getString("signature_filename");


                Prescription prescription = new Prescription( contenu, signatureFilename);
                prescription.setId(id);

                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            System.err.println("Failed to display all prescriptions: " + e.getMessage());
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

}
