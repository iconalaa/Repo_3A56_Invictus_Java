package entities;

public class Patient {

  private User user;
  private String cas_med;
  private int n_cnam;
  private int num_assurance;
  private String assurance;

  public Patient() {}

  public Patient(
    User user,
    String cas_med,
    int n_cnam,
    int num_assurance,
    String assurance
  ) {
    this.user = user;
    this.cas_med = cas_med;
    this.n_cnam = n_cnam;
    this.num_assurance = num_assurance;
    this.assurance = assurance;
  }

  public String getCas_med() {
    return cas_med;
  }

  public String getAssurance() {
    return assurance;
  }

  public int getN_cnam() {
    return n_cnam;
  }

  public int getNum_assurance() {
    return num_assurance;
  }

  public void setCas_med(String cas_med) {
    this.cas_med = cas_med;
  }

  public void setN_cnam(int n_cnam) {
    this.n_cnam = n_cnam;
  }

  public void setNum_assurance(int num_assurance) {
    this.num_assurance = num_assurance;
  }

  public void setAssurance(String assurance) {
    this.assurance = assurance;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return (
      "Patient{" +
      "cas_med='" +
      cas_med +
      '\'' +
      ", n_cnam=" +
      n_cnam +
      ", num_assurance=" +
      num_assurance +
      ", assurance='" +
      assurance +
      '\'' +
      '}'
    );
  }
}
