package entities;
import java.time.LocalDate;
import java.util.Arrays;

public class User {
    protected int user_id;
    protected String password;
    protected String email;
    protected String[] role;
    protected String name;
    protected String lastName;
    protected LocalDate birth_date;
    protected String gender;
    protected String brochure_filename;

    public User() {
    }



    public User(String email,String password, String[] role, String name, String lastName, LocalDate birth_date, String gender, String brochure_filename){
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.lastName = lastName;
        this.birth_date = birth_date;
        this.gender = gender;
        this.brochure_filename = brochure_filename;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public LocalDate getBirth_date() {
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

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBrochure_filename(String brochure_filename) {
        this.brochure_filename = brochure_filename;
    }

    @Override
    public String toString() {
        return
                name ;

    }
}
