package entities;

public class Image {
    private int id;
    private String filename;
    private Patient patient;
    private Radiologist radiologist;

    public Radiologist getRadiologist() {
        return radiologist;
    }

    public void setRadiologist(Radiologist radiologist) {
        this.radiologist = radiologist;
    }

    public Image(String filename,Patient patient) {
        this.filename = filename;
        this.patient = patient;
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



    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
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
