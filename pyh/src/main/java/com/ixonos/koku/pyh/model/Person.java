package com.ixonos.koku.pyh.model;

public class Person {
  
  private String firstname;
  private String middlename;
  private String surname;
  private String ssn;
  private String birthdate; // e.g. "13-05-2000"
  private String birthday; // e.g. "13"
  private String birthmonth; // e.g. "05"
  private String birthyear; // e.g. "2000"
  private String email;
  
  public Person() {
    this.firstname = "";
    this.middlename = "";
    this.surname = "";
    this.ssn = "";
    this.birthdate = "";
    this.birthday = "";
    this.birthmonth = "";
    this.birthyear = "";
    this.email = "";
  }
  
  public Person(String firstname, String middlename, String surname, String ssn, String birthdate, String email) {
    this.firstname = firstname;
    this.middlename = middlename;
    this.surname = surname;
    this.ssn = ssn;
    this.birthdate = birthdate;
    this.email = email;
  }
  
  public Person(Person p) {
    this.firstname = p.getFirstname();
    this.middlename = p.getMiddlename();
    this.surname = p.getSurname();
    this.ssn = p.getSsn();
    this.birthdate = p.getBirthdate();
    this.email = p.getEmail();
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
  
  public String getBirthdate() {
    return birthdate;
  }
  
  public void setBirthdate(String birthdate) {
    this.birthdate = birthdate;
  }
  
  public void setBirthdate(String day, String month, String year) {
    this.birthdate = day + "-" + month + "-" + year;
  }
  
  public String getBirthday() {
    return birthday;
  }
  
  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }
  
  public String getBirthmonth() {
    return birthmonth;
  }
  
  public void setBirthmonth(String birthmonth) {
    this.birthmonth = birthmonth;
  }
  
  public String getBirthyear() {
    return birthyear;
  }
  
  public void setBirthyear(String birthyear) {
    this.birthyear = birthyear;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
}
