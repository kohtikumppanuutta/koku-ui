package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixonos.koku.pyh.util.CommunityRole;

import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.community.v1.MemberType;
import fi.koku.services.entity.community.v1.MembersType;

public class Family {

  private static Logger log = LoggerFactory.getLogger(Family.class);
  
//  private Map<String, FamilyMember> otherFamilyMembers;
//  private Map<String, FamilyMember> childs;
//  private Map<String, FamilyMember> parents;
  
  private CommunityType community;
  
  public Family() {
    // family is separated by roles (for ease of queries)
//    otherFamilyMembers = new LinkedHashMap<String, FamilyMember>();
//    childs = new LinkedHashMap<String, FamilyMember>();
//    parents = new LinkedHashMap<String, FamilyMember>();
  }
  
  /**
   * Constructor for creating a Family from CommunityType and list of FamilyMembers
   */
  public Family(CommunityType community) {
    this.community = community;
  }
  
  public String getCommunityId() {
    return community.getId();
  }
  
  public String getCommunityType() {
    return community.getType();
  }
  
  public String getCommunityName() {
    return community.getName();
  }
  
  public MembersType getCommunityMembers() {
    return community.getMembers();
  }
  
  public CommunityType getCommunity() {
    return community;
  }
  
  public void combineFamily(Family family) {
    for (MemberType member : family.getAllMembers()) {
      addFamilyMember(member.getPic(), member.getRole());
    }
  }
  
  public MemberType getOtherParent(String notWantedParentPic) {
    for (MemberType m : getParents()) {
      if (!m.getPic().equals(notWantedParentPic)) {
        return m;
      }
    }
    return null;
  }
  
  public void addFamilyMember(String memberPic, String role) {
    MembersType membersType = community.getMembers();
    List<MemberType> members = membersType.getMember();
    
    MemberType newMember = new MemberType();
    newMember.setPic(memberPic);
    newMember.setRole(role);
    members.add(newMember);
  }
  
//  public FamilyMember getChild(String familyMemberSSN) {
//    return childs.get(familyMemberSSN);
//  }

//  public FamilyMember getFamilyMember(String familyMemberSSN) {
//
//    if (childs.containsKey(familyMemberSSN)) {
//      return childs.get(familyMemberSSN);
//    }
//
//    if (parents.containsKey(familyMemberSSN)) {
//      return parents.get(familyMemberSSN);
//    }
//    return otherFamilyMembers.get(familyMemberSSN);
//  }

//  public List<FamilyMember> getOtherFamilyMembers() {
//    return new ArrayList<FamilyMember>(otherFamilyMembers.values());
//  }

//  public boolean isFamilyMember(String personSSN) {
//    return parents.containsKey(personSSN) || childs.containsKey(personSSN) || otherFamilyMembers.containsKey(personSSN);
//  }

//  public List<FamilyMember> getChilds() {
//    return new ArrayList<FamilyMember>(childs.values());
//  }

  public List<MemberType> getParents() {
    List<MemberType> parents = new ArrayList<MemberType>();
    MembersType membersType = community.getMembers();
    List<MemberType> members = membersType.getMember();
    Iterator<MemberType> mi = members.iterator();
    while (mi.hasNext()) {
      MemberType member = mi.next();
      CommunityRole memberRole = CommunityRole.createFromRoleID(member.getRole());
      if (CommunityRole.PARENT.equals(memberRole) || CommunityRole.MOTHER.equals(memberRole) || 
          CommunityRole.FATHER.equals(memberRole)) {
        parents.add(member);
      }
    }
    return parents;
  }
  
  public List<MemberType> getAllMembers() {
    MembersType membersType = community.getMembers();
    List<MemberType> members = membersType.getMember();
    return members;
  }
  
  public boolean isParentsSet() {
    return getParents().size() >= 2;
  }
  
}
