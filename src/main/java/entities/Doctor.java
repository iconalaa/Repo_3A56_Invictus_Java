package entities;

import java.time.LocalDate;

public class Doctor {
    private User user;
    private String matricule;

    public Doctor() {
    }

    public Doctor(User user,String matricule) {
        this.matricule = matricule;
        this.user = user;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "user=" + user.getEmail() +
                ", matricule='" + matricule + '\'' +
                '}';
    }
}
