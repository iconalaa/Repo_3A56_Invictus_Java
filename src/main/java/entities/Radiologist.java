package entities;

import java.time.LocalDate;

public class Radiologist extends User{
    private String mat_nom;
    private boolean dispo;

    public  Radiologist(){}

    public Radiologist(String mat_nom, boolean dispo) {
        this.mat_nom = mat_nom;
        this.dispo = dispo;
    }

    public Radiologist(String email, String[] role, String name, String lastName, LocalDate birth_date, String gender, String brochure_filename, String mat_nom, boolean dispo) {
        super(email, role, name, lastName, birth_date, gender, brochure_filename);
        this.mat_nom = mat_nom;
        this.dispo = dispo;
    }

    public String getMat_nom() {
        return mat_nom;
    }

    public boolean isDispo() {
        return dispo;
    }

    public void setMat_nom(String mat_nom) {
        this.mat_nom = mat_nom;
    }

    public void setDispo(boolean dispo) {
        this.dispo = dispo;
    }
}
