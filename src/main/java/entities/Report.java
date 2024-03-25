package entities;

import java.util.Date;

public class Report {
    private int id;
    private String interpretation_med;
    private String interpretation_rad;
    private Doctor doctor;
    private Image image;
    private Date date;
    private boolean is_edited;

    public Report(){};

    public Report( String interpretation_med, String interpretation_rad, Doctor doctor, Image image, boolean is_edited) {
        this.interpretation_med = interpretation_med;
        this.interpretation_rad = interpretation_rad;
        this.doctor = doctor;
        this.image = image;
        this.is_edited = is_edited;
    }

    public Report(String interpretation_med, String interpretation_rad, Doctor doctor, Image image, Date date, boolean is_edited) {
        this.interpretation_med = interpretation_med;
        this.interpretation_rad = interpretation_rad;
        this.doctor = doctor;
        this.image = image;
        this.date = date;
        this.is_edited = is_edited;
    }

    public Report(int id, String interpretation_med, String interpretation_rad, Doctor doctor, Image image, Date date, boolean is_edited) {
        this.id = id;
        this.interpretation_med = interpretation_med;
        this.interpretation_rad = interpretation_rad;
        this.doctor = doctor;
        this.image = image;
        this.date = date;
        this.is_edited = is_edited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInterpretation_med() {
        return interpretation_med;
    }

    public void setInterpretation_med(String interpretation_med) {
        this.interpretation_med = interpretation_med;
    }

    public String getInterpretation_rad() {
        return interpretation_rad;
    }

    public void setInterpretation_rad(String interpretation_rad) {
        this.interpretation_rad = interpretation_rad;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isIs_edited() {
        return is_edited;
    }

    public void setIs_edited(boolean is_edited) {
        this.is_edited = is_edited;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", interpretation_med='" + interpretation_med + '\'' +
                ", interpretation_rad='" + interpretation_rad + '\'' +
                ", doctor=" + doctor +
                ", image=" + image +
                ", date=" + date +
                ", is_edited=" + is_edited +
                '}';
    }
}
