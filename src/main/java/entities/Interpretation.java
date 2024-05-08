package entities;

import java.time.LocalDate;

public class Interpretation {
private  int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Image image;
    private User Radiologist;

    private String interpretation;
private LocalDate sendAt ;

private String urgency;

private  String descriptin;

    public LocalDate getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDate sendAt) {
        this.sendAt = sendAt;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getDescriptin() {
        return descriptin;
    }

    public void setDescriptin(String descriptin) {
        this.descriptin = descriptin;
    }

    public Interpretation(Image image, User radiologist, String interpretation) {
        this.image = image;
        Radiologist = radiologist;
        this.interpretation = interpretation;
        this.sendAt = LocalDate.now();
        this.descriptin="ddddd";
        this.urgency="grave";
    }
    public Interpretation()
    {
        this.sendAt = LocalDate.now();
        this.descriptin="ddddd";
        this.urgency="grave";

    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public User getRadiologist() {
        return Radiologist;
    }

    public void setRadiologist(User radiologist) {
        Radiologist = radiologist;
    }

    public String getInterpretation() {
        return interpretation;
    }

    @Override
    public String toString() {
        return "Interpretation{" +
                "id=" + id +
                ", image=" + image +
                ", Radiologist=" + Radiologist +
                ", interpretation='" + interpretation + '\'' +
                '}';
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }


}
