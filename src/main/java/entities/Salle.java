package entities;

public class Salle {

    private  int id ;
    private String num_salle;
    private String num_dep;
    private String etat_salle;
    private String type_salle;



    public Salle(int id, String num_salle, String num_dep, String etat_salle, String type_salle) {
        this.id = id;
        this.num_salle = num_salle;
        this.num_dep = num_dep;
        this.etat_salle = etat_salle;
        this.type_salle = type_salle;
    }

    public Salle( String num_salle, String num_dep, String etat_salle, String type_salle) {
        this.num_salle = num_salle;
        this.num_dep = num_dep;
        this.etat_salle = etat_salle;
        this.type_salle = type_salle;
    }

    public Salle()
  {}
    public Salle(Salle s )
    {
        this.id = s.id;
        this.num_salle = s.num_salle;
        this.num_dep = s.num_dep;
        this.etat_salle = s.etat_salle;
        this.type_salle = s.type_salle;
    }






    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum_salle() {
        return num_salle;
    }

    public void setNum_salle(String num_salle) {
        this.num_salle = num_salle;
    }

    public String getNum_dep() {
        return num_dep;
    }

    public void setNum_dep(String num_dep) {
        this.num_dep = num_dep;
    }

    public String getEtat_salle() {
        return etat_salle;
    }

    public void setEtat_salle(String etat_salle) {
        this.etat_salle = etat_salle;
    }

    public String getType_salle() {
        return type_salle;
    }

    public void setType_salle(String type_salle) {
        this.type_salle = type_salle;
    }

    @Override
    public String toString() {
        return "Salle{" +
                "num_salle='" + num_salle + '\'' +
                '}';
    }
}
