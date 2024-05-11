package entities;

import java.time.LocalDate;

public class RendezVous {



    private  int id;
    private Salle salle;
   private LocalDate date_rv;
   private String type_exam;
    private User user_id;



    public RendezVous(int id, Salle salle, LocalDate date_rv, String type_exam, User user_id) {
        this.id = id;
        this.salle = salle;
        this.date_rv = date_rv;
        this.type_exam = type_exam;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", salle=" + salle +
                ", date_rv=" + date_rv +
                ", type_exam='" + type_exam + '\'' +
                '}';
    }

    public RendezVous()
    {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public LocalDate getDate_rv() {
        return date_rv;
    }

    public void setDate_rv(LocalDate date_rv) {
        this.date_rv = date_rv;
    }

    public String getType_exam() {
        return type_exam;
    }

    public void setType_exam(String type_exam) {
        this.type_exam = type_exam;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }
}
