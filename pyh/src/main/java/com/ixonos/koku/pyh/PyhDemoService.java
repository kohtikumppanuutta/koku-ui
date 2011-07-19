package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

@Service (value = "pyhDemoService")
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
      //this.user = new User("030405-3456", "guardian");
    }
    
  }
  
  public Person getUser() {
    List<Person> persons = model.getPersons();
    Iterator<Person> pi = persons.iterator();
    while (pi.hasNext()) {
      Person person = pi.next();
      if (person.getSsn().equals(user.getSsn())) {
        return person;
      }
    }
    return null; // unrecoverable error; user not found
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
          
          // if user SSN matches guardian's SSN then we have correct guardianship 
          // and we can return guardianship's dependants (that is user's dependants)
          dependants = gs.getDependants();
        }
      }
    }
    
    log.info("calling PyhDemoService.getDependants: returning dependants.size = " + dependants.size());
    return dependants;
  }
  
  public List<FamilyMember> getOtherFamilyMembers() {
    List<FamilyMember> otherFamilyMembers = new ArrayList<FamilyMember>();
    
    //try {
    
    log.info("calling PyhDemoService.getOtherFamilyMembers");
    log.info("userSSN: " + user.getSsn());
    
    Family usersFamily = getUsersFamily();
    
    if (usersFamily == null) {
      log.info("VIRHE: usersFamily == NULL");
      return otherFamilyMembers;
    }
    
    List<FamilyMember> fmList = usersFamily.getFamilyMembers();
    
    if (fmList == null) {
      log.info("VIRHE: fmList == NULL");
      return otherFamilyMembers;
    }
    
    Iterator<FamilyMember> fmi = fmList.iterator();
    while (fmi.hasNext()) {
      FamilyMember fm = fmi.next();
      if (fm.getSsn().equals(user.getSsn())== false && isUsersDependant(fm.getSsn()) == false) {
        otherFamilyMembers.add(fm);
      }
    }
    
    log.info("PyhDemoService.getOtherFamilyMembers: otherFamilyMembers.size() = " + otherFamilyMembers.size());
    
    return otherFamilyMembers;
  }
  
  public void searchUsers(String firstname, String surname, String ssn) {
    log.info("calling PyhDemoService.searchUsers: SEARCHING USERS");
    
    clearSearchedUsers();
    
    // dummy user list population
    if (firstname.equals("") == false || surname.equals("") == false || ssn.equals("") == false) {
      searchedUsers.add(new Person("matti", "tapani", "mainio", "1234567", "123456", ""));
      searchedUsers.add(new Person("mika", "tapani", "mainio", "7654321", "765432", ""));
      searchedUsers.add(new Person("pertti", "tapani", "tikka", "019827", "321314", ""));
    }
    
  }
  
  public List<Person> getSearchedUsers() {
    if (searchedUsers == null) {
      log.info("returning searchedUsers: null");
    } else {
      log.info("returning searchedUsers: NOT null, size: " + searchedUsers.size());
    }
    return searchedUsers;
  }
  
  public void clearSearchedUsers() {
    if (searchedUsers == null) {
      searchedUsers = new ArrayList<Person>();
    }
    else {
      searchedUsers.clear();
    }
  }
  
  private Family getUsersFamily() {
    Iterator<Family> fi = model.getFamilies().iterator();
    while (fi.hasNext()) {
      Family f = fi.next();
      if (f.isFamilyMember(user.getSsn())) {
        return f;
      }
      log.info("-- next family --");
    }
    return null;
  }
  
  private Person getPerson(String personSSN) {
    List<Person> persons = model.getPersons();
    Iterator<Person> pi = persons.iterator();
    while (pi.hasNext()) {
      Person person = pi.next();
      if (person.getSsn().equals(personSSN)) {
        return person;
      }
    }
    return null;
  }
  
  private Dependant getDependant(String dependantSSN) {
    List<Dependant> dependants = model.getAllDependants();
    Iterator<Dependant> di = dependants.iterator();
    while (di.hasNext()) {
      Dependant dependant = di.next();
      if (dependant.getSsn().equals(dependantSSN)) {
        return dependant;
      }
    }
    return null;
  }
  
  public void addDependantAsFamilyMember(String dependantSSN) {
    Dependant dependant = getDependant(dependantSSN);
    dependant.setMemberOfUserFamily(true);
    Family family = getUsersFamily();
    family.addFamilyMember(new FamilyMember(dependant, "lapsi"));
    
    // testing
//    log.info("new family member added, members now are:");
//    List<FamilyMember> members = family.getFamilyMembers();
//    Iterator<FamilyMember> fmi = members.iterator();
//    while (fmi.hasNext()) {
//      FamilyMember fm = fmi.next();
//      log.info(fm.getFirstname() + " " + fm.getSurname());
//    }
  }
  
  private boolean isUsersDependant(String personSSN) {
    String userSSN = user.getSsn();
    List<Guardianship> guardianships = model.getGuardianships();
    Iterator<Guardianship> gi = guardianships.iterator();
    while (gi.hasNext()) {
      Guardianship g = gi.next();
      if (g.guardianshipExists(user.getSsn(), personSSN)) {
        return true;
      }
    }
    return false;
  }
  
}
