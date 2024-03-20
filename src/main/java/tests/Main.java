package tests;


import entities.*;
import services.diagnostic.ImageService;
import services.diagnostic.ReportService;
import services.user.DoctorService;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class Main {

    public static void main(String[] args) {


        String[] roles = {"Doctor"};
        User userDoctor = new User("docsgfqqfqRFRFfzfr@56example.com", "qflkhk", roles, "hama", "ggfqf", LocalDate.of(1980, 4, 5), "F", "doc_brochure.pdf");
        User userPatient = new User("atiezqfqqfffz55ft@EAEAexample.com", "qkfhjqf", roles, "chrab", "qfqf", LocalDate.of(1980, 4, 5), "F", "doc_brochure.pdf");

        Patient patient = new Patient(userPatient, "terma", 53165, 545564, "termale");
        Doctor doctor = new Doctor(userDoctor, "222");
        Image image = new Image("hhFFff", patient);
        Report report1 = new Report("g", "ggggggggg", doctor, image,new Date() ,false);


        ReportService reportService = new ReportService();

        try {

            reportService.update(report1,16);



        } catch (SQLException e) {
            System.out.println("SQL Exception occurred: " + e.getMessage());
        }


    }
}
