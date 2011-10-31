/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package com.ixonos.koku.pyh.model;

import fi.koku.services.entity.customer.v1.CustomerType;

public class Dependant extends Person {
  
  private boolean memberOfUserFamily;
  
  public Dependant(CustomerType customer) {
    super(customer);
    memberOfUserFamily = false;
  }
  
  public boolean getMemberOfUserFamily() {
    return memberOfUserFamily;
  }
  
  public void setMemberOfUserFamily(boolean isMember) {
    this.memberOfUserFamily = isMember;
  }
  
}
