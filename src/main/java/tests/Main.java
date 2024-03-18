package tests;
import entities.User;
import services.user.UserService;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocalDate date = LocalDate.parse("2018-05-05");
        User u1 = new User("ayoub@esprit.tn", new String[]{"ROLE_USER"}, "ayoub", "dahmen",date, "male", "x");
        UserService service = new UserService();
        try {
            System.out.println(service.showAll());
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }


    }
}