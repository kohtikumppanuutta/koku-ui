package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.Guardian;
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

  // / persons ///

  public void addPerson(Person person) {
    persons.add(person);
  }

  public Person getPerson(String personSSN) {
    Iterator<Person> pi = persons.iterator();
    while (pi.hasNext()) {
      Person person = pi.next();
      if (person.getSsn().equals(personSSN)) {
        return person;
      }
    }
    return null;
  }

  public List<Person> getPersons() {
    return persons;
  }

  // / families ///

  public void addFamily(Family family) {
    families.add(family);
  }

  public void removeFamily(Family family) {
    families.remove(family);
  }

  public List<Family> getFamilies() {
    // log.info("PyhDemoModel.getFamilies: families.size = " + families.size());
    return families;
  }

  // / guardianships ///

  public void addGuardianship(Guardianship guardianship) {
    guardianships.add(guardianship);
  }

  public Guardianship getGuardianship(String guardianSSN) {
    Iterator<Guardianship> gsi = guardianships.iterator();
    while (gsi.hasNext()) {
      Guardianship gs = gsi.next();
      Iterator<Guardian> gi = gs.getGuardians().iterator();
      while (gi.hasNext()) {
        Guardian g = gi.next();
        if (g.getSsn().equals(guardianSSN)) {
          return gs;
        }
      }
    }
    return null;
  }

  public List<Guardian> getDependantGuardians(String dependantSSN) {
    Set<Guardian> tmp = new HashSet<Guardian>();

    Iterator<Guardianship> gsi = guardianships.iterator();
    while (gsi.hasNext()) {
      Guardianship gs = gsi.next();
      Iterator<Dependant> gi = gs.getDependants().iterator();

      boolean add = false;
      while (gi.hasNext()) {
        Dependant g = gi.next();
        if (g.getSsn().equals(dependantSSN)) {
          add = true;
        }
      }

      if (add) {
        tmp.addAll(gs.getGuardians());
      }
    }

    return new ArrayList<Guardian>(tmp);
  }

  public List<Guardianship> getGuardianships() {
    return guardianships;
  }

  // / dependants ///

  public List<Dependant> getAllDependants() {
    ArrayList<Dependant> dependants = new ArrayList<Dependant>();

    Iterator<Guardianship> gi = guardianships.iterator();
    while (gi.hasNext()) {
      Guardianship g = gi.next();
      List<Dependant> dList = g.getDependants();

      // iterate through guardianship's dependants and add them to the list to
      // be returned
      Iterator<Dependant> di = dList.iterator();
      while (di.hasNext()) {
        Dependant d = di.next();
        dependants.add(d);
      }
    }
    return dependants;
  }

  public Dependant getDependant(String dependantSSN) {
    List<Dependant> dependants = getAllDependants();
    Iterator<Dependant> di = dependants.iterator();
    while (di.hasNext()) {
      Dependant dependant = di.next();
      if (dependant.getSsn().equals(dependantSSN)) {
        return dependant;
      }
    }
    return null;
  }

}
