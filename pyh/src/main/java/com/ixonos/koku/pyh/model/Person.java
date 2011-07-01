package com.ixonos.koku.pyh.model;

public class Person {
  
  private String firstname;
  private String middlename;
  private String surname;
  private String ssn;
  
  public Person() {
    this.firstname = "";
    this.middlename = "";
    this.surname = "";
    this.ssn = "";
  }
  
  public Person(String firstname, String middlename, String surname, String ssn) {
    this.firstname = firstname;
    this.middlename = middlename;
    this.surname = surname;
    this.ssn = ssn;
  }
  
  public String getFirstname() {
    return firstname;
  }
  
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  
  public String getMiddlename() {
    return middlename;
  }
  
  public void setMiddlename(String middlename) {
    this.middlename = middlename;
  }
  
  public String getSurname() {
    return surname;
  }
  
  public void setSurname(String surname) {
    this.surname = surname;
  }
  
  public String getSsn() {
    return ssn;
  }
  
  public void setSsn(String ssn) {
    this.ssn = ssn;
  }
}
