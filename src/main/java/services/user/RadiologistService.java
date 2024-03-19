package services.user;
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
        UserService userService = new UserService();
        el.getUser().setRole(new String[]{"ROLE_USER","ROLE_RADIOLOGIST"});
        userService.add(el.getUser());
        int id = getLastInsertedUserIdFromDatabase();
        if (id != -1){
        String req = "INSERT INTO radiologist (user_id,mat_cnom,dispo) VALUES (?, ?, ?)";
        PreparedStatement st = connection.prepareStatement(req);
        st.setInt(1,id);
        st.setString(2,el.getMat_nom() );
        st.setBoolean(3,el.isDispo());
        st.executeUpdate();
        System.out.println("Added Successfully");
        }else System.out.println("User Not Found !");
    }
    @Override
    public void update(Radiologist el, int id) throws SQLException {
        String req = "UPDATE radiologist SET mat_cnom = ?, dispo = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, el.getMat_nom());
            ps.setBoolean(2, el.isDispo());
            ps.setInt(3, id);
            ps.executeUpdate();
            System.out.println("Modified");
        } catch (SQLException e) {
            System.err.println("Error modifying user: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        User user = null;
        user = findUserById(id);
        user.setRole(new String[] {"ROLE_USER"});
        UserService service = new UserService();
        service.update(user,user.getUser_id());
        String req = "DELETE FROM radiologist WHERE id = ?";
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
            rad.setMat_nom(rs.getString("mat_cnom"));
            rad.setDispo(rs.getBoolean("dispo"));
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
