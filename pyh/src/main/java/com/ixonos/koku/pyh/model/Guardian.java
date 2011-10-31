/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package com.ixonos.koku.pyh.model;

import fi.koku.services.entity.customer.v1.CustomerType;

public class Guardian extends Person {
  
  private String guardianRole;
  
  /*
  public Guardian(Person person, String guardianRole) {
    super(person.getFirstname(), person.getMiddlename(), person.getSurname(), person.getSsn(), person.getBirthdate(), person.getEmail());
    this.guardianRole = guardianRole;
  }
  */
  
  public Guardian(CustomerType customer) {
    super(customer);
  }
  
  public String getGuardianRole() {
    return guardianRole;
  }
  
}
