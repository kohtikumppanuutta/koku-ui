package com.ixonos.koku.pyh.mock;

import com.ixonos.koku.pyh.model.Person;

public class User extends Person {
  
  private String role;
  private String email;
  
  public User(String firstname, String middlename, String surname, String ssn, String birthdate, String email, String role) {
    super(firstname, middlename, surname, ssn, birthdate);
    this.email = email;
    this.role = role;
  }
  
  public String getRole() {
    return role;
  }
  
  public void setRole(String role) {
    this.role = role;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
}
