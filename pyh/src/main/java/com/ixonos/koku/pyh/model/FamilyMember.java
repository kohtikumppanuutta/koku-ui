package com.ixonos.koku.pyh.model;

import com.ixonos.koku.pyh.util.Role;

public class FamilyMember extends Person {

  private Role role;

  public FamilyMember(Person person, Role role) {
    super(person.getFirstname(), person.getMiddlename(), person.getSurname(), person.getSsn(), person.getBirthdate(),
        "");
    this.role = role;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

}
