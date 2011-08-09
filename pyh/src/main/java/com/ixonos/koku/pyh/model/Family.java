package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixonos.koku.pyh.util.Role;

public class Family {

  private static Logger log = LoggerFactory.getLogger(Family.class);

  private Map<String, FamilyMember> otherFamilyMembers;
  private Map<String, FamilyMember> childs;
  private Map<String, FamilyMember> parents;

  public Family() {
    // family is separated by roles (for ease of queries)
    otherFamilyMembers = new LinkedHashMap<String, FamilyMember>();
    childs = new LinkedHashMap<String, FamilyMember>();
    parents = new LinkedHashMap<String, FamilyMember>();
  }

  public void combineFamily(Family family) {
    for (FamilyMember tmp : family.getAllMembers()) {
      addFamilyMember(tmp);
    }
  }

  public FamilyMember getOtherParent(String notWantedParentSSN) {
    for (FamilyMember f : getParents()) {
      if (!f.getSsn().equals(notWantedParentSSN)) {
        return f;
      }
    }
    return null;
  }

  public void addFamilyMember(FamilyMember familyMember) {

    if (Role.CHILD.equals(familyMember.getRole())) {
      childs.put(familyMember.getSsn(), familyMember);
    } else if (Role.PARENT.equals(familyMember.getRole()) || Role.FATHER.equals(familyMember.getRole())
        || Role.MOTHER.equals(familyMember.getRole())) {
      parents.put(familyMember.getSsn(), familyMember);
    } else {
      otherFamilyMembers.put(familyMember.getSsn(), familyMember);
    }

  }

  public void removeFamilyMember(FamilyMember familyMember) {
    if (familyMember == null)
      return;

    if (Role.CHILD.equals(familyMember.getRole())) {
      childs.remove(familyMember.getSsn());
    } else if (Role.PARENT.equals(familyMember.getRole()) || Role.FATHER.equals(familyMember.getRole())
        || Role.MOTHER.equals(familyMember.getRole())) {
      parents.remove(familyMember.getSsn());
    } else {
      otherFamilyMembers.remove(familyMember.getSsn());
    }
  }

  public FamilyMember getChild(String familyMemberSSN) {
    return childs.get(familyMemberSSN);
  }

  public FamilyMember getFamilyMember(String familyMemberSSN) {

    if (childs.containsKey(familyMemberSSN)) {
      return childs.get(familyMemberSSN);
    }

    if (parents.containsKey(familyMemberSSN)) {
      return parents.get(familyMemberSSN);
    }
    return otherFamilyMembers.get(familyMemberSSN);
  }

  public List<FamilyMember> getOtherFamilyMembers() {
    return new ArrayList<FamilyMember>(otherFamilyMembers.values());
  }

  public boolean isFamilyMember(String personSSN) {
    return parents.containsKey(personSSN) || childs.containsKey(personSSN) || otherFamilyMembers.containsKey(personSSN);
  }

  public List<FamilyMember> getChilds() {
    return new ArrayList<FamilyMember>(childs.values());
  }

  public List<FamilyMember> getParents() {
    return new ArrayList<FamilyMember>(parents.values());
  }

  public List<FamilyMember> getAllMembers() {
    List<FamilyMember> tmp = new ArrayList<FamilyMember>();
    tmp.addAll(getParents());
    tmp.addAll(getChilds());
    tmp.addAll(getOtherFamilyMembers());
    return tmp;
  }

  public boolean isParentsSet() {
    // Family can have max 2 parents
    return parents.size() >= 2;
  }

  public String toString() {
    String family = "";

    Iterator<FamilyMember> it = childs.values().iterator();
    while (it.hasNext()) {
      FamilyMember fm = it.next();
      family += fm.getFirstname() + " " + fm.getSurname() + "; ";
    }

    Iterator<FamilyMember> ite = childs.values().iterator();
    while (ite.hasNext()) {
      FamilyMember fm = ite.next();
      family += fm.getFirstname() + " " + fm.getSurname() + "; ";
    }

    Iterator<FamilyMember> i = otherFamilyMembers.values().iterator();
    while (i.hasNext()) {
      FamilyMember fm = i.next();
      family += fm.getFirstname() + " " + fm.getSurname() + "; ";
    }
    return family;
  }

}
