package fi.koku.pyh.model;

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
