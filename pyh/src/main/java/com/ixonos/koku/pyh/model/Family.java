package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Family {
  
  private List<FamilyMember> familyMembers;
  
  public Family() {
    familyMembers = new ArrayList<FamilyMember>();
  }
  
  public void addFamilyMember(FamilyMember familyMember) {
    familyMembers.add(familyMember);
  }
  
  public List<FamilyMember> getFamilyMembers() {
    return familyMembers;
  }
  
  public boolean isFamilyMember(String personSSN) {
    Iterator<FamilyMember> i = familyMembers.iterator();
    while (i.hasNext()) {
      if (i.next().getSsn().equals(personSSN)) {
        return true;
      }
    }
    return false;
  }
  
}
