package services.user;
import com.google.gson.Gson;
import entities.User;
import services.ICrud;
import utils.MyDataBase;
import java.sql.*;
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
        st.setString(3, el.getPassword());
        st.setString(4, el.getName());
        st.setString(5, el.getBrochure_filename());
        st.setString(6, el.getLastName());
        st.setString(7, el.getBirth_date().toString());
        st.setString(8, el.getGender());

        st.executeUpdate();
        System.out.println("Added Successfully");
    }


    @Override
    public void update(User el,int id) throws SQLException {
        String req = "UPDATE user SET name = ?,  lastname = ? , email = ? , password = ? , brochure_filename = ?,date_birth = ?,gender = ?, roles = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            Gson gson = new Gson();
            String roles = gson.toJson(el.getRole());
            ps.setString(1, el.getName());
            ps.setString(2, el.getLastName());
            ps.setString(3, el.getEmail());
            ps.setString(4, el.getPassword());
            ps.setString(5, el.getBrochure_filename());
            ps.setString(6, el.getBirth_date().toString());
            ps.setString(7, el.getGender());
            ps.setString(8, roles);
            ps.setInt(9, id);
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
    public ArrayList<User> showAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String req = "SELECT * FROM user";
        Statement st = connection.createStatement();
        ResultSet rs =  st.executeQuery(req);

        while (rs.next()){
            User user = new User();
            user.setBirth_date(rs.getDate("date_birth").toLocalDate());
            user.setUser_id(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setGender(rs.getString("gender"));
            user.setBrochure_filename(rs.getString("brochure_filename"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setLastName(rs.getString("lastname"));
            String[] x = new String[]{rs.getString("roles")};
            user.setRole(x);
            users.add(user);
        }
        return users;
    }



}
