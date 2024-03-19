package entities;


public class Radiologist {
    private User user;
    private String mat_nom;
    private boolean dispo;

    public  Radiologist(){}

    public Radiologist(User user,String mat_nom, boolean dispo) {
        this.user = user;
        this.mat_nom = mat_nom;
        this.dispo = dispo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
