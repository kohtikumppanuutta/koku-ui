package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ixonos.koku.pyh.mock.User;
import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.FamilyMember;
import com.ixonos.koku.pyh.model.Guardian;
import com.ixonos.koku.pyh.model.Guardianship;
import com.ixonos.koku.pyh.model.Person;
import com.ixonos.koku.pyh.util.Role;

@Service(value = "pyhDemoService")
public class PyhDemoService {

  private static Logger log = LoggerFactory.getLogger(PyhDemoService.class);

  private PyhDemoModel model;
  private User user;
  private List<Person> searchedUsers;

  public PyhDemoService() {
    if (model == null) {
      this.model = PyhDemoFactory.createModel();
    }
    if (user == null) {
      this.user = new User("010101-1010", "guardian");
      // this.user = new User("030405-3456", "guardian");
    }
  }

  public Person getUser() {
    return model.getPerson(user.getSsn());
  }

  public List<Dependant> getDependants() {
    List<Dependant> dependants = new ArrayList<Dependant>();

    List<Guardianship> guardianships = model.getGuardianships();
    Iterator<Guardianship> gsi = guardianships.iterator();
    while (gsi.hasNext()) {
      Guardianship gs = gsi.next();

      List<Guardian> guardians = gs.getGuardians();
      Iterator<Guardian> gi = guardians.iterator();
      while (gi.hasNext()) {
        Guardian g = gi.next();
        if (g.getSsn().equals(user.getSsn())) {

          // if user SSN matches guardian's SSN then we have correct
          // guardianship
          // and we can return guardianship's dependants (that is user's
          // dependants)
          dependants = gs.getDependants();
        }
      }
    }
    return dependants;
  }

  public List<FamilyMember> getNonDependants() {
    Family usersFamily = getUsersFamily();
    return usersFamily.getChilds();
  }

  public List<FamilyMember> getOtherFamilyMembers() {
    List<FamilyMember> otherFamilyMembers = new ArrayList<FamilyMember>();
    Family usersFamily = getUsersFamily();
    List<FamilyMember> fmList = usersFamily.getAllMembers();

    Iterator<FamilyMember> fmi = fmList.iterator();
    while (fmi.hasNext()) {
      FamilyMember fm = fmi.next();
      if (!fm.getSsn().equals(user.getSsn()) && !isUsersDependant(fm.getSsn())) {
        otherFamilyMembers.add(fm);
      }
    }
    return otherFamilyMembers;
  }

  public boolean isParentsSet() {
    return getUsersFamily().isParentsSet();
  }

  public void searchUsers(String firstname, String surname, String ssn) {
    clearSearchedUsers();

    Family family = getUsersFamily();
    if (firstname.equals("") == false || surname.equals("") == false || ssn.equals("") == false) {
      Iterator<Person> pi = model.getPersons().iterator();
      while (pi.hasNext()) {
        Person person = pi.next();
        if ((person.getFirstname().equalsIgnoreCase(firstname) || person.getSurname().equalsIgnoreCase(surname) || person
            .getSsn().equalsIgnoreCase(ssn)) && !family.isFamilyMember(person.getSsn())) {
          searchedUsers.add(person);
        }
      }
    }
  }

  public List<Person> getSearchedUsers() {
    return searchedUsers;
  }

  public void clearSearchedUsers() {
    if (searchedUsers == null) {
      searchedUsers = new ArrayList<Person>();
    } else {
      searchedUsers.clear();
    }
  }

  public void addDependantAsFamilyMember(String dependantSSN) {
    Dependant dependant = model.getDependant(dependantSSN);
    dependant.setMemberOfUserFamily(true);
    Family family = getUsersFamily();
    family.addFamilyMember(new FamilyMember(dependant, Role.CHILD));
  }

  public void addNewDependant(Person dependant) {

    if (model.getPerson(dependant.getSsn()) == null) {
      model.addPerson(dependant);
    }

    // get user's guardianship or create if does not exist
    Guardianship guardianship = model.getGuardianship(user.getSsn());
    if (guardianship == null) {

      guardianship = new Guardianship();
      Person person = model.getPerson(user.getSsn());
      // TODO: get guardian's role
      guardianship.addGuardian(new Guardian(person, "rooli"));
      model.addGuardianship(guardianship);
    }
    guardianship.addDependant(new Dependant(dependant));
  }

  public void removeGuardianShip(String dependantSSN) {
    // get user's guardianship or create if does not exist
    Guardianship guardianship = model.getGuardianship(user.getSsn());
    if (guardianship != null) {
      guardianship.removeGuardian(user.getSsn());

      if (guardianship.isEmpty()) {
        model.getGuardianships().remove(guardianship);
      }
    }
  }

  public void removeFamilyChild(String familyMemberSSN) {
    Family family = getUsersFamily();
    FamilyMember familyMember = family.getChild(familyMemberSSN);
    family.removeFamilyMember(familyMember);
  }

  public void removeFamilyMember(String familyMemberSSN) {
    Family family = getUsersFamily();
    FamilyMember familyMember = family.getFamilyMember(familyMemberSSN);
    family.removeFamilyMember(familyMember);
  }

  public void addPersonsAsFamilyMembers(HashMap<String, String> personMap) {
    Family family = getUsersFamily();
    Set<String> keys = personMap.keySet();
    Iterator<String> si = keys.iterator();
    while (si.hasNext()) {
      String ssn = si.next();
      String role = personMap.get(ssn);
      log.info("(ssn) " + ssn + " ; (role) " + role);

      Person person = model.getPerson(ssn);
      Role r = Role.create(role);
      switch (r) {
      case DEPENDANT:
        addNewDependant(person);
        break;
      default:
        family.addFamilyMember(new FamilyMember(person, r));
        break;
      }

    }
  }

  private Family getUsersFamily() {
    Iterator<Family> fi = model.getFamilies().iterator();
    while (fi.hasNext()) {
      Family f = fi.next();
      if (f.isFamilyMember(user.getSsn())) {
        return f;
      }
    }
    return null;
  }

  private boolean isUsersDependant(String dependantSSN) {
    String userSSN = user.getSsn();
    List<Guardianship> guardianships = model.getGuardianships();
    Iterator<Guardianship> gi = guardianships.iterator();
    while (gi.hasNext()) {
      Guardianship g = gi.next();
      if (g.guardianshipExists(user.getSsn(), dependantSSN)) {
        return true;
      }
    }
    return false;
  }

}
