package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ixonos.koku.pyh.mock.User;
import com.ixonos.koku.pyh.model.Person;

@Service (value = "pyhDemoService")
public class PyhDemoService {
  
  private PyhDemoModel model;
  private User user;
  
  public PyhDemoService() {
    
  }
  
  public void createAndSetCurrentUser(User user) {
    this.model = PyhDemoFactory.createModel();
    this.user = user;
  }
  
  public List<Person> getGuardiansChildren(String guardianSSN)
  {
    // TODO: store children in a map to be retrieved by guardians SSN
    ArrayList<Person> l = new ArrayList<Person>();
    l.add(new Person("Matti", "Tapani", "Meikäläinen", "010203-1234"));
    l.add(new Person("Tiina", "Terhi", "Tavallinen", "040506-4567"));
    return l;
  }
  
  public User getUser() {
    return user;
  }
  
}
