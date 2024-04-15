package services.user;
import com.google.gson.Gson;
import entities.Doctor;
import entities.Radiologist;
import entities.User;
import services.ICrud;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;

public class RadiologistService implements ICrud<Radiologist> {
    private Connection connection;

    public RadiologistService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void add(Radiologist el) throws SQLException {
        el.setRole(new String[]{"ROLE_USER", "ROLE_Radiologist"});
        String req = "INSERT INTO user (email, roles, password, name, brochure_filename, lastname, date_birth, gender,matricule) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement st = connection.prepareStatement(req);
        Gson gson = new Gson();
        String roles = gson.toJson(el.getRole());
        st.setString(1, el.getEmail());
        st.setString(2, roles);
        st.setString(3, el.getPassword());
        st.setString(4, el.getName());
        st.setString(5, el.getBrochure_filename());
        st.setString(6, el.getLastName());
        st.setString(7, el.getBirth_date().toString());
        st.setString(8, el.getGender());
        st.setString(9, el.getMatricule());
        st.executeUpdate();
        System.out.println("Added Successfully");
    }
    @Override
    public void update(Radiologist el, int id) throws SQLException {
        UserService service = new UserService();
        service.update(el, id);
        String req = "UPDATE user SET matricule = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, el.getMatricule());
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Modified");
        } catch (SQLException e) {
            System.err.println("Error modifying user: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM user WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Deleted Successfully");
    }

    @Override
    public ArrayList<Radiologist> showAll() throws SQLException {
        ArrayList<Radiologist> radiologist = new ArrayList<>();
        String req = "SELECT * FROM radiologist";
        Statement st = connection.createStatement();
        ResultSet rs =  st.executeQuery(req);

        while (rs.next()){
            Radiologist rad = new Radiologist();
            rad.setBirth_date(rs.getDate("date_birth").toLocalDate());
            rad.setUser_id(rs.getInt("id"));
            rad.setEmail(rs.getString("email"));
            rad.setGender(rs.getString("gender"));
            rad.setBrochure_filename(rs.getString("brochure_filename"));
            rad.setPassword(rs.getString("password"));
            rad.setName(rs.getString("name"));
            rad.setLastName(rs.getString("lastname"));
            String[] x = new String[]{rs.getString("roles")};
            rad.setRole(x);
            rad.setMatricule(rs.getString("matricule"));
            radiologist.add(rad);
        }
        return radiologist;
    }
    public User findUserById(int id) {
        User user = null;
        String query = "SELECT * FROM user u, radiologist r WHERE u.id = r.user_id AND r.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setBirth_date(rs.getDate("date_birth").toLocalDate());
                    user.setUser_id(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setGender(rs.getString("gender"));
                    user.setBrochure_filename(rs.getString("brochure_filename"));
                    user.setPassword(rs.getString("password"));
                    user.setName(rs.getString("name"));
                    user.setLastName(rs.getString("lastname"));
                    user.setUser_id(rs.getInt("id"));
                    String[] x = new String[]{rs.getString("roles")};
                    user.setRole(x);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private int getLastInsertedUserIdFromDatabase() throws SQLException {
        int userId = -1;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT LAST_INSERT_ID() as last_id";
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("last_id");
            }
        } finally {
            // Close resources
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return userId;
    }

}
