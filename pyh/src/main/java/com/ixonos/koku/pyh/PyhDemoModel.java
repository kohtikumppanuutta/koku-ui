package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.List;

import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.Person;

public class PyhDemoModel {
  
  private List<Person> persons;
  private List<Family> families;
  
  public PyhDemoModel() {
    persons = new ArrayList<Person>();
    families = new ArrayList<Family>();
  }
  
  /// persons ///
  
  public void addPerson(Person person) {
    persons.add(person);
  }
  
  public List<Person> getPersons() {
    return persons;
  }
  
  /// families ///
  
  public void addFamily(Family family) {
    families.add(family);
  }
  
  public List<Family> getFamilies() {
    return families;
  }
  
  
  
}
