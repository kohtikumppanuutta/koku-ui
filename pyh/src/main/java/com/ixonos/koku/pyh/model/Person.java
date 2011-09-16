package com.ixonos.koku.pyh.model;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.ElectronicContactInfoType;
import fi.koku.services.entity.customer.v1.ElectronicContactInfosType;

public class Person {

//  private String firstname;
//  private String middlename;
//  private String surname;
//  private String ssn;
//  private Calendar birthdayCal;
//  private String birthdate; // e.g. "13-05-2000"
//  private String birthday; // e.g. "13"
//  private String birthmonth; // e.g. "05"
//  private String birthyear; // e.g. "2000"
//  private String email; // TODO: replace with eContactInfo
//  private String eContactInfo;
  private boolean requestPending;
  
  private CustomerType customer;
  
  public Person(CustomerType customer) {
    this.customer = customer;
  }
  
  public String getFirstname() {
    //return firstname;
    return customer.getEtuNimi();
  }

//  public void setFirstname(String firstname) {
//    this.firstname = firstname;
//  }

//  public String getMiddlename() {
//    return middlename;
//  }

//  public void setMiddlename(String middlename) {
//    this.middlename = middlename;
//  }

  public String getSurname() {
    //return surname;
    return customer.getSukuNimi();
  }

//  public void setSurname(String surname) {
//    this.surname = surname;
//  }

  public String getPic() {
    //return ssn;
    return customer.getHenkiloTunnus();
  }

//  public void setSsn(String ssn) {
//    this.ssn = ssn;
//  }
  
  public String getBirthdate() {
    //return birthdate;
    return customer.getSyntymaPvm().toString();
  }
  
  public Calendar getBirthdayAsCalendar() {
    //return birthdayCal;
    return customer.getSyntymaPvm();
  }
  
//  public void setBirthdate(String birthdate) {
//    this.birthdate = birthdate;
//  }

//  public void setBirthdate(String day, String month, String year) {
//    this.birthdate = day + "-" + month + "-" + year;
//  }

//  public void setBirthdayCalendar(Calendar cal) {
//    this.birthdayCal = cal;
//  }
  
//  public String getBirthday() {
//    return birthday;
//  }

//  public void setBirthday(String birthday) {
//    this.birthday = birthday;
//  }

//  public String getBirthmonth() {
//    return birthmonth;
//  }

//  public void setBirthmonth(String birthmonth) {
//    this.birthmonth = birthmonth;
//  }

//  public String getBirthyear() {
//    return birthyear;
//  }

//  public void setBirthyear(String birthyear) {
//    this.birthyear = birthyear;
//  }

//  public String getEmail() {
//    return email;
//  }
  
  public String getEContactInfo() {
    ElectronicContactInfosType contactInfoType = customer.getElectronicContactInfos();
    List<ElectronicContactInfoType> contactInfos = contactInfoType.getEContactInfo();
    Iterator<ElectronicContactInfoType> cii = contactInfos.iterator();
    String electronicContactInfo = "";
    while (cii.hasNext()) {
      ElectronicContactInfoType contactInfo = cii.next();
      String info = contactInfo.getContactInfoType() + ": " + contactInfo.getContactInfo();
      electronicContactInfo += info + "\n";
    }
    return electronicContactInfo;
  }
  
  public String getFullName() {
    return getFirstname() + " " + getSurname();
  }

  public String getCapFullName() {
    return (getFirstname() + " " + getSurname() + " " + getPic()).toUpperCase();
  }

//  public void setEmail(String email) {
//    this.email = email;
//  }
  
//  public void setEContactInfo(String eContactInfo) {
//    this.eContactInfo = eContactInfo;
//  }
  
  public boolean isRequestPending() {
    return this.requestPending;
  }
  
  public void setRequestPending(boolean requestPending) {
    this.requestPending = requestPending;
  }
  
  @Override
  public String toString() {
    return "Person [firstname=" + getFirstname() + ", surname=" + getSurname() + ", ssn=" + getPic() + "]";
  }
  
}
