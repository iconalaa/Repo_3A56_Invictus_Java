package Services;

import Entities.donateur;
import Entities.gratification;
import Utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ServiceDonateur implements IServices<donateur>  {

    Connection connection;

    public ServiceDonateur() {
        MyDataBase a = new MyDataBase();
        connection = a.connection;

    }

    @Override
    public void ajouter(donateur donateur) throws SQLException {

        /*String req = "insert into donateur (Nom_donateur,Prenom_donateur,email,type_donateur,telephone)" +
                "values(" + donateur.getNom_donateur() + "','" + donateur.getPrenom_donateur() + "','" + donateur.getEmail() + "','" + donateur.getType_donateur()+ "','" + donateur.getTelephone() + ")";

        Statement statement = connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("donateur ajoute");


         */

            String req = "INSERT INTO donateur (telephone, nom_donateur, prenom_donateur, email, type_donateur) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(req)) {
                statement.setInt(1, donateur.getTelephone());
                statement.setString(2, donateur.getNom_donateur());
                statement.setString(3, donateur.getPrenom_donateur());
                statement.setString(4, donateur.getEmail());
                statement.setString(5, donateur.getType_donateur());

                statement.executeUpdate();
                System.out.println("Donateur ajoutée");
            } catch (SQLException e) {
                System.err.println("Erreur d'ajout: " + e.getMessage());
                throw e;
            }
        }



    @Override
    public void modifier(donateur donateur) throws SQLException {
        String req = "update donateur set nom_donateur=?, prenom_donateur=?, type_donateur=?, email=?, telephone=? where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, donateur.getNom_donateur());
            preparedStatement.setString(2, donateur.getPrenom_donateur());
            preparedStatement.setString(3, donateur.getType_donateur());
            preparedStatement.setString(4, donateur.getEmail());
            preparedStatement.setInt(5, donateur.getTelephone());
            preparedStatement.setInt(6, donateur.getId()); // Set the id
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println("Erreur de mise à jour:" + e.getMessage());
        }
    }


    @Override
    public void supprimer(int id) throws SQLException {

        String sql = "DELETE FROM donateur WHERE id = " + id + "";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(sql);
            System.out.println("Instance supprimé");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<donateur> afficher() throws SQLException {
        List<donateur> personnes = new ArrayList<>();
        String req = "select * from donateur";
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            donateur personne = new Entities.donateur();
            personne.setId(rs.getInt("id"));
            personne.setNom_donateur(rs.getString("nom_donateur"));
            personne.setPrenom_donateur(rs.getString("prenom_donateur"));
            personne.setType_donateur(rs.getString("type_donateur"));
            personne.setEmail(rs.getString("email"));
            personne.setTelephone(rs.getInt("telephone"));



            personnes.add(personne);
        }


        return personnes;
    }

}
