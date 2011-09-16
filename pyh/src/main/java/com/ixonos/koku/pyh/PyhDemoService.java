package com.ixonos.koku.pyh;

import java.util.ArrayList;
import java.util.HashMap;
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
  
  @Autowired
  @Qualifier(value = "pyhMessageService")
  private MessageService messageService;
  
  private CustomerServicePortType customerService;
  private CommunityServicePortType communityService;
  
  public PyhDemoService() {
    
    // TODO: get user's PIC from the portal
    userPic = "010101-1010";
    
    // TODO: initialization with username and password
    
    String customerServiceEndpoint = "http://localhost:8180/customer-service-0.0.1-SNAPSHOT/CustomerServiceEndpointBean?wsdl";
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory("u", "p", customerServiceEndpoint);
    customerService = customerServiceFactory.getCustomerService();
    
    String communityServiceEndpoint = "http://localhost:8180/customer-service-0.0.1-SNAPSHOT/CommunityServiceEndpointBean?wsdl";
    CommunityServiceFactory communityServiceFactory = new CommunityServiceFactory("u", "p", communityServiceEndpoint);
    communityService = communityServiceFactory.getCommunityService();
  }
  
  /**
   * Returns a person by PIC.
   */
  public Person getPerson(String pic) {
    CustomerType customer = null;
    try {
      // TODO: AuditInfoType
      customer = customerService.opGetCustomer(pic, new fi.koku.services.entity.customer.v1.AuditInfoType());
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("PyhDemoService.getUser: opGetCustomer raised a ServiceFault", fault);
    }
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
    // TODO: get community type from constants
    communityQueryCriteria.setCommunityType("guardian_community");
    communityQueryCriteria.setMemberPic(userPic);
    CommunitiesType communitiesType = null;
    
    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, new fi.koku.services.entity.community.v1.AuditInfoType());
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getDependants: opQueryCommunities raised a ServiceFault", fault);
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
          // TODO: get role from constants
          if (member.getRole().equals("dependant")) {
            try {
              CustomerType customer = customerService.opGetCustomer(member.getPic(), new fi.koku.services.entity.customer.v1.AuditInfoType());
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
    List<FamilyMember> otherFamilyMembers = new ArrayList<FamilyMember>();
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    // TODO: get community type from constants
    communityQueryCriteria.setCommunityType("family_community");
    communityQueryCriteria.setMemberPic(userPic);
    CommunitiesType communitiesType = null;
    
    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, new fi.koku.services.entity.community.v1.AuditInfoType());
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getOtherFamilyMembers: opQueryCommunities raised a ServiceFault", fault);
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
          // TODO: get role from constants
          if (!member.getRole().equals("dependant")) { // TODO: dependant is not role of family_community; is this correct?
            CustomerType customer = null;
            
            try {
              customer = customerService.opGetCustomer(member.getPic(), new fi.koku.services.entity.customer.v1.AuditInfoType());
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
    Family family = getFamily(userPic);
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
    
    // TODO: what is the final query criteria?
    CustomerQueryCriteriaType customerCriteria = new CustomerQueryCriteriaType();
    customerCriteria.setId(customerID);
    customerCriteria.setPic(customerPic);
    CustomersType customersType = null;
    
    try {
      customersType = customerService.opQueryCustomers(customerCriteria, new fi.koku.services.entity.customer.v1.AuditInfoType());
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
  public void addDependantAsFamilyMember(String dependantSSN) {
    // REMOVE COMMENTS, IF CONFIRMATION MESSAGES ARE NEED ALSO FOR DEPENDANT >
    // FAMILY INSERTION
    // List<String> tmp = generateRecipients(dependantSSN, user, Role.CHILD);

    // if (tmp.size() == 0) {
    
    insertDependantToFamily(userPic, dependantSSN, CommunityRole.CHILD);
    
    // } else {
    // sendDependantFamilyAdditionMessage(tmp, user,
    // model.getPerson(dependantSSN), Role.CHILD);
    // }
  }
  
  /**
   * Selects persons (PICs) to whom send the confirmation message for a operation, for example adding a dependant 
   * into a family.
   */
  private List<String> generateRecipients(String targetSSN, Person user, CommunityRole role) {
    List<String> recipientPics = new ArrayList<String>();
    
    if (CommunityRole.CHILD.equals(role)) {
      CommunityQueryCriteriaType communityCriteria = new CommunityQueryCriteriaType();
      communityCriteria.setCommunityType("guardian_community");
      communityCriteria.setMemberPic(targetSSN);
      CommunitiesType communitiesType = null;
      
      try {
        communitiesType = communityService.opQueryCommunities(communityCriteria, new fi.koku.services.entity.community.v1.AuditInfoType());
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
            if (member.getRole().equals("guardian")) {
              recipientPics.add(member.getPic());
            }
          }
        }
      }
    } else {
      
      MemberType familyMember = getFamily(userPic).getOtherParent(user.getPic());
      
      if (familyMember != null) {
        recipientPics.add(familyMember.getPic());
      }
      Family family = getFamily(targetSSN);
      
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
  public void insertDependantToFamily(String userPic, String dependantSSN, CommunityRole role) {
      Family family = getFamily(userPic);
      family.addFamilyMember(dependantSSN, role.getRoleID());
      
      try {
        communityService.opUpdateCommunity(family.getCommunity(), new fi.koku.services.entity.community.v1.AuditInfoType());
      } catch (ServiceFault fault) {
        log.error("PyhDemoService.insertDependantToFamily: opUpdateCommunity raised a ServiceFault", fault);
      }
  }
  
  /**
   * Removes a family member from a family community.
   */
  public void removeFamilyMember(String familyMemberSSN) {
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    // TODO: get community type from constants
    communityQueryCriteria.setCommunityType("family_community");
    communityQueryCriteria.setMemberPic(familyMemberSSN);
    
    // TODO: could this method be implemented somehow with opGetCommunity?
    
    CommunitiesType communitiesType = null;
    
    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, new fi.koku.services.entity.community.v1.AuditInfoType());
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
          if (member.getPic().equals(familyMemberSSN)) {
            members.remove(member);
            try {
              communityService.opUpdateCommunity(community, new fi.koku.services.entity.community.v1.AuditInfoType());
            } catch (ServiceFault fault) {
              log.error("PyhDemoService.removeFamilyMember: opUpdateCommunity raised a ServiceFault", fault);
            }
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
      String ssn = si.next();
      String role = personMap.get(ssn);
      
      Person person = getPerson(ssn);
      CommunityRole communityRole = CommunityRole.create(role);
      
      List<String> recipients = generateRecipients(ssn, user, communityRole);
      
      if (CommunityRole.PARENT.equals(communityRole) || CommunityRole.FATHER.equals(communityRole) || 
          CommunityRole.MOTHER.equals(communityRole)) {
        sendParentAdditionMessage(ssn, user, ssn, person, communityRole);
      } else if (recipients.size() == 0) {
        insertInto(userPic, ssn, communityRole);
      } else {
        sendFamilyAdditionMessage(recipients, user, ssn, person, communityRole);
      }
    }
  }
  
  /**
   * TODO: service implementation
   */
  private void sendParentAdditionMessage(String recipient, Person user, String ssn, Person person, CommunityRole r) {
    List<String> tmp = new ArrayList<String>();
    tmp.add(recipient);
    Message message = Message.createMessage(tmp, user.getPic(), ssn, person.getCapFullName()
        + " Uusi perheyhteystieto.", "Käyttäjä " + user.getFullName()
        + " on lisännyt sinut perheyhteisön toiseksi vanhemmaksi. "
        + "Hyväksymällä pyynnön perheyhteisönne yhdistetään automaattisesti.", new ParentExecutable(user.getPic(), ssn,
        r, this));
    messageService.addMessage(message);
  }
  
  /**
   * TODO: service implementation
   */
  private void sendFamilyAdditionMessage(List<String> recipients, Person user, String ssn, Person person, CommunityRole r) {
    Message message = Message.createMessage(recipients, user.getPic(), ssn, person.getCapFullName()
        + " Uusi perheyhteystieto.", "Käyttäjä " + user.getFullName() + " on lisännyt henkilön " + person.getFullName()
        + " perheyhteisön muuksi jäseneksi. "
        + "Kaikkien opsapuolten on hyväksyttävä uuden jäsenen liittäminen perheyhteisöön.",
        new FamilyExecutable(user.getPic(), ssn, r, this));
    messageService.addMessage(message);
  }
  
  /**
   * TODO: service implementation
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
    Family family = getFamily(toFamilyPic);
    family.addFamilyMember(personPic, role.getRoleID());
    
    try {
      communityService.opUpdateCommunity(family.getCommunity(), new fi.koku.services.entity.community.v1.AuditInfoType());
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.insertInto: opUpdateCommunity raised a ServiceFault", fault);
    }
  }
  
  /**
   * Inserts a parent (from other family) into a family and then combines the two families.
   */
  public void insertParentInto(String toFamilyPic, String personPic, CommunityRole role) {
    Family family = getFamily(toFamilyPic);
    Family combine = getFamily(personPic);
    
    family.combineFamily(combine);
    family.addFamilyMember(personPic, role.getRoleID());
    
    try {
      communityService.opUpdateCommunity(family.getCommunity(), new fi.koku.services.entity.community.v1.AuditInfoType());
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.insertParentInto: opUpdateCommunity raised a ServiceFault", fault);
    }
    
    removeFamily(combine);
  }
  
  /**
   * Removes a family community.
   */
  private void removeFamily(Family family) {
    try {
      communityService.opDeleteCommunity(family.getCommunityId(), new fi.koku.services.entity.community.v1.AuditInfoType());
    } catch (ServiceFault fault) {
      log.error("PyhDemoService.removeFamily: opDeleteCommunity raised a ServiceFault", fault);
    }
  }
  
  /**
   * Returns a family by person's PIC.
   *
   * TODO: miten ratkaistaan se, että tämä metodi voi palauttaa useamman kuin yhden perheen?
   */
  private Family getFamily(String pic) {
    List<Family> families = new ArrayList<Family>();
    CommunityQueryCriteriaType communityCriteria = new CommunityQueryCriteriaType();
    // TODO: get community type from constants
    communityCriteria.setCommunityType("family_community");
    communityCriteria.setMemberPic(pic);
    CommunitiesType communitiesType = null;
    
    try {
      communitiesType = communityService.opQueryCommunities(communityCriteria, new fi.koku.services.entity.community.v1.AuditInfoType());
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
      // TODO: families voi sisältää useamman kuin yhden perheen henkilön kuuluessa useaan perheeseen ->
      // mikä perhe tässä tilanteessa palautetaan?
      return families.get(0);
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
