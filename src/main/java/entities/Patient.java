package entities;

import java.time.LocalDate;
import java.util.Arrays;

public class Patient extends User {
  private String cas_med;
  private int n_cnam;
  private int num_assurance;
  private String assurance;

  public Patient(){}
  public Patient(String email, String password, String[] role, String name, String lastName, LocalDate birth_date, String gender, String brochure_filename, String cas_med, int n_cnam, int num_assurance, String assurance) {
    super(email, password, role, name, lastName, birth_date, gender, brochure_filename);
    this.cas_med = cas_med;
    this.n_cnam = n_cnam;
    this.num_assurance = num_assurance;
    this.assurance = assurance;
  }

  public String getCas_med() {
    return cas_med;
  }

  public void setCas_med(String cas_med) {
    this.cas_med = cas_med;
  }

  public int getN_cnam() {
    return n_cnam;
  }

  public void setN_cnam(int n_cnam) {
    this.n_cnam = n_cnam;
  }

  public int getNum_assurance() {
    return num_assurance;
  }

  public void setNum_assurance(int num_assurance) {
    this.num_assurance = num_assurance;
  }

  public String getAssurance() {
    return assurance;
  }

  public void setAssurance(String assurance) {
    this.assurance = assurance;
  }

  @Override
  public String toString() {
    return "Patient{" +
            "cas_med='" + cas_med + '\'' +
            ", n_cnam=" + n_cnam +
            ", num_assurance=" + num_assurance +
            ", assurance='" + assurance + '\'' +
            ", user_id=" + user_id +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", role=" + Arrays.toString(role) +
            ", name='" + name + '\'' +
            ", lastName='" + lastName + '\'' +
            ", birth_date=" + birth_date +
            ", gender='" + gender + '\'' +
            ", brochure_filename='" + brochure_filename + '\'' +
            '}';
  }
}
