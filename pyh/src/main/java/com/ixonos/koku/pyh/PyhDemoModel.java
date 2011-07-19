package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.Guardianship;
import com.ixonos.koku.pyh.model.Person;

public class PyhDemoModel {
  
  private static Logger log = LoggerFactory.getLogger(PyhDemoModel.class);
  
  private List<Person> persons;
  private List<Family> families;
  private List<Guardianship> guardianships;
  
  public PyhDemoModel() {
    persons = new ArrayList<Person>();
    families = new ArrayList<Family>();
    guardianships = new ArrayList<Guardianship>();
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
    //log.info("PyhDemoModel.getFamilies: families.size = " + families.size());
    return families;
  }
  
  /// guardianships ///
  
  public void addGuardianship(Guardianship guardianship) {
    guardianships.add(guardianship);
  }
  
  public List<Guardianship> getGuardianships() {
    return guardianships;
  }
  
  /// dependants ///
  
  public List<Dependant> getAllDependants() {
    ArrayList<Dependant> dependants = new ArrayList<Dependant>();
    
    Iterator<Guardianship> gi = guardianships.iterator();
    while (gi.hasNext()) {
      Guardianship g = gi.next();
      List<Dependant> dList = g.getDependants();
      
      // iterate through guardianship's dependants and add them to the list to be returned
      Iterator<Dependant> di = dList.iterator();
      while (di.hasNext()) {
        Dependant d = di.next();
        dependants.add(d);
      }
    }
    return dependants;
  }
  
}
