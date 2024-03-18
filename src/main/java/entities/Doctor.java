package entities;

public class Doctor extends User{
    private String matricule;

    public  Doctor(){}

    public Doctor(String matricule) {
        this.matricule = matricule;
    }

    public Doctor(String email, String[] role, String name, String lastName, String birth_date, String gender, String brochure_filename, String matricule) {
        super(email, role, name, lastName, birth_date, gender, brochure_filename);
        this.matricule = matricule;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}
