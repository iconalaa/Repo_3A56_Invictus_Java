package entities;

import java.util.Date;

public class Prescription {
    private Report report;
    private int id;
    private String contenu;
    private Date date;
    private String signature_filename;

    public Prescription(Report report, String contenu, Date date, String signature_filename) {
        this.report = report;
        this.contenu = contenu;
        this.date = date;
        this.signature_filename = signature_filename;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSignature_filename() {
        return signature_filename;
    }

    public void setSignature_filename(String signature_filename) {
        this.signature_filename = signature_filename;
    }
}
