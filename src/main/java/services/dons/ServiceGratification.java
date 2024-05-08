package services.dons;

import entities.Donateur;
import entities.Gratification;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ServiceGratification implements IServices<Gratification> {

    private final Connection connection;

    public ServiceGratification(){
        connection = MyDataBase.getInstance().getConnection();


    }
    /*@Override
    public void ajouter(gratification gratification) throws SQLException {

        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        int donorId = getLastdonorDB();

        String req = "INSERT INTO gratification (id_donateur_id, titre_grat, desc_grat, date_grat, type_grat, montant) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setInt(1, donorId);
            statement.setString(2, gratification.getTitre_grat());
            statement.setString(3, gratification.getDesc_grat());
            statement.setDate(4, sqlDate);
            statement.setString(5, gratification.getType_grat());
            statement.setDouble(6, gratification.getMontant());

            statement.executeUpdate();
            System.out.println("Gratification ajoutée");
        } catch (SQLException e) {
            System.err.println("Failed to add gratification: " + e.getMessage());
            throw e;
        }
    }

     */
    @Override
    public void ajouter(Gratification gratification) throws SQLException {
        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        int donorId = getLastdonorDB();
        // Fetch the donor from the database
        Donateur donor = getDonorById(donorId);
        gratification.setDonor(donor); // Set the donor in the gratification object

        String req = "INSERT INTO gratification (id_donateur_id, titre_grat, desc_grat, date_grat, type_grat, montant) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setInt(1, donorId);
            statement.setString(2, gratification.getTitre_grat());
            statement.setString(3, gratification.getDesc_grat());
            statement.setDate(4, sqlDate);
            statement.setString(5, gratification.getType_grat());
            statement.setDouble(6, gratification.getMontant());

            statement.executeUpdate();
            System.out.println("Gratification ajoutée");
        } catch (SQLException e) {
            System.err.println("Failed to add gratification: " + e.getMessage());
            throw e;
        }
    }



    @Override
    public void modifier(Gratification gratification) throws SQLException {
        String sql = "update gratification set titre_grat=?,desc_grat=?,type_grat=?,montant=?  where id=?";
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, gratification.getTitre_grat());
        preparedStatement.setString(2, gratification.getDesc_grat());
        preparedStatement.setString(3, gratification.getType_grat());
        preparedStatement.setInt(4, gratification.getMontant());
        preparedStatement.setInt(5, gratification.getId());


        preparedStatement.executeUpdate();


    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM gratification WHERE id = " + id + "";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
            System.out.println("Instance supprimé");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Gratification> afficher() throws SQLException {
        List<Gratification> grats = new ArrayList<>();
        String req = "select * from gratification";
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            Gratification grat = new Gratification();
            grat.setId(rs.getInt("id"));
            //grat.setId_donateur_id(rs.getInt("Id_donateur_id"));
            grat.setDate_grat(rs.getDate("date_grat"));
            grat.setTitre_grat(rs.getString("titre_grat"));
            grat.setType_grat(rs.getString("type_grat"));
            grat.setDesc_grat(rs.getString("desc_grat"));
            grat.setMontant(rs.getInt("montant"));



            grats.add(grat);

        }


        return grats;
    }

    private int getLastdonorDB() throws SQLException {
        int donorId = -1;
        String query = "SELECT MAX(id) AS last_id FROM donateur";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                donorId = rs.getInt("last_id");
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve last inserted patient ID: " + e.getMessage());
            throw e;
        }
        return donorId;
    }

    public Donateur getDonorById(int donorId) {
        String query = "SELECT * FROM donateur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, donorId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Donateur donor = new Donateur();
                donor.setId(resultSet.getInt("id"));
                donor.setNom_donateur(resultSet.getString("nom_donateur"));
                donor.setPrenom_donateur(resultSet.getString("prenom_donateur"));
                donor.setTelephone(resultSet.getInt("telephone"));
                donor.setEmail(resultSet.getString("email"));
                donor.setType_donateur(resultSet.getString("type_donateur"));


                return donor;
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve donor: " + e.getMessage());
        }
        return null;
    }

    public Map<LocalDate, Integer> getGratificationsPerDay() throws SQLException {
        Map<LocalDate, Integer> gratificationsPerDay = new HashMap<>();

        String query = "SELECT date_grat, COUNT(*) AS count FROM gratification GROUP BY date_grat";

        try{
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDate date = rs.getDate("date_grat").toLocalDate();
                int count = rs.getInt("count");
                gratificationsPerDay.put(date, count);
            }


    }catch (SQLException s){
            System.out.println(s.getMessage());
        }        return gratificationsPerDay;
    }




    }