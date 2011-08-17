package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo malli KKS:lle
 * 
 * @author tuomape
 * 
 */
public class KKSDemoModel {

  private List<Person> persons;

  public KKSDemoModel() {
    persons = new ArrayList<Person>();
  }

  public void addPerson(Person h) {
    persons.add(h);
  }

  public List<Person> getPersons() {
    return persons;
  }

  public void setPersons(List<Person> persons) {
    this.persons = persons;
  }

}
