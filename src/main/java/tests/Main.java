package tests;

import entities.Doctor;
import entities.Radiologist;
import entities.User;
import services.user.DoctorService;
import services.user.RadiologistService;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocalDate date = LocalDate.parse("2018-05-05");
        User u1 = new User("smayer@es.com", "23658", new String[]{"ROLE_USER"}, "samir", "errouj", date, "male", "x");
        User u2 = new User("JOhn@cena.com", "23658", new String[]{"ROLE_USER"}, "samir", "errouj", date, "male", "x");
        User u3 = new User("leila@wuj.com", "23658", new String[]{"ROLE_USER"}, "samir", "errouj", date, "male", "x");
   
        Doctor d2 = new Doctor(u2,"Lotfi55WUUJ");
  
        DoctorService service = new DoctorService();
        
        try {
            service.update(d2, 3);
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }

    }
}
