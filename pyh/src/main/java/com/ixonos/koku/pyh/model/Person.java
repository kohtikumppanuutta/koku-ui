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
    return customer.getEtuNimi();
  }
  
  public String getFirstnames() {
    return customer.getEtunimetNimi();
  }
  
  public String getSurname() {
    return customer.getSukuNimi();
  }

  public String getPic() {
    return customer.getHenkiloTunnus();
  }

  public String getBirthdate() {
    return customer.getSyntymaPvm().toString();
  }
  
  public Calendar getBirthdaycalendar() {
    return customer.getSyntymaPvm();
  }
  
  public String getEcontactinfo() {
    String electronicContactInfo = "";
    ElectronicContactInfosType contactInfoType = customer.getElectronicContactInfos();
    if (contactInfoType != null) {
      List<ElectronicContactInfoType> contactInfos = contactInfoType.getEContactInfo();
      Iterator<ElectronicContactInfoType> cii = contactInfos.iterator();
      while (cii.hasNext()) {
        ElectronicContactInfoType contactInfo = cii.next();
        String info = contactInfo.getContactInfoType() + ": " + contactInfo.getContactInfo();
        electronicContactInfo += info + "\n";
      }
    }
    return electronicContactInfo;
  }
  
  public String getFullName() {
    return getFirstname() + " " + getSurname();
  }

  public String getCapFullName() {
    return (getFirstname() + " " + getSurname() + " " + getPic()).toUpperCase();
  }

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
