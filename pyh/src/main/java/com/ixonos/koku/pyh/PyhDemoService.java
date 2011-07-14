package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ixonos.koku.pyh.mock.User;
import com.ixonos.koku.pyh.model.Child;
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
      this.user = new User("guardian", "Pekka", "Perustyyppi", "pekka.perustyyppi@meili.com");
    }
    
  }
  
  public User getUser() {
    return user;
  }
  
  public List<Child> getGuardiansChildren(String guardianSSN) {
    
    // TODO: store children in a map to be retrieved by guardians SSN
    // set the child's status 'member of the user's family'
    
    Child c1 = new Child("Matti", "Tapani", "Meikäläinen", "010203-1234", "010203");
    Child c2 = new Child("Tiina", "Terhi", "Tavallinen", "040506-4567", "040506");
    c1.setMemberOfUserFamily(true);
    c2.setMemberOfUserFamily(false);
    
    ArrayList<Child> l = new ArrayList<Child>();
    l.add(c1);
    l.add(c2);
    return l;
  }
  
  public List<FamilyMember> getFamilyMembers(String userSSN) {
    // TODO: how to store user's family members?
    // TODO: add role attribute to family members
    ArrayList<FamilyMember> l = new ArrayList<FamilyMember>();
    l.add(new FamilyMember("mem", "ber", "1", "000000-0000", "000000", "äiti"));
    l.add(new FamilyMember("mem", "ber", "2", "111111-1111", "111111", "lapsi"));
    return l;
  }
  
  public void searchUsers(String firstname, String surname, String ssn) {
    clearSearchedUsers();
    
    // dummy user list population
    searchedUsers.add(new Person("matti", "tapani", "mainio", "1234567", "123456"));
    searchedUsers.add(new Person("mika", "tapani", "mainio", "7654321", "765432"));
    searchedUsers.add(new Person("pertti", "tapani", "tikka", "019827", "321314"));
  }
  
  public List<Person> getSearchedUsers() {
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
  
}
