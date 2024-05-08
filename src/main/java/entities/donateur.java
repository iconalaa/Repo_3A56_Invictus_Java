package entities;

public class donateur {

    private int id,telephone;

    private String nom_donateur,prenom_donateur,email,type_donateur;


    public donateur( int telephone, String nom_donateur, String prenom_donateur, String email, String type_donateur) {
        //this.id = id;
        this.telephone = telephone;
        this.nom_donateur = nom_donateur;
        this.prenom_donateur = prenom_donateur;
        this.email = email;
        this.type_donateur = type_donateur;
    }

    public donateur() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getNom_donateur() {
        return nom_donateur;
    }

    public void setNom_donateur(String nom_donateur) {
        this.nom_donateur = nom_donateur;
    }

    public String getPrenom_donateur() {
        return prenom_donateur;
    }

    public void setPrenom_donateur(String prenom_donateur) {
        this.prenom_donateur = prenom_donateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType_donateur() {
        return type_donateur;
    }

    public void setType_donateur(String type_donateur) {
        this.type_donateur = type_donateur;
    }

    @Override
    public String toString() {
        return "donateur{" +
                //"id=" + id +
                ", telephone=" + telephone +
                ", nom_donateur='" + nom_donateur + '\'' +
                ", prenom_donateur='" + prenom_donateur + '\'' +
                ", email='" + email + '\'' +
                ", type_donateur='" + type_donateur + '\'' +
                '}';
    }
}
