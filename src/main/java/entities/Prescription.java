package entities;

import java.util.Date;

public class Prescription {
    private Report report;
    private int id;
    private String contenu;
    private Date date;
    private String signature_filename;

    public Prescription( String contenu, String signature_filename) {
        this.contenu = contenu;
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

    @Override
    public String toString() {
        return "Prescription{" +
                "report=" + report +
                ", id=" + id +
                ", contenu='" + contenu + '\'' +
                ", date=" + date +
                ", signature_filename='" + signature_filename + '\'' +
                '}';
    }
}
