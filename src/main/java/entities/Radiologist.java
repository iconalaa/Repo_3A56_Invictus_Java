package entities;


import java.time.LocalDate;
import java.util.Arrays;

public class Radiologist extends User {
    private String matricule;
    public  Radiologist(){}

    public Radiologist(String mat_nom) {
        this.matricule = mat_nom;
    }

    public Radiologist(String email, String password, String[] role, String name, String lastName, LocalDate birth_date, String gender, String brochure_filename, String mat_nom) {
        super(email, password, role, name, lastName, birth_date, gender, brochure_filename);
        this.matricule = mat_nom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String mat_nom) {
        this.matricule = mat_nom;
    }

    @Override
    public String toString() {
        return "Radiologist{" +
                "matricule='" + matricule + '\'' +
                ", user_id=" + user_id +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + Arrays.toString(role) +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birth_date=" + birth_date +
                ", gender='" + gender + '\'' +
                ", brochure_filename='" + brochure_filename + '\'' +
                '}';
    }
}
