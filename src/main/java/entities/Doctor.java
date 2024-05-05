package entities;

import java.time.LocalDate;
import java.util.Arrays;

public class Doctor extends User {
    private String matricule;
    private String confirmationToken;

    // Constructor, getters, setters, and other methods...

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }
    public Doctor() {
    }

    @Override
    public String toString() {
        return "Doctor{" +
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

    public Doctor(String email, String password, String[] role, String name, String lastName, LocalDate birth_date, String gender, String brochure_filename, String matricule) {
        super(email, password, role, name, lastName, birth_date, gender, brochure_filename);
        this.matricule = matricule;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}
