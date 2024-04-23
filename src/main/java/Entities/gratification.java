package Entities;

import java.util.Date;

public class gratification {

    private int id,id_donateur_id,montant;

    private donateur Donor ;

    private String titre_grat,desc_grat,type_grat;

    private Date date_grat;


    public gratification(){

    }

    public donateur getDonor() {
        return Donor;
    }

    public void setDonor(donateur donor) {
        Donor = donor;
    }

    public gratification(int montant, String titre_grat, String desc_grat, String type_grat) {
        //this.id = id;
        //this.id_donateur_id = id_donateur_id;
        this.montant = montant;
        this.titre_grat = titre_grat;
        this.desc_grat = desc_grat;
        this.type_grat = type_grat;
        //this.type_machine = type_machine;
    }


    public Date getDate_grat() {
        return date_grat;
    }

    public void setDate_grat(Date date_grat) {
        this.date_grat = date_grat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_donateur_id() {
        return id_donateur_id;
    }

    public void setId_donateur_id(int id_donateur_id) {
        this.id_donateur_id = id_donateur_id;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public String getTitre_grat() {
        return titre_grat;
    }

    public void setTitre_grat(String titre_grat) {
        this.titre_grat = titre_grat;
    }

    public String getDesc_grat() {
        return desc_grat;
    }

    public void setDesc_grat(String desc_grat) {
        this.desc_grat = desc_grat;
    }

    public String getType_grat() {
        return type_grat;
    }

    public void setType_grat(String type_grat) {
        this.type_grat = type_grat;
    }

    /*
    public String getType_machine() {
        return type_machine;
    }

    public void setType_machine(String type_machine) {
        this.type_machine = type_machine;
    }*/

    @Override
    public String toString() {
        return "gratification{" +
                "id=" + id +
                //", id_donateur_id=" + id_donateur_id +
                ", montant=" + montant +
                ", titre_grat='" + titre_grat + '\'' +
                ", desc_grat='" + desc_grat + '\'' +
                ", type_grat='" + type_grat + '\'' +
                //", type_machine='" + type_machine + '\'' +
                '}';
    }
}
