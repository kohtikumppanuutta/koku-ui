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
  private String customerId;
  private KKS kks;

  public Person() {
    this.firstName = "";
    this.secondName = "";
    this.lastName = "";
    this.kks = new KKS();
  }

  public Person(String first, String second, String last, String pic) {
    this.firstName = first;
    this.secondName = second;
    this.lastName = last;
    this.pic = pic;
    this.kks = new KKS();
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
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

  public KKS getKks() {
    return kks;
  }

  public void setKks(KKS kks) {
    this.kks = kks;
  }

  public String getName() {
    return firstName + " " + lastName;
  }

  public static Person fromCustomerType(CustomerType c) {
    Person p = new Person();
    p.setFirstName(c.getEtuNimi());
    p.setSecondName(c.getEtunimetNimi());
    p.setLastName(c.getSukuNimi());
    p.setPic(c.getHenkiloTunnus());
    p.setCustomerId(c.getId());
    return p;
  }
}
