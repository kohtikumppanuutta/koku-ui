package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixonos.koku.pyh.EditFamilyInformationController;

public class Family {
  
  private static Logger log = LoggerFactory.getLogger(Family.class);
  
  private List<FamilyMember> familyMembers;
  
  public Family() {
    familyMembers = new ArrayList<FamilyMember>();
  }
  
  public void addFamilyMember(FamilyMember familyMember) {
    if (isFamilyMember(familyMember.getSsn()) == false) {
      familyMembers.add(familyMember);
    }
  }
  
  public void removeFamilyMember(FamilyMember familyMember) {
    familyMembers.remove(familyMember);
  }
  
  public FamilyMember getFamilyMember(String familyMemberSSN) {
    Iterator<FamilyMember> fmi = familyMembers.iterator();
    while (fmi.hasNext()) {
      FamilyMember fm = fmi.next();
      if (fm.getSsn().equals(familyMemberSSN)) {
        return fm;
      }
    }
    return null;
  }
  
  public List<FamilyMember> getFamilyMembers() {
    return familyMembers;
  }
  
  public boolean isFamilyMember(String personSSN) {
    if (getFamilyMember(personSSN) != null) {
      return true;
    }
    else {
      return false;
    }
  }
  
  public String toString() {
    String family = "";
    Iterator<FamilyMember> i = familyMembers.iterator();
    while (i.hasNext()) {
      FamilyMember fm = i.next();
      family += fm.getFirstname() + " " + fm.getSurname() + "; ";
    }
    return family;
  }
  
}
