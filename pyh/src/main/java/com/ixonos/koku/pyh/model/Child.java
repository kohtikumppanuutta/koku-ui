package com.ixonos.koku.pyh.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Child extends Person {
  
  private static Logger log = LoggerFactory.getLogger(Child.class);
  
  private String guardianSSN;
  private boolean memberOfUserFamily;
  
  public Child(String firstname, String middlename, String surname, String ssn, String birthdate, String guardianSSN) {
    super(firstname, middlename, surname, ssn, birthdate);
    this.guardianSSN = guardianSSN;
  }
  
  // TODO: instead of 'member of user family' attribute use Family instance
  
  public boolean getMemberOfUserFamily() {
    return memberOfUserFamily;
  }
  
  public void setMemberOfUserFamily(boolean isMember) {
    this.memberOfUserFamily = isMember;
  }
  
  public String getGuardianSSN() {
    return guardianSSN;
  }
  
  
}
