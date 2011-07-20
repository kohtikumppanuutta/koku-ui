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
    //log.info("adding family member: " + familyMember.getFirstname() + " " + familyMember.getSurname());
    familyMembers.add(familyMember);
  }
  
  public void removeFamilyMember(FamilyMember familyMember) {
    log.info("family members before removing:");
    Iterator<FamilyMember> fmi = familyMembers.iterator();
    while (fmi.hasNext()) {
      FamilyMember fm = fmi.next();
      log.info(fm.getFirstname() + " " + fm.getSurname());
    }
    
    familyMembers.remove(familyMember);
    
    log.info("family members after removing:");
    fmi = familyMembers.iterator();
    while (fmi.hasNext()) {
      FamilyMember fm = fmi.next();
      log.info(fm.getFirstname() + " " + fm.getSurname());
    }
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
    Iterator<FamilyMember> i = familyMembers.iterator();
    while (i.hasNext()) {
      FamilyMember fm = i.next();
      log.info("family member ssn: " + fm.getSsn());
      log.info("person ssn: " + personSSN);
      if (fm.getSsn().equals(personSSN)) {
        return true;
      }
    }
    return false;
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
