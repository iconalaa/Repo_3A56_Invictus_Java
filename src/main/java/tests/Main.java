package tests;

import entities.User;
import services.user.UserService;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        User user = new User("example@email.com", "password123", new String[]{"admin"}, "John", "Doe", LocalDate.of(1990, 5, 15), "Male", "brochure.pdf");
        UserService ps = new UserService();
        try {
            ps.update(user, 51);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

