package fi.koku.pyh.model;

import java.util.List;

public class FamilyIdAndFamilyMembers {

    private String familyId;
    private List<FamilyMember> familyMembers;
    
    public FamilyIdAndFamilyMembers() {
    }
    
    public void setFamilyMembers(List<FamilyMember> members) {
      this.familyMembers = members;
    }
    
    public void setFamilyId(String id) {
      this.familyId = id;
    }
    
    public List<FamilyMember> getFamilyMembers() {
      return familyMembers;
    }
    
    public String getFamilyId() {
      return familyId;
    }
    
}
