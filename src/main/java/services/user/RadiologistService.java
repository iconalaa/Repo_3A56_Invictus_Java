package services.user;

import com.google.gson.Gson;
import entities.Radiologist;
import entities.User;
import services.ICrud;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public ArrayList<Radiologist> showAll() throws SQLException {
        return null;
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
