package entities;

import java.time.LocalDate; // import the LocalDate class

public class Image {
    private int id;
    private String filename;
    private User patient;
    private User radiologist;
    private String bodyPart;
    private String fileName;
    public LocalDate aquisitionDate;
    public LocalDate dateAjout;


    public Image(int id ,String filename,String bodyPart,LocalDate aquisitionDate)
    {this.id=id;
        this.filename=filename;
        this.bodyPart=bodyPart;
        this.aquisitionDate=aquisitionDate;


    }
    public User getRadiologist() {
        return radiologist;
    }

    public void setRadiologist(User radiologist) {
        this.radiologist = radiologist;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDate getAquisitionDate() {
        return aquisitionDate;
    }

    public void setAquisitionDate(LocalDate aquisitionDate) {
        this.aquisitionDate = aquisitionDate;
    }

    public Image(String filename, User patient, String bodyPart, LocalDate aquisitionDate) {
        this.filename = filename;
        this.patient = patient;
        this.aquisitionDate=aquisitionDate;
        this.filename=filename;
        this.bodyPart=bodyPart;
    }
    public Image() {}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }



    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", patient=" + patient +
                ", radiologist=" + radiologist +
                '}';
    }


}
