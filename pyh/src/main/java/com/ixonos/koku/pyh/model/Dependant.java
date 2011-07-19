package com.ixonos.koku.pyh.model;

public class Dependant extends Person {
  
  private boolean memberOfUserFamily;
  
  public Dependant(Person person) {
    super(person.getFirstname(), person.getMiddlename(), person.getSurname(), person.getSsn(), person.getBirthdate(), "");
    memberOfUserFamily = false;
  }
  
  public boolean getMemberOfUserFamily() {
    return memberOfUserFamily;
  }
  
  public void setMemberOfUserFamily(boolean isMember) {
    this.memberOfUserFamily = isMember;
  }
  
}
