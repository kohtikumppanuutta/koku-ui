package fi.koku.pyh.model;


import fi.koku.pyh.util.CommunityRole;
import fi.koku.services.entity.community.v1.MemberType;
import fi.koku.services.entity.customer.v1.CustomerType;

public class FamilyMember extends Person {

  private CommunityRole role;
  
  public FamilyMember(CustomerType customer, CommunityRole role) {
    super(customer);
    this.role = role;
  }
  
  /**
   * Returns member's role ID which is one of the following:
   * guardian, dependant, parent, father, mother
   */
  public String getRoleId() {
    return role.getRoleID();
  }
  
  public CommunityRole getRole() {
    return role;
  }
  
  @Override
  public String toString() {
    return super.toString() + " FamilyMember [role=" + role + "]";
  }
  
}
