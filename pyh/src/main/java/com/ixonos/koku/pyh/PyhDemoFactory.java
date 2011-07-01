package com.ixonos.koku.pyh;

import com.ixonos.koku.pyh.model.Person;

public class PyhDemoFactory {
  
  public static PyhDemoModel createModel() {
    Person person1 = new Person("Matti", "Tapani", "Meikäläinen", "010203-1234");
    Person person2 = new Person("Tiina", "Terhi", "Tavallinen", "040506-4567");
    
    PyhDemoModel model = new PyhDemoModel();
    model.addPerson(person1);
    model.addPerson(person2);
    return model;
  }
  
  
  
}
