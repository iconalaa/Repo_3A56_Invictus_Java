package services.user;

import entities.Doctor;
import entities.User;
import java.sql.*;
import java.util.ArrayList;
import services.ICrud;
import utils.MyDataBase;

public class DoctorService implements ICrud<Doctor> {

  private Connection connection;

  public DoctorService() {
    connection = MyDataBase.getInstance().getConnection();
  }

  @Override
  public void add(Doctor el) throws SQLException {
    UserService userService = new UserService();
    el.getUser().setRole(new String[] { "ROLE_USER", "ROLE_DOCTOR" });
    userService.add(el.getUser());
    int id = getLastInsertedUserIdFromDatabase();
    if (id != -1) {
      String req = "INSERT INTO doctor (user_id,matricule) VALUES (?, ?)";
      PreparedStatement st = connection.prepareStatement(req);
      st.setInt(1, id);
      st.setString(2, el.getMatricule());
      st.executeUpdate();
      System.out.println("Added Successfully");
    } else System.out.println("User Not Found !");
  }

  @Override
  public void update(Doctor el, int id) throws SQLException {
    String req = "UPDATE doctor SET matricule = ? WHERE id = ?";
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
    User user = null;
    user = findUserById(id);
    user.setRole(new String[] { "ROLE_USER" });
    UserService service = new UserService();
    service.update(user, user.getUser_id());
    String req = "DELETE FROM doctor WHERE id = ?";
    PreparedStatement ps = connection.prepareStatement(req);
    ps.setInt(1, id);
    ps.executeUpdate();
    System.out.println("Deleted Successfully");
  }

  @Override
  public ArrayList<Doctor> showAll() throws SQLException {
    ArrayList<Doctor> doctor = new ArrayList<>();
    String req = "SELECT * FROM doctor";
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery(req);

    while (rs.next()) {
      Doctor rad = new Doctor();
      rad.setMatricule(rs.getString("matricule"));
      doctor.add(rad);
    }
    return doctor;
  }

  public User findUserById(int id) {
    User user = null;
    String query =
      "SELECT * FROM user u, doctor d WHERE u.id = d.user_id AND d.id = ?";

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
  public int getDoctorIdByMatricule(String matricule) throws SQLException {
    String sql = "SELECT id FROM doctor WHERE matricule = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, matricule);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt("id");
        } else {
          throw new SQLException("Doctor not found for matricule: " + matricule);
        }
      }
    }
  }

}
