package fi.koku.pyh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import fi.koku.pyh.model.Dependant;
import fi.koku.pyh.model.DependantsAndFamily;
import fi.koku.pyh.model.Family;
import fi.koku.pyh.model.FamilyIdAndFamilyMembers;
import fi.koku.pyh.model.FamilyMember;
import fi.koku.pyh.model.Message;
import fi.koku.pyh.model.Person;
import fi.koku.pyh.util.CommunityRole;
import fi.koku.services.entity.community.v1.CommunitiesType;
import fi.koku.services.entity.community.v1.CommunityQueryCriteriaType;
import fi.koku.services.entity.community.v1.CommunityServiceConstants;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.community.v1.MemberPicsType;
import fi.koku.services.entity.community.v1.MemberType;
import fi.koku.services.entity.community.v1.MembersType;
import fi.koku.services.entity.community.v1.MembershipApprovalType;
import fi.koku.services.entity.community.v1.MembershipApprovalsType;
import fi.koku.services.entity.community.v1.MembershipRequestQueryCriteriaType;
import fi.koku.services.entity.community.v1.MembershipRequestType;
import fi.koku.services.entity.community.v1.MembershipRequestsType;
import fi.koku.services.entity.community.v1.ServiceFault;
import fi.koku.services.entity.customer.v1.CustomerQueryCriteriaType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.CustomersType;
import fi.koku.services.entity.customer.v1.PicsType;

@Service(value = "pyhDemoService")
public class PyhDemoService {
  
  private static Logger log = LoggerFactory.getLogger(PyhDemoService.class);
  
  private CustomerServicePortType customerService;
  private CommunityServicePortType communityService;
  
  @Autowired
  private MailSender mailSender;
  
  @Autowired
  private SimpleMailMessage templateMessage;
  
  public PyhDemoService() {
    
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(PyhConstants.CUSTOMER_SERVICE_USER_ID, PyhConstants.CUSTOMER_SERVICE_PASSWORD, PyhConstants.CUSTOMER_SERVICE_ENDPOINT);
    customerService = customerServiceFactory.getCustomerService();
    
    CommunityServiceFactory communityServiceFactory = new CommunityServiceFactory(PyhConstants.COMMUNITY_SERVICE_USER_ID, PyhConstants.COMMUNITY_SERVICE_PASSWORD, PyhConstants.COMMUNITY_SERVICE_ENDPOINT);
    communityService = communityServiceFactory.getCommunityService();
  
  }
  
  /**
   * Returns a person by PIC.
   */
  public Person getPerson(String pic) {
    CustomerType customer = null;
    
    // TODO: check if user's pic is needed as parameter;
    // if not, this method should be always called with current user's pic as a parameter
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(pic);
    
    try {
      customer = customerService.opGetCustomer(pic, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("PyhDemoService.getUser: opGetCustomer raised a ServiceFault", fault);
      return null;
    }
    
    log.debug("getPerson(): returning customer: " + customer.getEtunimetNimi() + " " + customer.getSukuNimi() + ", " + customer.getHenkiloTunnus());
    log.debug("--");
    
    
    return new Person(customer);
  }
  
  public List<Person> getPersons(List<String> pics, String currentUserPic) {
    ArrayList<Person> persons = new ArrayList<Person>();
    
    if (pics == null || pics.size() == 0) {
      return persons;
    }
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(currentUserPic);
    
    CustomersType customersType = null;
    try {
      PicsType picsType = new PicsType();
      picsType.getPic().addAll(pics);
      CustomerQueryCriteriaType customerQueryCriteria = new CustomerQueryCriteriaType();
      customerQueryCriteria.setPics(picsType);
      customersType = customerService.opQueryCustomers(customerQueryCriteria, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("PyhDemoService.getUser: opGetCustomer raised a ServiceFault", fault);
      return persons;
    }
    
    if (customersType != null) {
      List<CustomerType> customers = customersType.getCustomer();
      Iterator<CustomerType> ci = customers.iterator();
      while (ci.hasNext()) {
        persons.add(new Person(ci.next()));
      }
    }
    
    return persons;
  }
  
  /**
   * Returns the current user.
   */
  public Person getUser(String userPic) {
    log.debug("getUser(): calling getPerson() with pic " + userPic);
    
    return getPerson(userPic);
  }
  
  /**
   * Returns user's dependants.
   */
  public DependantsAndFamily getDependantsAndFamily(String userPic) {
    List<Dependant> dependants = new ArrayList<Dependant>();
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    communityQueryCriteria.setCommunityType(CommunityServiceConstants.COMMUNITY_TYPE_GUARDIAN_COMMUNITY);
    
    MemberPicsType memberPics = new MemberPicsType();
    memberPics.getMemberPic().add(userPic);
    communityQueryCriteria.setMemberPics(memberPics);
    
    CommunitiesType communitiesType = null;
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userPic);
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userPic);
    
    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, communityAuditInfoType);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getDependantsAndFamily: opQueryCommunities raised a ServiceFault", fault);
    }
    
    ArrayList<String> depPics = new ArrayList<String>();
    
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
          if (member.getRole().equals(CommunityServiceConstants.COMMUNITY_ROLE_DEPENDANT)) {
            depPics.add(member.getPic());
          }
        }
      }
    }
    
    PicsType picsType = new PicsType();
    picsType.getPic().addAll(depPics);
    CustomerQueryCriteriaType customerQueryCriteriaType = new CustomerQueryCriteriaType();
    customerQueryCriteriaType.setPics(picsType);
    CustomersType customersType = null;
    try {
      customersType = customerService.opQueryCustomers(customerQueryCriteriaType, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("PyhDemoService.getDependantsAndFamily: opGetCustomer raised an ServiceFault", fault);
    }
    if (customersType != null) {
      List<CustomerType> customers = customersType.getCustomer();
      Iterator<CustomerType> ci = customers.iterator();
      while (ci.hasNext()) {
        CustomerType customer = ci.next();
        dependants.add(new Dependant(customer));
      }
    }
    
    // next check if dependant is member of user's family
    
    Family userFamily;
    try {
      userFamily = getFamily(userPic);
    } catch (FamilyNotFoundException fnfe) {
      userFamily = null;
      log.error("getDependantsAndFamily(): caught FamilyNotFoundException: cannot set Dependant.memberOfUserFamily because userFamily is null!");
      log.error(fnfe.getMessage());
    } catch (TooManyFamiliesException tmfe) {
      userFamily = null;
      log.error("getDependantsAndFamily(): caught TooManyFamiliesException: cannot set Dependant.memberOfUserFamily because userFamily is null!", tmfe);
      log.error(tmfe.getMessage());
    }
    
    DependantsAndFamily dependantsAndFamily = new DependantsAndFamily();
    
    if (userFamily != null) {
      Iterator<Dependant> di = dependants.iterator();
      while (di.hasNext()) {
        Dependant d = di.next();
        
        List<MemberType> members = userFamily.getAllMembers();
        Iterator<MemberType> mi = members.iterator();
        while (mi.hasNext()) {
          MemberType member = mi.next();
          // if dependant belongs to user's family then set Dependant.memberOfUserFamily
          if (d.getPic().equals(member.getPic())) {
            d.setMemberOfUserFamily(true);
          }
        }
      }
      dependantsAndFamily.setFamily(userFamily);
    }
    
    dependantsAndFamily.setDependants(dependants);
    
    if (log.isDebugEnabled()) {
      Iterator<Dependant> it = dependants.iterator();
      log.debug("getDependantsAndFamily(), returning dependants:");
      while (it.hasNext()) {
        log.debug("dep pic: " + it.next().getPic());
      }
      log.debug("--");
    }
    
    return dependantsAndFamily;
  }
  
  /**
   * Returns all other members of the user's family except dependants.
   */
  public FamilyIdAndFamilyMembers getOtherFamilyMembers(String userPic) {
    List<Dependant> dependants = getDependantsAndFamily(userPic).getDependants();
    Set<String> dependantPics = new HashSet<String>();
    Iterator<Dependant> di = dependants.iterator();
    while (di.hasNext()) {
      dependantPics.add(di.next().getPic());
    }
    
    List<FamilyMember> otherFamilyMembers = new ArrayList<FamilyMember>();
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    communityQueryCriteria.setCommunityType(CommunityServiceConstants.COMMUNITY_TYPE_FAMILY);
    
    MemberPicsType memberPics = new MemberPicsType();
    memberPics.getMemberPic().add(userPic);
    communityQueryCriteria.setMemberPics(memberPics);
    
    CommunitiesType communitiesType = null;
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userPic);
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(userPic);
    
    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, communityAuditInfoType);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getOtherFamilyMembers: opQueryCommunities raised a ServiceFault", fault);
    }
    
    String familyId = "";
    
    if (communitiesType != null) {
      List<CommunityType> communities = communitiesType.getCommunity();
      Iterator<CommunityType> ci = communities.iterator();
      
      List<String> otherFamilyMemberPics = new ArrayList<String>();
      List<String> otherFamilyMemberRoles = new ArrayList<String>();
      
      while (ci.hasNext()) {
        CommunityType community = ci.next();
        familyId = community.getId(); // communityService.opQueryCommunities should return only one community
        MembersType membersType = community.getMembers();
        List<MemberType> members = membersType.getMember();
        Iterator<MemberType> mi = members.iterator();
        while (mi.hasNext()) {
          MemberType member = mi.next();
          if (!dependantPics.contains(member.getPic()) && !userPic.equals(member.getPic())) {
            
            otherFamilyMemberPics.add(member.getPic());
            otherFamilyMemberRoles.add(member.getRole());
          }
        }
      }
      
      if (otherFamilyMemberPics.size() > 0) {
        CustomersType customersType = null;
        try {
          CustomerQueryCriteriaType customerCriteria = new CustomerQueryCriteriaType();
          PicsType picsType = new PicsType();
          picsType.getPic().addAll(otherFamilyMemberPics);
          customerCriteria.setPics(picsType);
          customerCriteria.setSelection("basic");
          customersType = customerService.opQueryCustomers(customerCriteria, customerAuditInfoType);
        } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
          log.error("PyhDemoService.getOtherFamilyMembers: opQueryCustomers raised a ServiceFault", fault);
        }
        
        if (customersType != null) {
          Iterator<CustomerType> customerIterator = customersType.getCustomer().iterator();
          Iterator<String> roleIterator = otherFamilyMemberRoles.iterator();
          while (customerIterator.hasNext()) {
            CustomerType customer = customerIterator.next();
            String role = roleIterator.next();
            otherFamilyMembers.add(new FamilyMember(customer, CommunityRole.createFromRoleID(role)));
          }
        }
      }
    }
    
    if (log.isDebugEnabled()) {
      Iterator<FamilyMember> it = otherFamilyMembers.iterator();
      log.debug("getOtherFamilyMembers(), returning members:");
      while (it.hasNext()) {
        log.debug("member pic: " + it.next().getPic());
      }
      log.debug("--");
    }
    
    FamilyIdAndFamilyMembers fidm = new FamilyIdAndFamilyMembers();
    fidm.setFamilyMembers(otherFamilyMembers);
    fidm.setFamilyId(familyId);
    return fidm;
  }
  
  /**
   * Checks if the user's family has max. number of parents.
   */
  public boolean isParentsSet(String userPic) {
    Family family = null;
    
    try {
      family = getFamily(userPic);
    } catch (TooManyFamiliesException tme) {
      log.error("PyhDemoService.isParentsSet(): getFamily(userPic) threw a TooManyFamiliesException!", tme);
      log.error(tme.getMessage());
    } catch (FamilyNotFoundException fnfe) {
      log.error("PyhDemoService.isParentsSet(): getFamily(userPic) threw a FamilyNotFoundException!");
      log.error(fnfe.getMessage());
    }
    
    if (family != null) {
      log.debug("isParentsSet(): returning " + family.isParentsSet());
      
      return family.isParentsSet();
    }
    
    log.debug("isParentsSet(): family == null, returning false");
    
    return false;
  }
  
  /**
   * Query persons by name, PIC and customer ID and returns results in a List<Person> list.
   */
  public List<Person> searchUsers(String surname, String customerPic, /*String customerID,*/ String currentUserPic) {
    
    // this search can return only one result because search criteria includes PIC
    
    CustomerQueryCriteriaType customerCriteria = new CustomerQueryCriteriaType();
    
    PicsType pics = new PicsType();
    pics.getPic().add(customerPic);
    customerCriteria.setPics(pics);
    CustomersType customersType = null;
    
    fi.koku.services.entity.customer.v1.AuditInfoType customerAuditInfoType = new fi.koku.services.entity.customer.v1.AuditInfoType();
    customerAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    customerAuditInfoType.setUserId(currentUserPic);
    
    try {
      customersType = customerService.opQueryCustomers(customerCriteria, customerAuditInfoType);
    } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
      log.error("PyhDemoService.searchUsers: opQueryCustomers raised a ServiceFault", fault);
    }
    
    Set<String> depPics = getDependantPics(currentUserPic);
    Set<String> familyMemberPics = getFamilyMemberPics(currentUserPic);
    
    List<Person> searchedUsers = new ArrayList<Person>();
    
    if (customersType != null) {
      List<CustomerType> customers = customersType.getCustomer();
      Iterator<CustomerType> ci = customers.iterator();
      while (ci.hasNext()) {
        CustomerType customer = ci.next();
        // filter out user and his/hers dependants from search results
        // surname given as search parameter must match the customer's surname (searching by PIC alone is forbidden)
        if (!depPics.contains(customer.getHenkiloTunnus()) && !familyMemberPics.contains(customer.getHenkiloTunnus()) &&
            !currentUserPic.equals(customer.getHenkiloTunnus()) && surname.equalsIgnoreCase(customer.getSukuNimi())) {
          searchedUsers.add(new Person(customer));
        }
      }
    }
    
    if (log.isDebugEnabled()) {
      log.debug("searchUsers(): searchedUsers contains:");
      Iterator<Person> pi = searchedUsers.iterator();
      while (pi.hasNext()) {
        Person p = pi.next();
        log.debug("person pic: " + p.getPic());
      }
    }
    
    return searchedUsers;
  }
  
  /**
   * Selects persons (PICs) to whom send the confirmation message for a operation, for example adding a dependant 
   * into a family.
   */
  private List<String> generateRecipients(String memberToAddPic, Person user, CommunityRole role, String currentUserPic) {
    List<String> recipientPics = new ArrayList<String>();
    
    if (CommunityRole.CHILD.equals(role)) {
      CommunityQueryCriteriaType communityCriteria = new CommunityQueryCriteriaType();
      communityCriteria.setCommunityType(CommunityServiceConstants.COMMUNITY_TYPE_GUARDIAN_COMMUNITY);
      
      MemberPicsType memberPics = new MemberPicsType();
      memberPics.getMemberPic().add(memberToAddPic);
      communityCriteria.setMemberPics(memberPics);
      
      CommunitiesType communitiesType = null;

      fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
      communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
      communityAuditInfoType.setUserId(currentUserPic);
      
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
            if (member.getRole().equals(CommunityServiceConstants.COMMUNITY_ROLE_GUARDIAN)) {
              recipientPics.add(member.getPic());
            }
          }
        }
      }
    } else {
      
      MemberType familyMember = null;
      
      try {
        familyMember = getFamily(currentUserPic).getOtherParent(user.getPic());
      } catch (TooManyFamiliesException tme) {
        log.error("PyhDemoService.generateRecipients(): getFamily(userPic) threw a TooManyFamiliesException!", tme);
        log.error(tme.getMessage());
      } catch (FamilyNotFoundException fnfe) {
        log.error("PyhDemoService.generateRecipients(): getFamily(userPic) threw a FamilyNotFoundException!");
        log.error(fnfe.getMessage());
      }
      
      if (familyMember != null) {
        recipientPics.add(familyMember.getPic());
      }
      Family family = null;
      
      try {
        family = getFamily(memberToAddPic);
      } catch (TooManyFamiliesException tme) {
        log.error("PyhDemoService.generateRecipients(): getFamily(targetPic) threw a TooManyFamiliesException!", tme);
        log.error(tme.getMessage());
      } catch (FamilyNotFoundException fnfe) {
        log.error("PyhDemoService.generateRecipients(): getFamily(targetPic) threw a FamilyNotFoundException!");
        log.error(fnfe.getMessage());
      }
      
      if (family != null) {
        for (MemberType member : family.getParents()) {
          if (!member.getPic().equals(user.getPic()) && !recipientPics.contains(member.getPic())) {
            recipientPics.add(member.getPic());
          }
        }
      }
    }
    
    if (log.isDebugEnabled()) {
      log.debug("generateRecipients(): returning pics:");
      Iterator<String> rpi = recipientPics.iterator();
      while (rpi.hasNext()) {
        log.debug("recipient pic: " + rpi.next());
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
        log.error("PyhDemoService.insertDependantToFamily: getFamily(userPic) threw a TooManyFamiliesException!", tme);
        log.error(tme.getMessage());
        return;
      } catch (FamilyNotFoundException fnfe) {
        log.error("PyhDemoService.insertDependantToFamily: getFamily(userPic) threw a FamilyNotFoundException!");
        log.error(fnfe.getMessage());
        return;
      }
      
      if (family != null) {
        family.addFamilyMember(dependantPic, role.getRoleID());
        
        fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
        communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
        communityAuditInfoType.setUserId(userPic);
        
        try {
          communityService.opUpdateCommunity(family.getCommunity(), communityAuditInfoType);
          Log.getInstance().update(userPic, "", "pyh.family.community", "Adding dependant " + dependantPic + " into family");
        } catch (ServiceFault fault) {
          log.error("PyhDemoService.insertDependantToFamily: opUpdateCommunity raised a ServiceFault", fault);
        }
        
        if (log.isDebugEnabled()) {
          log.debug("insertDependantToFamily(): family members after insert:");
          List<MemberType> members = family.getAllMembers();
          Iterator<MemberType> mi = members.iterator();
          while (mi.hasNext()) {
            MemberType m = mi.next();
            log.debug("member pic: " + m.getPic());
          }
        }
      }
  }
  
  /**
   * Removes a family member from a family community.
   */
  public void removeFamilyMember(String familyMemberPic, String userPic) {
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    communityQueryCriteria.setCommunityType(CommunityServiceConstants.COMMUNITY_TYPE_FAMILY);
    
    MemberPicsType memberPics = new MemberPicsType();
    memberPics.getMemberPic().add(familyMemberPic);
    communityQueryCriteria.setMemberPics(memberPics);
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userPic);
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
          if (member.getPic().equals(familyMemberPic) && isMemberOfCommunity(userPic, members)) {
            members.remove(member);
            try {
              
              // TODO: place outside the loop
              communityService.opUpdateCommunity(community, communityAuditInfoType);
              Log.getInstance().update(userPic, "", "pyh.family.community", "Removing family member " + familyMemberPic + " from family");
            } catch (ServiceFault fault) {
              log.error("PyhDemoService.removeFamilyMember: opUpdateCommunity raised a ServiceFault", fault);
            }
            
            if (log.isDebugEnabled()) {
              log.debug("removeFamilyMember(): members after removing:");
              Iterator<MemberType> mit = members.iterator();
              while (mit.hasNext()) {
                MemberType m = mit.next();
                log.debug("member pic: " + m.getPic());
              }
            }
            
            return;
          }
        }
      }
    }
  }
  
  /**
   * Returns true if person is member of a community. Otherwise returns false.
   * 
   * personPic - PIC of the person
   * members - list of members of the community
   */
  private boolean isMemberOfCommunity(String personPic, List<MemberType> members) {
    Iterator<MemberType> mi = members.iterator();
    while (mi.hasNext()) {
      MemberType member = mi.next();
      if (personPic.equals(member.getPic())) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Creates a recipient list for confirmation request message for adding persons as family members.
   * Then the message sending method is called or if there are no recipients then persons are added immediately.
   */
  public void addPersonsAsFamilyMembers(HashMap<String, String> personMap, String userPic, String communityId) 
    throws FamilyNotFoundException, GuardianForChildNotFoundException {
    
    // personMap parameter contains (personPIC, role) pairs
    
    if (log.isDebugEnabled()) {
      log.debug("addPersonsAsFamilyMembers(): adding persons:");
      Set<String> set = personMap.keySet();
      Iterator<String> it = set.iterator();
      while (it.hasNext()) {
        String personPic = it.next();
        log.debug("person pic: " + personPic);
      }
    }
    
    if (communityId == null) {
      throw new FamilyNotFoundException("PyhDemoService.addPersonsAsFamilyMembers: cannot add family members because family community does not exist!");
    }
    
    Set<String> keys = personMap.keySet();
    Iterator<String> si = keys.iterator();
    Person user = getPerson(userPic);
    while (si.hasNext()) {
      String memberToAddPic = si.next();
      String role = personMap.get(memberToAddPic);
      
      CommunityRole communityRole = CommunityRole.create(role);
      
      List<String> recipients = generateRecipients(memberToAddPic, user, communityRole, userPic/*current user's pic*/);
      
      if (CommunityRole.PARENT.equals(communityRole) || CommunityRole.FATHER.equals(communityRole) || 
          CommunityRole.MOTHER.equals(communityRole)) {
        sendParentAdditionMessage(communityId, memberToAddPic, userPic, communityRole);
      } else if (CommunityRole.CHILD.equals(communityRole) && recipients.size() == 0) {
        // we don't have guardian information for the child so we can't send the request
        
        // TODO: nämä tekstit Language-ext.properties
        String messageSubject = "KoKu: puutteelliset tiedot järjestelmässä";
        String messageText = "Järjestelmästä ei löydy lapsen huoltajatietoja. Lapsen HETU: " + memberToAddPic;
        
        SimpleMailMessage mailMessage = new SimpleMailMessage(this.templateMessage);
        mailMessage.setFrom(PyhConstants.KOKU_FROM_EMAIL_ADDRESS);
        mailMessage.setTo(PyhConstants.KOKU_SUPPORT_EMAIL_ADDRESS);
        mailMessage.setSubject(messageSubject);
        mailMessage.setText(messageText);
        try {
          this.mailSender.send(mailMessage);
        } catch (MailException me) {
          log.error("PyhDemoService.addPersonsAsFamilyMembers: sending mail to KoKu support failed!", me);
        }
        
        Log.getInstance().send(userPic, "", "pyh.membership.request", "Cannot send approval request because guardian for child " + memberToAddPic + " is not found");
        
        throw new GuardianForChildNotFoundException("Guardian for child (pic: " + memberToAddPic + ") not found!");
        
      } else if (recipients.size() == 0) {
        insertInto(userPic, memberToAddPic, communityRole);
      } else {
        sendFamilyAdditionMessage(communityId, recipients, userPic, memberToAddPic, communityRole);
      }
    }
  }
  
  /**
   * Sends a new membership request for adding a parent into a family. 
   * 
   */
  private void sendParentAdditionMessage(String communityId, String memberToAddPic, String requesterPic, CommunityRole role) {
    log.debug("calling sendParentAdditionMessage()");
    log.debug("communityId: " + communityId);
    log.debug("memberToAddPic: " + memberToAddPic);
    log.debug("requesterPic: " + requesterPic);
    log.debug("role: " + role.getRoleID());
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(requesterPic); // requesterPic is PIC of current user
    
    MembershipApprovalType membershipApproval = new MembershipApprovalType();
    membershipApproval.setApproverPic(memberToAddPic);
    membershipApproval.setStatus(CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_NEW);
    
    MembershipApprovalsType membershipApprovalsType = new MembershipApprovalsType();
    membershipApprovalsType.getApproval().add(membershipApproval);
    
    if (log.isDebugEnabled()) {
      log.debug("listing approvals:");
      Iterator<MembershipApprovalType> mi = membershipApprovalsType.getApproval().iterator();
      while (mi.hasNext()) {
        MembershipApprovalType approval = mi.next();
        log.debug("approval: " + approval.getApproverPic() + ", " + approval.getStatus());
      }
    }
    
    MembershipRequestType membershipRequest = new MembershipRequestType();
    membershipRequest.setCommunityId(communityId);
    membershipRequest.setMemberRole(role.getRoleID());
    membershipRequest.setMemberPic(memberToAddPic);
    membershipRequest.setRequesterPic(requesterPic);
    membershipRequest.setApprovals(membershipApprovalsType);
    
    try {
      communityService.opAddMembershipRequest(membershipRequest, communityAuditInfoType);
      Log.getInstance().send(requesterPic, "", "pyh.membership.request", "Sending membership request to add person " + memberToAddPic + " into family");
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.sendParentAdditionMessage: opAddMembershipRequest raised a ServiceFault", fault);
    }
  }
  
  /**
   * Sends a new membership request for adding a member (not parent) into a family.
   */
  private void sendFamilyAdditionMessage(String communityId, List<String> recipients, String requesterPic, String memberToAddPic, CommunityRole role) {
    if (log.isDebugEnabled()) {
      log.debug("calling sendFamilyAdditionMessage()");
      log.debug("communityId: " + communityId);
      log.debug("recipients:");
      Iterator<String> ri = recipients.iterator();
      while (ri.hasNext()) {
        String recipientPic = ri.next();
        log.debug("recipient pic: " + recipientPic);
      }
      log.debug("requesterPic: " + requesterPic);
      log.debug("memberToAddPic: " + memberToAddPic);
      log.debug("role: " + role.getRoleID());
    }
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(requesterPic); // requesterPic on kirjautunut käyttäjä
    
    MembershipApprovalsType membershipApprovalsType = new MembershipApprovalsType();
    
    Iterator<String> recipientsIterator = recipients.iterator();
    while (recipientsIterator.hasNext()) {
      String approverPic = recipientsIterator.next();
      
      MembershipApprovalType membershipApproval = new MembershipApprovalType();
      membershipApproval.setApproverPic(approverPic);
      membershipApproval.setStatus("new");
      
      membershipApprovalsType.getApproval().add(membershipApproval);
    }
    
    if (log.isDebugEnabled()) {
      log.debug("listing approvals:");
      Iterator<MembershipApprovalType> mi = membershipApprovalsType.getApproval().iterator();
      while (mi.hasNext()) {
        MembershipApprovalType approval = mi.next();
        log.debug("approval: " + approval.getApproverPic() + ", " + approval.getStatus());
      }
    }
    
    MembershipRequestType membershipRequest = new MembershipRequestType();
    membershipRequest.setCommunityId(communityId);
    membershipRequest.setMemberRole(role.getRoleID());
    membershipRequest.setMemberPic(memberToAddPic);
    membershipRequest.setRequesterPic(requesterPic);
    membershipRequest.setApprovals(membershipApprovalsType);
    
    try {
      communityService.opAddMembershipRequest(membershipRequest, communityAuditInfoType);
      Log.getInstance().send(requesterPic, "", "pyh.membership.request", "Sending membership request to add person " + memberToAddPic + " into family");
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.sendFamilyAdditionMessage: opAddMembershipRequest raised a ServiceFault", fault);
    }
  }
  
  /**
   * Inserts a new member into a family community.
   */
  public void insertInto(String toFamilyPic, String memberToAddPic, CommunityRole role) {
    Family family = null;
    
    try {
      family = getFamily(toFamilyPic);
    } catch (TooManyFamiliesException tme) {
      log.error("PyhDemoService.insertInto: getFamily(toFamilyPic) threw a TooManyFamiliesException!", tme);
      log.error(tme.getMessage());
      return;
    } catch (FamilyNotFoundException fnfe) {
      log.error("PyhDemoService.insertInto: getFamily(toFamilyPic) threw a FamilyNotFoundException!");
      log.error(fnfe.getMessage());
      return;
    }
    
    if (family != null) {
      family.addFamilyMember(memberToAddPic, role.getRoleID());
      
      fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
      communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
      communityAuditInfoType.setUserId(toFamilyPic); // toFamilyPic is current user's pic
      
      try {
        communityService.opUpdateCommunity(family.getCommunity(), communityAuditInfoType);
        Log.getInstance().update(toFamilyPic, "", "pyh.family.community", "Adding person " + memberToAddPic + " into family");
      } catch (ServiceFault fault) {
        log.error("PyhDemoService.insertInto: opUpdateCommunity raised a ServiceFault", fault);
      }
      
      if (log.isDebugEnabled()) {
        log.debug("insertInto(): members after insert:");
        List<MemberType> members = family.getAllMembers();
        Iterator<MemberType> mti = members.iterator();
        while (mti.hasNext()) {
          MemberType m = mti.next();
          log.debug("member pic: " + m.getPic());
        }
      }
    }
  }
  
  public String addFamily(String userPic) {
    log.debug("calling addFamily() with parameter:");
    log.debug("userPic: " + userPic);
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(userPic);
    
    MemberType member = new MemberType();
    member.setPic(userPic);
    member.setRole(CommunityServiceConstants.COMMUNITY_ROLE_PARENT);
    
    MembersType members = new MembersType();
    members.getMember().add(member);
    
    CommunityType community = new CommunityType();
    community.setMembers(members);
    community.setType(CommunityServiceConstants.COMMUNITY_TYPE_FAMILY);
    
    String communityId = null;
    try {
      communityId = communityService.opAddCommunity(community, communityAuditInfoType);
      Log.getInstance().update(userPic, "", "pyh.family.community", "Adding family for user " + userPic);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.addFamily: opAddCommunity raised a ServiceFault", fault);
    }
    return communityId;
  }
  
  /**
   * Returns a family by person's PIC.
   *
   * If the query returns more than one community (family) then TooManyFamiliesException is thrown.
   * A user should belong to one family only. Dependants are an exception because they can be members 
   * of one or more families. NOTE! The method parameter 'pic' should be guardian's or parent's pic.
   * 
   */
  private Family getFamily(String pic) throws TooManyFamiliesException, FamilyNotFoundException {
    List<Family> families = new ArrayList<Family>();
    CommunityQueryCriteriaType communityCriteria = new CommunityQueryCriteriaType();
    communityCriteria.setCommunityType(CommunityServiceConstants.COMMUNITY_TYPE_FAMILY);
    
    MemberPicsType memberPics = new MemberPicsType();
    memberPics.getMemberPic().add(pic);
    communityCriteria.setMemberPics(memberPics);
    
    CommunitiesType communitiesType = null;
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(pic);
    
    try {
      communitiesType = communityService.opQueryCommunities(communityCriteria, communityAuditInfoType);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getFamily: opQueryCommunities raised a ServiceFault", fault);
    }
    
    if (communitiesType != null) {
      List<CommunityType> communities = communitiesType.getCommunity();
      Iterator<CommunityType> ci = communities.iterator();
      
      if (!ci.hasNext()) {
        throw new FamilyNotFoundException("FamilyNotFoundException: family containing a member PIC " + pic + " not found!");
      }
      
      while (ci.hasNext()) { 
        CommunityType community = ci.next();
        families.add(new Family(community));
      }
      
      if (families.size() > 1) {
        throw new TooManyFamiliesException("opQueryCommunities with parameter 'pic=" + pic + "' returned more than one family!");
      } else if (families.size() > 0) {
        Family family = families.get(0);
        
        log.debug("getFamily(): returning family with community ID " + family.getCommunityId());
        
        return family;
      }
    }
    
    log.debug("getFamily(): returning null!");
    
    return null;
  }
  
  /**
   * Returns user's dependants' PICs.
   */
  private Set<String> getDependantPics(String userPic) {
    Set<String> dependantPics = new HashSet<String>();
    List<Dependant> dependants = getDependantsAndFamily(userPic).getDependants();
    Iterator<Dependant> di = dependants.iterator();
    while (di.hasNext()) {
      dependantPics.add(di.next().getPic());
    }
    return dependantPics;
  }
  
  /**
   * Returns user's family members' PICs.
   */
  private Set<String> getFamilyMemberPics(String userPic) {
    Set<String> familyMemberPics = new HashSet<String>();
    List<FamilyMember> familyMembers = getOtherFamilyMembers(userPic).getFamilyMembers();
    Iterator<FamilyMember> fmi = familyMembers.iterator();
    while (fmi.hasNext()) {
      familyMemberPics.add(fmi.next().getPic());
    }
    return familyMemberPics;
  }
  
  /**
   * Get membership requests which user has received (that is all requests user needs to approve or reject).
   * 
   */
  public List<Message> getMessagesFor(Person user) {
    
    List<Message> requestMessages = new ArrayList<Message>();
    
    if (user == null) {
      return requestMessages;
    }
    
    log.debug("calling getMessagesFor() with pic = " + user.getPic());
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(user.getPic());
    
    MembershipRequestQueryCriteriaType membershipRequestQueryCriteria = new MembershipRequestQueryCriteriaType();
    membershipRequestQueryCriteria.setApproverPic(user.getPic());
    MembershipRequestsType membershipRequestsType = null;
    
    try {
      membershipRequestsType = communityService.opQueryMembershipRequests(membershipRequestQueryCriteria, communityAuditInfoType);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getMessagesFor: opQueryMembershipRequests raised a ServiceFault", fault);
    }
    
    if (membershipRequestsType != null) {
      List<String> memberToAddPics = new ArrayList<String>();
      List<String> messageIds = new ArrayList<String>();
      List<String> senderPics = new ArrayList<String>();
      List<String> userApprovalStatusList = new ArrayList<String>();
      
      List<MembershipRequestType> membershipRequests = membershipRequestsType.getMembershipRequest();
      Iterator<MembershipRequestType> mrti = membershipRequests.iterator();
      // iterate through membership requests
      while (mrti.hasNext()) {
        MembershipRequestType membershipRequest = mrti.next();
        memberToAddPics.add(membershipRequest.getMemberPic());
        messageIds.add(membershipRequest.getId());
        senderPics.add(membershipRequest.getRequesterPic());
        
        MembershipApprovalsType membershipApprovalsType = membershipRequest.getApprovals();
        List<MembershipApprovalType> approvals = membershipApprovalsType.getApproval();
        Iterator<MembershipApprovalType> ait = approvals.iterator();
        while (ait.hasNext()) {
          MembershipApprovalType approval = ait.next();
          String approverPic = approval.getApproverPic();
          
          // there should be match because MembershipRequestQueryCriteriaType.approverPic was set to user's pic
          if (approverPic.equals(user.getPic())) {
            userApprovalStatusList.add(approval.getStatus());
            break;
          }
        }
      }
      
      // reason for two loops is that the WS services are called only once (for better performance)
      
      List<Person> requestSenders = getPersons(senderPics, user.getPic());
      List<Person> targetPersons = getPersons(memberToAddPics, user.getPic());
      Iterator<Person> requestSenderIterator = requestSenders.iterator();
      Iterator<Person> targetPersonIterator = targetPersons.iterator();
      
      Iterator<String> messageIdIterator = messageIds.iterator();
      Iterator<String> memberToAddIterator = memberToAddPics.iterator();
      Iterator<String> messageSenderIterator = senderPics.iterator();
      Iterator<String> approvalStatusIterator = userApprovalStatusList.iterator();
      
      boolean userFamilyHasTwoParents = isParentsSet(user.getPic());
      
      while (messageIdIterator.hasNext()) {
        String messageId = messageIdIterator.next();
        String memberToAddPic = memberToAddIterator.next();
        String senderPic = messageSenderIterator.next();
        String userApprovalStatus = approvalStatusIterator.next();
        
        if (CommunityServiceConstants.MEMBERSHIP_REQUEST_STATUS_NEW.equals(userApprovalStatus)) {
          
          Person requestSender = requestSenderIterator.next();
          Person targetPerson = targetPersonIterator.next();
          String senderName = "";
          String targetName = "";
          
          if (requestSender != null) {
            senderName = requestSender.getFullName();
          }
          
          if (targetPerson != null) {
            targetName = targetPerson.getFullName();
          }
          
          boolean twoParentsInFamily = false;
          if (memberToAddPic.equals(user.getPic())) {
            // twoParentsInFamily is set if there are two (or more; this is theoretical, 
            // shouldn't be possible) parents in the family
            twoParentsInFamily = userFamilyHasTwoParents;
          }
          
          String messageText = "";
          if (twoParentsInFamily) {
            messageText = "Uusi perheyhteyspyyntö: " + senderName + 
            " on lisäämässä sinua perheyhteisönsä jäseneksi, mutta et voi hyväksyä pyyntöä, koska perheessänne on jo kaksi vanhempaa. " +
            "Voit hylätä pyynnön tai poistaa toisen vanhemman perheyhteisöstäsi, minkä jälkeen voit hyväksyä pyynnön.";
          }
          else {
            messageText = "Saapunut perheyhteyspyyntö: " + senderName + " haluaa lisätä perheyhteisön jäseneksi: " + targetName + ".\n" +
              "Pyynnön hyväksymällä tietojen lisääminen tapahtuu automaattisesti tämän verkkopalvelun tietoihin.";
          }
          
          Message message = new Message(messageId, senderPic, memberToAddPic, messageText, twoParentsInFamily);
          requestMessages.add(message);
        }
      }
    }
    return requestMessages;
  }
  
  /**
   * Get membership requests which the user has sent.
   * 
   */
  public List<Message> getSentMessages(Person user) {
    
    List<Message> requestMessages = new ArrayList<Message>();
    
    if (user == null) {
      return requestMessages;
    }
    
    log.debug("calling getSentMessages() with pic = " + user.getPic());
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(user.getPic());
    
    MembershipRequestQueryCriteriaType membershipRequestQueryCriteria = new MembershipRequestQueryCriteriaType();
    membershipRequestQueryCriteria.setRequesterPic(user.getPic());
    MembershipRequestsType membershipRequestsType = null;
    
    try {
      membershipRequestsType = communityService.opQueryMembershipRequests(membershipRequestQueryCriteria, communityAuditInfoType);
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.getSentMessages: opQueryMembershipRequests raised a ServiceFault", fault);
    }
    
    if (membershipRequestsType != null) {
      ArrayList<String> memberToAddPics = new ArrayList<String>();
      ArrayList<String> messageIds = new ArrayList<String>();
      ArrayList<String> senderPics = new ArrayList<String>();
      
      List<MembershipRequestType> membershipRequests = membershipRequestsType.getMembershipRequest();
      Iterator<MembershipRequestType> mrti = membershipRequests.iterator();
      MembershipRequestType membershipRequest = null;
      while (mrti.hasNext()) {
        membershipRequest = mrti.next();
        memberToAddPics.add(membershipRequest.getMemberPic());
        messageIds.add(membershipRequest.getId());
        senderPics.add(membershipRequest.getRequesterPic());
      }
      
      List<Person> membersToAdd = getPersons(memberToAddPics, user.getPic());
      Iterator<Person> pi = membersToAdd.iterator();
      Iterator<String> messageIdIt = messageIds.iterator();
      Iterator<String> senderPicIt = senderPics.iterator();
      
      String targetPersonName = "";
      String messageId = "";
      String senderPic = "";
      while (pi.hasNext()) {
        Person targetPerson = pi.next();
        targetPersonName = targetPerson.getFullName();
        messageId = messageIdIt.next();
        senderPic = senderPicIt.next();
        
        String messageText = "Lisäys perheyhteystietoihisi: " + targetPersonName + " (odottaa vastaanottajan hyväksyntää)";
        Message message = new Message(messageId, senderPic, "" /*memberToAddPic*/, messageText, false);
        requestMessages.add(message);
      }
    }
    
    return requestMessages;
  }
  
  /**
   * Sets membership request as accepted or rejected. If accepted, adding a member into a family is done by the service after  
   * changing the request status.
   * 
   */
  public void acceptOrRejectMembershipRequest(String membershipRequestId, String approverPic, String status, String familyId) {
    log.debug("calling PyhDemoService.acceptOrRejectMembershipRequest with parameters:");
    log.debug("membershipRequestId: " + membershipRequestId);
    log.debug("approverPic: " + approverPic);
    log.debug("status: " + status);
    
    MembershipApprovalType membershipApproval = new MembershipApprovalType();
    membershipApproval.setApproverPic(approverPic);
    membershipApproval.setMembershipRequestId(membershipRequestId);
    membershipApproval.setStatus(status);
    
    fi.koku.services.entity.community.v1.AuditInfoType communityAuditInfoType = new fi.koku.services.entity.community.v1.AuditInfoType();
    communityAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    communityAuditInfoType.setUserId(approverPic); // approver is the current user
    
    try {
      communityService.opUpdateMembershipApproval(membershipApproval, communityAuditInfoType);
      Log.getInstance().update(approverPic, "", "pyh.membership.approval", "Membership approval status for user " + approverPic + " was set to '" + status + "'");
      
      if (familyId != null) {
        communityService.opDeleteCommunity(familyId, communityAuditInfoType);
        Log.getInstance().update(approverPic, "", "pyh.family.community", "Removing family " + familyId);
        
      }
      
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      log.error("PyhDemoService.acceptMembershipRequest: opUpdateMembershipApproval raised a ServiceFault", fault);
    }
    
  }
  
  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void setTemplateMessage(SimpleMailMessage templateMessage) {
    this.templateMessage = templateMessage;
  }
  
}
