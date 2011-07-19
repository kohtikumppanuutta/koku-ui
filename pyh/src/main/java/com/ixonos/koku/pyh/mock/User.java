package com.ixonos.koku.pyh.mock;


public class User {
  
  private String role;
  private String ssn;
  //private String email;
  
  public User(String ssn, String role) {
    //this.email = email;
    
    this.ssn = ssn;
    this.role = role;
  }
  
  public String getSsn() {
    return ssn;
  }
  
  public String getRole() {
    return role;
  }
  
//  public String getEmail() {
//    return email;
//  }
//  
//  public void setEmail(String email) {
//    this.email = email;
//  }
  
}
