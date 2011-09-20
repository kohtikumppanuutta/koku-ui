package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ixonos.koku.pyh.model.Dependant;
import com.ixonos.koku.pyh.model.DependantExecutable;
import com.ixonos.koku.pyh.model.Family;
import com.ixonos.koku.pyh.model.FamilyExecutable;
import com.ixonos.koku.pyh.model.FamilyMember;
import com.ixonos.koku.pyh.model.Message;
import com.ixonos.koku.pyh.model.MessageService;
import com.ixonos.koku.pyh.model.ParentExecutable;
import com.ixonos.koku.pyh.model.Person;
import com.ixonos.koku.pyh.util.CommunityRole;

import fi.koku.services.entity.community.v1.CommunitiesType;
import fi.koku.services.entity.community.v1.CommunityQueryCriteriaType;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.community.v1.MemberType;
import fi.koku.services.entity.community.v1.MembersType;
import fi.koku.services.entity.community.v1.ServiceFault;
import fi.koku.services.entity.customer.v1.CustomerQueryCriteriaType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.CustomersType;

@Service(value = "pyhDemoService")
public class PyhDemoService {
  
  private static Logger log = LoggerFactory.getLogger(PyhDemoService.class);
  
  private List<Person> searchedUsers;
  private String userPic;
  private String userID;
  
  @Autowired
  @Qualifier(value = "pyhMessageService")
  private MessageService messageService;
  
  private CustomerServicePortType customerService;
  private CommunityServicePortType communityService;
  
  public PyhDemoService() {
    
    // TODO: get user ID from UserInfo
    userID = "";
    
    // TODO: get user's PIC from the portal
    userPic = "010101-1010";
    
    String customerServiceEndpoint = PyhConstants.CUSTOMER_SERVICE_ENDPOINT;
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(PyhConstants.CUSTOMER_SERVICE_USER_ID, PyhConstants.CUSTOMER_SERVICE_PASSWORD, customerServiceEndpoint);
    customerService = customerServiceFactory.getCustomerService();
    
    String communityServiceEndpoint = PyhConstants.COMMUNITY_SERVICE_ENDPOINT;
    CommunityServiceFactory communityServiceFactory = new CommunityServiceFactory(PyhConstants.COMMUNITY_SERVICE_USER_ID, PyhConstants.COMMUNITY_SERVICE_PASSWORD, communityServiceEndpoint);
    communityService = communityServiceFactory.getCommunityService();
  }
  
  /**
   * Returns a person by PIC.
   */
  public Person getPerson(String pic) {
    CustomerType customer = null;
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userID);
    
    try {
      customer = customerService.opGetCustomer(pic, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("PyhDemoService.getUser: opGetCustomer raised a ServiceFault", fault);
    }
    
    // testing
    log.info("getPerson()");
    log.info("etunimet: " + customer.getEtunimetNimi());
    log.info("sukunimi: " + customer.getSukuNimi());
    log.info("hetu: " + customer.getHenkiloTunnus());
    
    return new Person(customer);
  }
  
  /**
   * Returns the current user.
   */
  public Person getUser() {
    return getPerson(userPic);
  }
  
  /**
   * Returns the user's PIC.
   */
  public String getUserPic() {
    return userPic;
  }
  
  /**
   * Returns user's dependants.
   */
  public List<Dependant> getDependants() {
    List<Dependant> dependants = new ArrayList<Dependant>();
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    communityQueryCriteria.setCommunityType(PyhConstants.COMMUNITY_TYPE_GUARDIAN_COMMUNITY);
    communityQueryCriteria.setMemberPic(userPic);
    CommunitiesType communitiesType = null;
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userID);
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userID);
    
    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, communityAuditInfoType);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getDependants: opQueryCommunities raised a ServiceFault", fault);
    }
    
    if (communitiesType != null) {
      List<CommunityType> communities = communitiesType.getCommunity();
      Iterator<CommunityType> ci = communities.iterator();
      
      // TODO: remove this log message
      if (!ci.hasNext()) {
        log.info("getDependants: communities.iterator() has no communities!");
      }
      
      while (ci.hasNext()) {
        CommunityType community = ci.next();
        MembersType membersType = community.getMembers();
        List<MemberType> members = membersType.getMember();
        Iterator<MemberType> mi = members.iterator();
        while (mi.hasNext()) {
          MemberType member = mi.next();
          if (member.getRole().equals(PyhConstants.ROLE_DEPENDANT)) {
            try {
              // FIXME: call to opGetCustomer must be placed outside the loop
              CustomerType customer = customerService.opGetCustomer(member.getPic(), customerAuditInfoType);
              dependants.add(new Dependant(customer));
            }
            catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
              log.error("PyhDemoService.getDependants: opGetCustomer raised an ServiceFault", fault);
            }
          }
        }
      }
    }
    return dependants;
  }
  
  /**
   * Returns all other members of the user's family except dependants.
   */
  public List<FamilyMember> getOtherFamilyMembers() {
    List<Dependant> dependants = getDependants();
    Set<String> dependantPics = new HashSet<String>();
    Iterator<Dependant> di = dependants.iterator();
    while (di.hasNext()) {
      dependantPics.add(di.next().getPic());
    }
    
    List<FamilyMember> otherFamilyMembers = new ArrayList<FamilyMember>();
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    communityQueryCriteria.setCommunityType(PyhConstants.COMMUNITY_TYPE_FAMILY);
    communityQueryCriteria.setMemberPic(userPic);
    CommunitiesType communitiesType = null;
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userID);
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userID);
    
    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, communityAuditInfoType);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getOtherFamilyMembers: opQueryCommunities raised a ServiceFault", fault);
    }
    
    if (communitiesType != null) {
      List<CommunityType> communities = communitiesType.getCommunity();
      Iterator<CommunityType> ci = communities.iterator();
      
      // TODO: remove this log message
      if (!ci.hasNext()) {
        log.info("getOtherFamilyMembers: communities.iterator() has no communities!");
      }
      
      while (ci.hasNext()) {
        CommunityType community = ci.next();
        MembersType membersType = community.getMembers();
        List<MemberType> members = membersType.getMember();
        Iterator<MemberType> mi = members.iterator();
        while (mi.hasNext()) {
          MemberType member = mi.next();
          if (!dependantPics.contains(member.getPic())) {
            CustomerType customer = null;
            
            try {
              // FIXME: getCustomeria ei pidä kutsua loopissa toistamiseen
              customer = customerService.opGetCustomer(member.getPic(), customerAuditInfoType);
            } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
              log.error("PyhDemoService.getOtherFamilyMembers: opGetCustomer raised a ServiceFault", fault);
            }
            
            if (customer != null) {
              otherFamilyMembers.add(new FamilyMember(customer, CommunityRole.createFromRoleID(member.getRole())));
            }
          }
        }
      }
    }
    return otherFamilyMembers;
  }
  
  /**
   * Checks if the user's family has max. number of parents.
   */
  public boolean isParentsSet() {
    Family family = null;
    
    try {
      family = getFamily(userPic);
    } catch (TooManyFamiliesException tme) {
      log.error("PyhDemoService.isParentsSet: caught TooManyFamiliesException", tme);
      return false;
    }
    
    if (family != null) {
      return family.isParentsSet();
    }
    return false;
  }
  
  /**
   * Query persons by name, PIC and customer ID and stores them in the searchedUsers list.
   */
  public void searchUsers(String firstname, String surname, String customerPic, String customerID) {
    clearSearchedUsers();
    
    CustomerQueryCriteriaType customerCriteria = new CustomerQueryCriteriaType();
    customerCriteria.setPic(customerPic);
    customerCriteria.setSelection("full"); // FIXME: tätä käytetään vain silloin kun tarvitaan käyttäjän kaikki tiedot
    CustomersType customersType = null;
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userID);
    
    try {
      customersType = customerService.opQueryCustomers(customerCriteria, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("PyhDemoService.searchUsers: opQueryCustomers raised a ServiceFault", fault);
    }
    
    if (customersType != null) {
      List<CustomerType> customers = customersType.getCustomer();
      Iterator<CustomerType> ci = customers.iterator();
      while (ci.hasNext()) {
        CustomerType customer = ci.next();
        searchedUsers.add(new Person(customer));
      }
    }
  }
  
  /**
   * Returns the results of a query for persons.
   */
  public List<Person> getSearchedUsers() {
    return searchedUsers;
  }
  
  /**
   * Clears the search results.
   */
  public void clearSearchedUsers() {
    if (searchedUsers == null) {
      searchedUsers = new ArrayList<Person>();
    } else {
      searchedUsers.clear();
    }
  }
  
  /**
   * Adds a dependant into the user's family.
   */
  public void addDependantAsFamilyMember(String dependantPic) {
    // REMOVE COMMENTS, IF CONFIRMATION MESSAGES ARE NEED ALSO FOR DEPENDANT >
    // FAMILY INSERTION
    // List<String> tmp = generateRecipients(dependantPic, user, Role.CHILD);

    // if (tmp.size() == 0) {
    
    insertDependantToFamily(userPic, dependantPic, CommunityRole.CHILD);
    
    // } else {
    // sendDependantFamilyAdditionMessage(tmp, user,
    // model.getPerson(dependantPic), Role.CHILD);
    // }
  }
  
  /**
   * Selects persons (PICs) to whom send the confirmation message for a operation, for example adding a dependant 
   * into a family.
   */
  private List<String> generateRecipients(String targetPic, Person user, CommunityRole role) {
    List<String> recipientPics = new ArrayList<String>();
    
    if (CommunityRole.CHILD.equals(role)) {
      CommunityQueryCriteriaType communityCriteria = new CommunityQueryCriteriaType();
      communityCriteria.setCommunityType(PyhConstants.COMMUNITY_TYPE_GUARDIAN_COMMUNITY);
      communityCriteria.setMemberPic(targetPic);
      CommunitiesType communitiesType = null;

      fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
      communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
      communityAuditInfoType.setUserId(userID);
      
      try {
        communitiesType = communityService.opQueryCommunities(communityCriteria, communityAuditInfoType);
      }
      catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
        log.error("PyhDemoService.generateRecipients: opQueryCommunities raised a ServiceFault", fault);
      }
      
      if (communitiesType != null) {
        List<CommunityType> communities = communitiesType.getCommunity();
        Iterator<CommunityType> ci = communities.iterator();
        while (ci.hasNext()) {
          CommunityType community = ci.next();
          MembersType membersType = community.getMembers();
          List<MemberType> members = membersType.getMember();
          Iterator<MemberType> mi = members.iterator();
          while (mi.hasNext()) {
            MemberType member = mi.next();
            if (member.getRole().equals(PyhConstants.ROLE_GUARDIAN)) {
              recipientPics.add(member.getPic());
            }
          }
        }
      }
    } else {
      
      MemberType familyMember = null;
      
      try {
        familyMember = getFamily(userPic).getOtherParent(user.getPic());
      } catch (TooManyFamiliesException tme) {
        log.error("PyhDemoService.generateRecipients: caught TooManyFamiliesException", tme);
        return null;
      }
      
      if (familyMember != null) {
        recipientPics.add(familyMember.getPic());
      }
      Family family = null;
      
      try {
        family = getFamily(targetPic);
      } catch (TooManyFamiliesException tme) {
        log.error("PyhDemoService.generateRecipients: caught TooManyFamiliesException", tme);
        return null;
      }
      
      if (family != null) {
        for (MemberType member : family.getParents()) {
          if (!member.getPic().equals(user.getPic()) && !recipientPics.contains(member.getPic())) {
            recipientPics.add(member.getPic());
          }
        }
      }
    }
    return recipientPics;
  }
  
  /**
   * Inserts a dependant into a family.
   */
  public void insertDependantToFamily(String userPic, String dependantPic, CommunityRole role) {
      Family family = null;
      
      try {
        family = getFamily(userPic);
      } catch (TooManyFamiliesException tme) {
        log.error("PyhDemoService.insertDependantToFamily: caught TooManyFamiliesException", tme);
        return;
      }
      
      family.addFamilyMember(dependantPic, role.getRoleID());
      
      fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
      communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
      communityAuditInfoType.setUserId(userID);
      
      try {
        communityService.opUpdateCommunity(family.getCommunity(), communityAuditInfoType);
      } catch (ServiceFault fault) {
        log.error("PyhDemoService.insertDependantToFamily: opUpdateCommunity raised a ServiceFault", fault);
      }
  }
  
  /**
   * Removes a family member from a family community.
   */
  public void removeFamilyMember(String familyMemberPic) {
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    communityQueryCriteria.setCommunityType(PyhConstants.COMMUNITY_TYPE_FAMILY);
    communityQueryCriteria.setMemberPic(familyMemberPic);
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userID);
    CommunitiesType communitiesType = null;
    
    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, communityAuditInfoType);
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.removeFamilyMember: opGetCommunity raised a ServiceFault", fault);
    }
    
    if (communitiesType != null) {
      List<CommunityType> communityType = communitiesType.getCommunity();
      Iterator<CommunityType> ci = communityType.iterator();
      CommunityType community = null;
      while (ci.hasNext()) {
        community = ci.next();
        MembersType membersType = community.getMembers();
        List<MemberType> members = membersType.getMember();
        Iterator<MemberType> mi = members.iterator();
        while (mi.hasNext()) {
          MemberType member = mi.next();
          if (member.getPic().equals(familyMemberPic)) {
            members.remove(member);
            try {
              communityService.opUpdateCommunity(community, communityAuditInfoType);
            } catch (ServiceFault fault) {
              log.error("PyhDemoService.removeFamilyMember: opUpdateCommunity raised a ServiceFault", fault);
            }
            return;
          }
        }
      }
    }
  }
  
  /**
   * Creates a recipient list for confirmation request message for adding persons as family members.
   * Then the message sending method is called or if there are no recipients then persons are added immediately.
   */
  public void addPersonsAsFamilyMembers(HashMap<String, String> personMap) {
    
    // personMap parameter contains (personPIC, role) pairs
    
    Set<String> keys = personMap.keySet();
    Iterator<String> si = keys.iterator();
    Person user = getPerson(userPic);
    while (si.hasNext()) {
      String pic = si.next();
      String role = personMap.get(pic);
      
      Person person = getPerson(pic);
      CommunityRole communityRole = CommunityRole.create(role);
      
      List<String> recipients = generateRecipients(pic, user, communityRole);
      
      if (CommunityRole.PARENT.equals(communityRole) || CommunityRole.FATHER.equals(communityRole) || 
          CommunityRole.MOTHER.equals(communityRole)) {
        sendParentAdditionMessage(pic, user, pic, person, communityRole);
      } else if (recipients.size() == 0) {
        insertInto(userPic, pic, communityRole);
      } else {
        sendFamilyAdditionMessage(recipients, user, pic, person, communityRole);
      }
    }
  }
  
  /**
   * TODO: service implementation (Käyttäjäviestintä)
   */
  private void sendParentAdditionMessage(String recipient, Person user, String pic, Person person, CommunityRole r) {
    List<String> tmp = new ArrayList<String>();
    tmp.add(recipient);
    Message message = Message.createMessage(tmp, user.getPic(), pic, person.getCapFullName()
        + " Uusi perheyhteystieto.", "Käyttäjä " + user.getFullName()
        + " on lisännyt sinut perheyhteisön toiseksi vanhemmaksi. "
        + "Hyväksymällä pyynnön perheyhteisönne yhdistetään automaattisesti.", new ParentExecutable(user.getPic(), pic,
        r, this));
    messageService.addMessage(message);
  }
  
  /**
   * TODO: service implementation (Käyttäjäviestintä)
   */
  private void sendFamilyAdditionMessage(List<String> recipients, Person user, String pic, Person person, CommunityRole r) {
    Message message = Message.createMessage(recipients, user.getPic(), pic, person.getCapFullName()
        + " Uusi perheyhteystieto.", "Käyttäjä " + user.getFullName() + " on lisännyt henkilön " + person.getFullName()
        + " perheyhteisön muuksi jäseneksi. "
        + "Kaikkien opsapuolten on hyväksyttävä uuden jäsenen liittäminen perheyhteisöön.",
        new FamilyExecutable(user.getPic(), pic, r, this));
    messageService.addMessage(message);
  }
  
  /**
   * TODO: service implementation (Käyttäjäviestintä)
   */
  private void sendDependantFamilyAdditionMessage(List<String> recipients, Person user, Person person, CommunityRole r) {
    Message message = Message.createMessage(recipients, user.getPic(), person.getPic(), person.getCapFullName()
        + " Uusi perheyhteystieto.", "Käyttäjä " + user.getFullName() + " on lisännyt henkilön " + person.getFullName()
        + " perheyhteisön muuksi jäseneksi. "
        + "Kaikkien opsapuolten on hyväksyttävä uuden jäsenen liittäminen perheyhteisöön.", new DependantExecutable(
        user.getPic(), person.getPic(), r, this));
    person.setRequestPending(true);
    messageService.addMessage(message);
  }
  
  /**
   * Inserts a new member into a family community.
   */
  public void insertInto(String toFamilyPic, String personPic, CommunityRole role) {
    Family family = null;
    
    try {
      family = getFamily(toFamilyPic);
    } catch (TooManyFamiliesException tme) {
      log.error("PyhDemoService.insertInto: caught TooManyFamiliesException", tme);
      return;
    }
    
    family.addFamilyMember(personPic, role.getRoleID());
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userID);
    
    try {
      communityService.opUpdateCommunity(family.getCommunity(), communityAuditInfoType);
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.insertInto: opUpdateCommunity raised a ServiceFault", fault);
    }
  }
  
  /**
   * Inserts a parent (from other family) into a family and then combines the two families.
   */
  public void insertParentInto(String toFamilyPic, String personPic, CommunityRole role) {
    Family family = null;
    Family combine = null;
    
    try {
      family = getFamily(toFamilyPic);
      combine = getFamily(personPic);
    } catch (TooManyFamiliesException tme) {
      log.error("PyhDemoService.insertParentInto: caught TooManyFamiliesException", tme);
      return;
    }
    
    family.combineFamily(combine);
    family.addFamilyMember(personPic, role.getRoleID());
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userID);
    
    try {
      communityService.opUpdateCommunity(family.getCommunity(), communityAuditInfoType);
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.insertParentInto: opUpdateCommunity raised a ServiceFault", fault);
    }
    
    removeFamily(combine);
  }
  
  /**
   * Removes a family community.
   */
  private void removeFamily(Family family) {
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userID);
    
    try {
      communityService.opDeleteCommunity(family.getCommunityId(), communityAuditInfoType);
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.removeFamily: opDeleteCommunity raised a ServiceFault", fault);
    }
  }
  
  /**
   * Returns a family by person's PIC.
   *
   * TODO: miten ratkaistaan se, että tämä metodi voi palauttaa useamman kuin yhden perheen?
   */
  private Family getFamily(String pic) throws TooManyFamiliesException {
    List<Family> families = new ArrayList<Family>();
    CommunityQueryCriteriaType communityCriteria = new CommunityQueryCriteriaType();
    communityCriteria.setCommunityType(PyhConstants.COMMUNITY_TYPE_FAMILY);
    communityCriteria.setMemberPic(pic);
    CommunitiesType communitiesType = null;
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userID);
    
    try {
      communitiesType = communityService.opQueryCommunities(communityCriteria, communityAuditInfoType);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getFamily: opQueryCommunities raised a ServiceFault", fault);
    }
    
    if (communitiesType != null) {
      List<CommunityType> communities = communitiesType.getCommunity();
      Iterator<CommunityType> ci = communities.iterator();
      while (ci.hasNext()) { 
        CommunityType community = ci.next();
        families.add(new Family(community));
      }
      
      if (families.size() > 1) {
        TooManyFamiliesException tme = new TooManyFamiliesException("opQueryCommunities with parameter 'pic=" + pic + "' returned more than one family!");
      }
      
      if (families.size() > 0) {
        return families.get(0);
      }
    }
    return null;
  }
  
  /**
   * Checks if a dependant is user's dependant.
   */
  private boolean isUsersDependant(String dependantPic) {
    List<Dependant> dependants = getDependants();
    Iterator<Dependant> di = dependants.iterator();
    while (di.hasNext()) {
      Dependant dependant = di.next();
      if (dependantPic.equals(dependant.getPic())) {
        return true;
      }
    }
    return false;
  }
  
}
