package fi.koku.kks.model;

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

}
