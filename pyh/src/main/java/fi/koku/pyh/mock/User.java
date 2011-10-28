package fi.koku.pyh.mock;

public class User {

  private String role;
  private String ssn;

  public User(String ssn, String role) {
    this.ssn = ssn;
    this.role = role;
  }

  public String getSsn() {
    return ssn;
  }

  public String getRole() {
    return role;
  }

}
