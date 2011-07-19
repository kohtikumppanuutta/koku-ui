package com.ixonos.koku.pyh.model;

public class Guardian extends Person {
  
  private String guardianRole;
  
  public Guardian(Person person, String guardianRole) {
    super(person.getFirstname(), person.getMiddlename(), person.getSurname(), person.getSsn(), person.getBirthdate(), "");
    this.guardianRole = guardianRole;
  }
  
  public String getGuardianRole() {
    return guardianRole;
  }
  
}
