package services.user;

import entities.Patient;
import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    UserService userService = new UserService();
    el.getUser().setRole(new String[] { "ROLE_USER", "ROLE_PATIENT" });
    userService.add(el.getUser());
    int id = getLastInsertedUserIdFromDatabase();
    if (id != -1) {
      String req =
        "INSERT INTO patient (user_id,cas_med,n_cnam,assurance,num_assurance) VALUES (?, ?, ?, ?,?)";
      PreparedStatement st = connection.prepareStatement(req);
      st.setInt(1, id);
      st.setString(2, el.getCas_med());
      st.setInt(3, el.getN_cnam());
      st.setString(4, el.getAssurance());
      st.setInt(5, el.getNum_assurance());
      st.executeUpdate();
      System.out.println("Added Successfully");
    } else System.out.println("User Not Found !");
  }

  @Override
  public void update(Patient el, int id) throws SQLException {}

  @Override
  public void delete(int id) throws SQLException {
    User user = null;
    user = findUserById(id);
    user.setRole(new String[] { "ROLE_USER" });
    UserService service = new UserService();
    service.update(user, user.getUser_id());
    String req = "DELETE FROM patient WHERE id = ?";
    PreparedStatement ps = connection.prepareStatement(req);
    ps.setInt(1, id);
    ps.executeUpdate();
    System.out.println("Deleted Successfully");
  }

  @Override
  public ArrayList<Patient> showAll() throws SQLException {
    return null;
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
