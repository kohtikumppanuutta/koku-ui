package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ixonos.koku.pyh.mock.User;
import com.ixonos.koku.pyh.model.Child;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.FamilyMember;
import com.ixonos.koku.pyh.model.Person;

@Service (value = "pyhDemoService")
public class PyhDemoService {
  
  private PyhDemoModel model;
  private User user;
  private List<Person> searchedUsers;
  
  public PyhDemoService() {
    if (model == null) {
      this.model = PyhDemoFactory.createModel();
    }
    if (user == null) {
      this.user = new User("Pekka", "", "Peltola", "010101-1010", "", "pekka.peltola@meili.com", "guardian");
    }
    searchedUsers = null;
  }
  
  public User getUser() {
    return user;
  }
  
  public List<Child> getDependants(String guardianSSN) {
    List<Child> dependants = new ArrayList<Child>(); 
    
    Iterator<Person> i = model.getPersons().iterator();
    while (i.hasNext()) {
      Person p = i.next();
      if (p instanceof Child) {
        Child child = (Child) p;
        if (child.getGuardianSSN().equals(guardianSSN)) {
          dependants.add(child);
        }
      }
    }
    return dependants;
  }
  
  public List<FamilyMember> getFamilyMembers(String userSSN) {
    List<FamilyMember> familyMembers = new ArrayList<FamilyMember>();
    
    familyMembers.add(new FamilyMember("test", "test", "test", "test", "test", "test"));
    familyMembers.add(new FamilyMember("test", "test", "test", "test", "test", "test"));
    return familyMembers;
    
//    Iterator<Family> i = model.getFamilies().iterator();
//    while (i.hasNext()) {
//      Family f = i.next();
//      if (f.isFamilyMember(userSSN)) {
//        familyMembers = f.getFamilyMembers();
//        break;
//      }
//    }
//    
//    return familyMembers;
    
    // TODO: remove user and user's dependants from the list because we want to display only other family members
    // maybe Child and Person can inherit from FamilyMember...? and Child could inherit from Person?
    
//    Iterator<FamilyMember> i2 = familyMembers.iterator();
//    while (i2.hasNext()) {
//      FamilyMember fm = i2.next();
//      if (fm.getSsn().equals(userSSN) || (fm instanceof Child && ((Child)fm).getGuardianSSN().equals(userSSN)) ) {
//        familyMembers.remove(fm);
//      }
//    }
    
  }
  
  public void searchUsers(String firstname, String surname, String ssn) {
    searchedUsers = new ArrayList<Person>();
    
    // dummy user list population
    if (firstname.equals("") == false || surname.equals("") == false || ssn.equals("") == false) {
      searchedUsers.add(new Person("matti", "tapani", "mainio", "1234567", "123456"));
      searchedUsers.add(new Person("mika", "tapani", "mainio", "7654321", "765432"));
      searchedUsers.add(new Person("pertti", "tapani", "tikka", "019827", "321314"));
    }
    
  }
  
  public List<Person> getSearchedUsers() {
    return searchedUsers;
  }
  
  public void clearSearchedUsers() {
    searchedUsers = null;
  }
  
}
