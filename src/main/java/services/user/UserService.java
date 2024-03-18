package services.user;

import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonValue;
import entities.User;
import services.ICrud;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserService implements ICrud<User> {
    private Connection connection;

    public UserService() {
        connection = MyDataBase.getInstance().getConnection();

    }

    @Override
    public void add(User el) throws SQLException {
        String req = "INSERT INTO user (email, roles, password, name, brochure_filename, lastname, date_birth, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = connection.prepareStatement(req);
        Gson gson = new Gson();
        String roles = gson.toJson(el.getRole());
        st.setString(1, el.getEmail());
        st.setString(2, roles);
        st.setString(3, "0");
        st.setString(4, el.getName());
        st.setString(5, el.getBrochure_filename());
        st.setString(6, el.getLastName());
        st.setString(7, el.getBirth_date().toString());
        st.setString(8, el.getGender());

        // Execute the prepared statement
        st.executeUpdate();
        System.out.println("Added Successfully");
    }


    @Override
    public void update(User el) throws SQLException {

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
    public ArrayList<User> showAll() throws SQLException {
        return null;
    }
}
