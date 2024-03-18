package entities;

public class User {
    protected int user_id;
    protected String email;
    protected String[] role;
    protected String name;
    protected String lastName;
    protected String birth_date;
    protected String gender;
    protected String brochure_filename;

    public  User(){}
    public User(String email, String[] role, String name, String lastName, String birth_date, String gender, String brochure_filename) {
        this.email = email;
        this.role = role;
        this.name = name;
        this.lastName = lastName;
        this.birth_date = birth_date;
        this.gender = gender;
        this.brochure_filename = brochure_filename;
    }

    public int getId() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public String[] getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getGender() {
        return gender;
    }

    public String getBrochure_filename() {
        return brochure_filename;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String[] role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBrochure_filename(String brochure_filename) {
        this.brochure_filename = brochure_filename;
    }
}
