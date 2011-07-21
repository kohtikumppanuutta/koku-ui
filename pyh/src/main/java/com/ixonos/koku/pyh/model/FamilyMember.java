package com.ixonos.koku.pyh.model;

public class FamilyMember extends Person {
  
  private String role;
  
  public FamilyMember(Person person, String role) {
    super(person.getFirstname(), person.getMiddlename(), person.getSurname(), person.getSsn(), person.getBirthdate(), "");
    this.role = role;
  }
  
  public String getRole() {
    return role;
  }
  
  public void setRole(String role) {
    this.role = role;
  }
  
}
