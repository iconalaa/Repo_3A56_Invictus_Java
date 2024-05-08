package entities;

public class Droit {
    private  int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private User radiologist;
    private Image image;

    private String  role;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public User getRadiologist() {
        return radiologist;
    }

    public void setRadiologist(User radiologist) {
        this.radiologist = radiologist;
    }

    public Droit(User radiologist , Image image, String role )
    {
        this.radiologist=radiologist;
        this.image=image;
        this.role=role;
    }


public  Droit()
{}

    @Override

    public String toString()
    {
        return this.radiologist.getName();
    }


}
