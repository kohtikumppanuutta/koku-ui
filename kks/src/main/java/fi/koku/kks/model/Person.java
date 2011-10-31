/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.model;

import fi.koku.services.entity.customer.v1.CustomerType;

/**
 * Contains person info and collections
 * 
 * @author tuomape
 * 
 */
public class Person {

  private String firstName;
  private String secondName;
  private String lastName;
  private String pic;

  public Person() {
    this.firstName = "";
    this.secondName = "";
    this.lastName = "";
  }

  public Person(String first, String second, String last, String pic) {
    this.firstName = first;
    this.secondName = second;
    this.lastName = last;
    this.pic = pic;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }

  public String getId() {
    return getPic();
  }

  public String getName() {
    return secondName + " " + lastName;
  }

  public static Person fromCustomerType(CustomerType c) {
    Person p = new Person();
    p.setFirstName(c.getEtuNimi());
    p.setSecondName(c.getEtunimetNimi());
    p.setLastName(c.getSukuNimi());
    p.setPic(c.getHenkiloTunnus());
    return p;
  }
}
