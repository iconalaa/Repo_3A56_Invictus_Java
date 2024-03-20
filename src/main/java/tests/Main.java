package tests;


import entities.*;
import services.diagnostic.ImageService;
import services.diagnostic.ReportService;
import services.user.DoctorService;


import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {


        String[] roles = {"Doctor"};
        User userDoctor = new User("docsgFfqqfqRFRFfzfr@56example.com", "securepassword", roles, "Jane", "Doe", LocalDate.of(1980, 4, 5), "F", "doc_brochure.pdf");
        User userPatient = new User("atieFzqfqqfffz55ft@EAEAexample.com", "securepassword", roles, "Jane", "Doe", LocalDate.of(1980, 4, 5), "F", "doc_brochure.pdf");

        Patient patient = new Patient(userPatient, "terma", 53165, 545564, "termale");
        Doctor doctor = new Doctor(userDoctor, "222");
        Image image = new Image("hhFFff", patient);
        Report report = new Report("ff", "fff", doctor, image, true);


        ReportService reportService = new ReportService();
        DoctorService doctorService = new DoctorService();
        ImageService imageService = new ImageService();

        try {
            doctorService.add(doctor);
            imageService.addImage(image);
            reportService.add(report, doctor, image);
            System.out.println("Report added successfully to the database.");
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred: " + e.getMessage());
        }


    }
}
