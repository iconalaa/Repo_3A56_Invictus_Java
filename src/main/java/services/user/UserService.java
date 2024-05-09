package services.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import entities.User;
import services.ICrud;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
    public void update(User el, int id) throws SQLException {
        String req = "UPDATE user SET name = ?,  lastname = ? , email = ? , password = ? , brochure_filename = ?,date_birth = ?,gender = ?, roles = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            Gson gson = new Gson();
            String s = el.getRole()[0].substring(2, el.getRole()[0].length() - 2);
            String role = gson.toJson(new String[]{s});
            ps.setString(1, el.getName());
            ps.setString(2, el.getLastName());
            ps.setString(3, el.getEmail());
            ps.setString(4, el.getPassword());
            ps.setString(5, el.getBrochure_filename());
            ps.setString(6, el.getBirth_date().toString());
            ps.setString(7, el.getGender());
            ps.setString(8, role);
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
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
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

    public User getUserById(int id) {
        String sql = "SELECT * FROM USER WHERE id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getInt("id"));
                user.setRole(rs.getString("roles").split(","));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setGender(rs.getString("gender"));
                user.setPassword(rs.getString("password"));
                user.setLastName(rs.getString("lastname"));
                user.setBirth_date(rs.getDate("date_birth").toLocalDate());
                user.setBrochure_filename(rs.getString("brochure_filename"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM USER WHERE email = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getInt("id"));
                user.setRole(rs.getString("roles").split(","));
                user.setName(rs.getString("name"));
                user.setGender(rs.getString("gender"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setLastName(rs.getString("lastname"));
                user.setBirth_date(rs.getDate("date_birth").toLocalDate());
                user.setBrochure_filename(rs.getString("brochure_filename"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countUsers(String role) throws SQLException {
        int count = 0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT COUNT(*) AS user_count FROM user WHERE JSON_CONTAINS(roles, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "\"" + role + "\"");
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("user_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String matriculeUser(String email) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT matricule FROM user WHERE email=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("matricule");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    public List<User> getToApproveUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql = "SELECT * FROM user WHERE JSON_CONTAINS(roles, ?) OR JSON_CONTAINS(roles, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "\"ROLE_WAITING_DOCTOR\"");
            statement.setString(2, "\"ROLE_WAITING_RADIOLOGIST\"");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Extracting roles as a string and then converting it into a string array
                String rolesString = resultSet.getString("roles");
                String[] roles = rolesString.split(", "); // Assuming roles are stored as comma-separated values

                // Creating a new User object using the provided constructor
                User user = new User(
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        roles,
                        resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getDate("date_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getString("brochure_filename")
                );
                user.setUser_id(resultSet.getInt("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close statement and result set in finally block
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return users;
    }

    public User findUserById(int userId) throws SQLException {



        ArrayList<User> users = new ArrayList<>();
        String req = "SELECT * FROM user where id = "+userId;
        Statement st = connection.createStatement();
        ResultSet rs =  st.executeQuery(req);
        User user = new User();

        while (rs.next()){
            user.setBirth_date(rs.getDate("date_birth").toLocalDate());
            user.setUser_id(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setGender(rs.getString("gender"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setLastName(rs.getString("lastname"));
            String[] x = new String[]{rs.getString("roles")};
            user.setRole(x);
            break;
        }
        return user;


    }

    // Retrieve all patients from the database
    public ArrayList<User> showAllp() throws SQLException {
        ArrayList<User> patients = new ArrayList<>();
        String query = "SELECT * FROM user WHERE roles = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "[\"ROLE_PATIENT\"]");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Retrieve patient data from the ResultSet and create a Patient object

                    // Assuming Patient has a User object
                    // Populate other attributes of the patient object as per your database schema

                    //retriving user information
                    User user = new User();
                    user.setUser_id(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setLastName(rs.getString("lastName"));
                    user.setBirth_date(rs.getDate("date_birth").toLocalDate());
                    user.setGender(rs.getString("gender"));
                    // Associate the user with the patient
                    // Add the patient object to the list
                    patients.add(user);
                }
            }
        }
        System.out.println("liste des patient {"+patients+"}}}}}");
        return patients;
    }

    public ArrayList<User> showAlld() throws SQLException {
        ArrayList<User> patients = new ArrayList<>();
        String query = "SELECT * FROM user WHERE roles = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "[\"ROLE_DOCTOR\"]");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Retrieve patient data from the ResultSet and create a Patient object

                    // Assuming Patient has a User object
                    // Populate other attributes of the patient object as per your database schema

                    //retriving user information
                    User user = new User();
                    user.setUser_id(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setLastName(rs.getString("lastName"));
                    user.setBirth_date(rs.getDate("date_birth").toLocalDate());
                    user.setGender(rs.getString("gender"));
                    // Associate the user with the patient
                    // Add the patient object to the list
                    patients.add(user);
                }
            }
        }
        System.out.println("liste des patient {"+patients+"}}}}}");
        return patients;
    }
}
