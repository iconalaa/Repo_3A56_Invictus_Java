package tests;

import entities.Radiologist;
import entities.User;
import services.user.RadiologistService;
import services.user.UserService;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocalDate date = LocalDate.parse("2018-05-05");
        User u1 = new User("warum@es.com", "23658", new String[]{"ROLE_USER"}, "samir", "errouj", date, "male", "x");
        Radiologist r1 = new Radiologist(u1, "22ddd", false);
        RadiologistService service = new RadiologistService();

        try {
            service.add(r1);
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }

    }
