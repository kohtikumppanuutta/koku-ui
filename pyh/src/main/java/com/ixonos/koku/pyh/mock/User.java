package com.ixonos.koku.pyh.mock;

public class User {
  
  private String role;
  private String firstname;
  private String surname;
  private String email;
  
  public User() {
    
  }
  
  public User(String role, String firstname, String surname, String email) {
    this.role = role;
    this.firstname = firstname;
    this.surname = surname;
    this.email = email;
  }
  
  public String getRole() {
    return role;
  }
  
  public void setRole(String role) {
    this.role = role;
  }
  
  public String getFirstname() {
    return firstname;
  }
  
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  
  public String getSurname() {
    return surname;
  }
  
  public void setSurname(String surname) {
    this.surname = surname;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
}
