package services.user;

import com.google.gson.Gson;
import entities.Doctor;
import entities.Patient;
import entities.User;

import java.sql.*;
import java.util.ArrayList;
import services.ICrud;
import utils.MyDataBase;

public class PatientService implements ICrud<Patient> {

  private Connection connection;

  public PatientService() {
    connection = MyDataBase.getInstance().getConnection();
  }

  @Override
  public void add(Patient el) throws SQLException {
    el.setRole(new String[]{"ROLE_USER", "ROLE_PATIENT"});
    String req = "INSERT INTO user (email, roles, password, name, brochure_filename, lastname, date_birth, gender,cas_med,n_cnam,assurance,num_assurance) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
    st.setString(9, el.getCas_med());
    st.setInt(10, el.getN_cnam());
    st.setString(11, el.getAssurance());
    st.setInt(12, el.getNum_assurance());
    st.executeUpdate();
    System.out.println("Added Successfully");
  }

  @Override
  public void update(Patient el, int id) throws SQLException {
    UserService service = new UserService();
    service.update(el, id);
    String req = "UPDATE user SET cas_med = ?, n_cnam = ?, assurance = ?, num_assurance = ? WHERE id = ?";
    try {
      PreparedStatement ps = connection.prepareStatement(req);
      ps.setString(1, el.getCas_med());
      ps.setInt(2, el.getN_cnam());
      ps.setString(3, el.getAssurance());
      ps.setInt(4, el.getNum_assurance());
      ps.setInt(5, id);
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
  public ArrayList<Patient> showAll() throws SQLException {
    ArrayList<Patient> patient = new ArrayList<>();
    String req = "SELECT * FROM user";
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery(req);
    while (rs.next()) {
      Patient rad = new Patient();
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
      rad.setAssurance(rs.getString("assurance"));
      rad.setCas_med(rs.getString("cas_med"));
      rad.setN_cnam(rs.getInt("n_cnam"));
      rad.setNum_assurance(rs.getInt("num_assurance"));
      patient.add(rad);
    }
    return patient;
  }

  public User findUserById(int id) {
    User user = null;
    String query =
      "SELECT * FROM user u, patient p WHERE u.id = p.user_id AND p.id = ?";

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
          String[] x = new String[] { rs.getString("roles") };
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
