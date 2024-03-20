package entities;
public class Doctor {
    private int id;
    private User user;
    private String matricule;

    public Doctor() {
    }

    public Doctor(User user,String matricule) {
        this.matricule = matricule;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
                "id=" + id +
                ", user=" + user +
                ", matricule='" + matricule + '\'' +
                '}';
    }
}
