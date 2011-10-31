/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package com.ixonos.koku.pyh.model;

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
