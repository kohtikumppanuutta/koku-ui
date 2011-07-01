package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.List;

import com.ixonos.koku.pyh.model.Person;

public class PyhDemoModel {
  
  private List<Person> persons;
  
  public PyhDemoModel() {
    persons = new ArrayList<Person>();
  }
  
  public void addPerson(Person person) {
    persons.add(person);
  }
  
  public List<Person> getPersons() {
    return persons;
  }
  
  public void setPersons(List<Person> persons) {
    this.persons = persons;
  }
  
}
