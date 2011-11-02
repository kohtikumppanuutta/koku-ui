/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.koku.calendar.CalendarUtil;
import fi.koku.kks.ui.common.utils.CollectionComparator;
import fi.koku.kks.ui.common.utils.Constants;
import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.services.entity.community.v1.CommunitiesType;
import fi.koku.services.entity.community.v1.CommunityQueryCriteriaType;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.community.v1.MemberPicsType;
import fi.koku.services.entity.community.v1.MemberType;
import fi.koku.services.entity.community.v1.MembersType;
import fi.koku.services.entity.customer.v1.CustomerQueryCriteriaType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;
import fi.koku.services.entity.customer.v1.CustomersType;
import fi.koku.services.entity.customer.v1.PicsType;
import fi.koku.services.entity.family.v1.FamilyService;
import fi.koku.services.entity.kks.v1.KksCollectionClassType;
import fi.koku.services.entity.kks.v1.KksCollectionCreationCriteriaType;
import fi.koku.services.entity.kks.v1.KksCollectionStateCriteriaType;
import fi.koku.services.entity.kks.v1.KksCollectionType;
import fi.koku.services.entity.kks.v1.KksCollectionsCriteriaType;
import fi.koku.services.entity.kks.v1.KksCollectionsType;
import fi.koku.services.entity.kks.v1.KksEntryClassType;
import fi.koku.services.entity.kks.v1.KksEntryCriteriaType;
import fi.koku.services.entity.kks.v1.KksEntryValueType;
import fi.koku.services.entity.kks.v1.KksGroupType;
import fi.koku.services.entity.kks.v1.KksQueryCriteriaType;
import fi.koku.services.entity.kks.v1.KksServiceFactory;
import fi.koku.services.entity.kks.v1.KksServicePortType;
import fi.koku.services.entity.kks.v1.KksTagNamesType;
import fi.koku.services.entity.kks.v1.KksType;
import fi.koku.services.entity.kks.v1.ServiceFault;
import fi.koku.services.entity.person.v1.PersonConstants;
import fi.koku.services.entity.person.v1.PersonService;
import fi.koku.services.entity.tiva.v1.Consent;
import fi.koku.services.entity.tiva.v1.ConsentTemplate;
import fi.koku.services.entity.tiva.v1.GivenTo;
import fi.koku.services.entity.tiva.v1.KokuTivaToKksService;
import fi.koku.services.utility.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.utility.authorizationinfo.v1.AuthorizationInfoServiceFactory;
import fi.koku.services.utility.authorizationinfo.v1.model.OrgUnit;
import fi.koku.services.utility.authorizationinfo.v1.model.Registry;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Handles the services that are needed for KKS
 * 
 * @author tuomape
 * 
 */
@Service(value = "kksService")
public class KksService {

  private static final Logger LOG = LoggerFactory.getLogger(KksService.class);

  private Map<String, KksEntryClassType> entryClasses;
  private Map<String, KksCollectionClassType> collectionClasses;
  private KksConverter converter;
  private KksServicePortType kksService;
  private CommunityServicePortType communityService;
  private CustomerServicePortType customerService;
  private FamilyService familyService;
  private KokuTivaToKksService tivaService;
  private AuthorizationInfoService authorizationService;
  private PersonService personService;

  public KksService() {
    entryClasses = new HashMap<String, KksEntryClassType>();
    collectionClasses = new HashMap<String, KksCollectionClassType>();
    converter = new KksConverter(this);
    kksService = getKksService();
    communityService = getCommunityService();
    customerService = getCustomerService();
    familyService = getFamilyService();
    authorizationService = getAuthorizationService();
    tivaService = getTivaService();
    personService = new PersonService();
  }

  public fi.koku.services.entity.customer.v1.AuditInfoType getCustomerAuditInfo(String user) {
    fi.koku.services.entity.customer.v1.AuditInfoType a = new fi.koku.services.entity.customer.v1.AuditInfoType();
    a.setComponent(Constants.COMPONENT_KKS);
    a.setUserId(user);
    return a;
  }

  public fi.koku.services.entity.community.v1.AuditInfoType getCommynityAuditInfo(String user) {
    fi.koku.services.entity.community.v1.AuditInfoType a = new fi.koku.services.entity.community.v1.AuditInfoType();
    a.setComponent(Constants.COMPONENT_KKS);
    a.setUserId(user);
    return a;
  }

  public fi.koku.services.entity.kks.v1.AuditInfoType getKksAuditInfo(String user) {
    fi.koku.services.entity.kks.v1.AuditInfoType a = new fi.koku.services.entity.kks.v1.AuditInfoType();
    a.setComponent(Constants.COMPONENT_KKS);
    a.setUserId(user);
    return a;
  }

  public Map<String, Registry> getAuthorizedRegistries(String user) {
    Map<String, Registry> tmp = new HashMap<String, Registry>();
    List<Registry> register = authorizationService.getUsersAuthorizedRegistries(user);

    if (register != null) {
      for (Registry r : register) {
        tmp.put(r.getId(), r);
      }
    }
    return tmp;
  }

  private KksServicePortType getKksService() {
    KksServiceFactory kksServiceFactory = new KksServiceFactory(Constants.KKS_SERVICE_USER_ID,
        Constants.KKS_SERVICE_PASSWORD, Constants.ENDPOINT);
    return kksServiceFactory.getKksService();
  }

  private CommunityServicePortType getCommunityService() {
    CommunityServiceFactory csf = new CommunityServiceFactory(Constants.COMMUNITY_SERVICE_USER_ID,
        Constants.COMMUNITY_SERVICE_PASSWORD, Constants.COMMUNITY_ENDPOINT);
    return csf.getCommunityService();
  }

  private CustomerServicePortType getCustomerService() {
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(Constants.CUSTOMER_SERVICE_USER_ID,
        Constants.CUSTOMER_SERVICE_PASSWORD, Constants.CUSTOMER_ENDPOINT);
    return customerServiceFactory.getCustomerService();
  }

  public KksEntryClassType getEntryClassType(String id, String user) {
    if (entryClasses.isEmpty()) {
      collectMetadata(user);
    }
    return entryClasses.get(id);
  }

  public KksCollectionClassType getCollectionClassType(String id, String user) {
    if (collectionClasses.isEmpty()) {
      collectMetadata(user);
    }
    return collectionClasses.get(id);
  }

  public void clearMetadata() {
    entryClasses.clear();
    collectionClasses.clear();
  }

  public List<KKSCollection> getKksCollections(String pic, String user) {
    List<KKSCollection> tmp = new ArrayList<KKSCollection>();
    try {

      KksCollectionsCriteriaType criteria = new KksCollectionsCriteriaType();
      criteria.setPic(pic);
      criteria.setKksScope("minimum");
      KksType kks = kksService.opGetKks(criteria, getKksAuditInfo(user));
      List<KksCollectionType> collections = kks.getKksCollections().getKksCollection();

      if (collections != null) {

        for (KksCollectionType kct : collections) {
          tmp.add(converter.fromWsType(kct, user));
        }
      }

      setCollectionCreators(user, tmp);
      Collections.sort(tmp, new CollectionComparator());

    } catch (ServiceFault e) {
      LOG.error("Failed to get KKS collections", e);
    }

    return tmp;
  }

  private void setCollectionCreators(String user, List<KKSCollection> tmp) {

    List<String> creators = new ArrayList<String>();

    for (KKSCollection c : tmp) {
      creators.add(c.getCreator());
    }

    Map<String, String> personMap = getPersonsMap(user, creators);

    for (KKSCollection c : tmp) {
      c.setModifierFullName(personMap.get(c.getCreator()));
    }
  }

  private Map<String, String> getPersonsMap(String user, List<String> creators) {
    List<fi.koku.services.entity.person.v1.Person> persons = getPersonsFromService(user, creators);

    Map<String, String> personMap = new HashMap<String, String>();
    if (persons != null) {
      for (fi.koku.services.entity.person.v1.Person p : persons) {
        personMap.put(p.getPic(), p.getFname() + " " + p.getSname());
      }
    }
    return personMap;
  }

  private List<fi.koku.services.entity.person.v1.Person> getPersonsFromService(String user, List<String> creators) {
    // currently only domain officer can give comments ad create collections
    String domain = PersonConstants.PERSON_SERVICE_DOMAIN_OFFICER;
    return personService.getPersonsByPics(creators, domain, user, "KKS");
  }

  public KKSCollection getKksCollection(String collectionId, UserInfo info) {

    if (info == null) {
      return null;
    }

    try {
      KksCollectionType kks = kksService.opGetKksCollection(collectionId, getKksAuditInfo(info.getPic()));

      KKSCollection col = converter.fromWsType(kks, info.getPic());
      col.setMaster(isParent(info.getPic(), col.getCustomer()));
      col.setAuthorizedRegistrys(getAuthorizedRegistries(info.getPic()));
      setEntryModifiers(info, col);
      return col;
    } catch (ServiceFault e) {
      LOG.error("Failed to get KKS collection " + collectionId, e);
    }
    return null;
  }

  private void setEntryModifiers(UserInfo info, KKSCollection col) {
    Set<String> creators = new HashSet<String>();
    for (Entry e : col.getEntries().values()) {

      if (e.getType().isMultiValue()) {
        for (EntryValue v : e.getEntryValues()) {
          creators.add(v.getModifier());
        }
      }
    }

    Map<String, String> personMap = getPersonsMap(info.getPic(), new ArrayList<String>(creators));

    for (Entry e : col.getEntries().values()) {

      if (e.getType().isMultiValue()) {
        for (EntryValue v : e.getEntryValues()) {
          v.setModifierFullName(personMap.get(v.getModifier()));
        }
      }
    }
  }

  public boolean updateKksCollection(KKSCollection collection, String customer, String user) {

    if (collection == null) {
      return false;
    }
    try {
      collection.setCreator(user);
      kksService.opUpdateKksCollection(converter.toWsType(collection, customer, user), getKksAuditInfo(user));
    } catch (ServiceFault e) {
      LOG.error("Failed to update KKS collection " + collection.getId(), e);
      return false;
    }
    return true;
  }

  public boolean updateKksCollectionStatus(String customer, String collectionId, String status, String user) {
    try {
      KksCollectionStateCriteriaType state = new KksCollectionStateCriteriaType();
      state.setCollectionId(collectionId);
      state.setState(status);
      state.setCustomer(customer);
      kksService.opUpdateKksCollectionStatus(state, getKksAuditInfo(user));
    } catch (ServiceFault e) {
      LOG.error("Failed to update KKS collection status " + collectionId + " : " + status, e);
      return false;
    }
    return true;
  }

  public String createKksCollection(String name, String type, String customer, String user) {
    try {
      KksCollectionCreationCriteriaType kksCollectionCreationCriteria = new KksCollectionCreationCriteriaType();
      kksCollectionCreationCriteria.setCollectionName(name);
      kksCollectionCreationCriteria.setPic(customer);
      kksCollectionCreationCriteria.setCollectionTypeId(type);
      kksCollectionCreationCriteria.setKksScope("new");
      return kksService.opAddKksCollection(kksCollectionCreationCriteria, getKksAuditInfo(user)).getId();
    } catch (ServiceFault e) {
      LOG.error("Failed to create KKS collection " + name, e);
    }
    return null;
  }

  public String createKksCollectionVersion(String name, String type, String customer, boolean empty, String user) {
    try {
      KksCollectionCreationCriteriaType kksCollectionCreationCriteria = new KksCollectionCreationCriteriaType();
      kksCollectionCreationCriteria.setCollectionName(name);
      kksCollectionCreationCriteria.setPic(customer);
      kksCollectionCreationCriteria.setCollectionTypeId(type);

      if (empty) {
        kksCollectionCreationCriteria.setKksScope("empty_version");
      } else {
        kksCollectionCreationCriteria.setKksScope("version");
      }

      return kksService.opAddKksCollection(kksCollectionCreationCriteria, getKksAuditInfo(user)).getId();
    } catch (ServiceFault e) {
      LOG.error("Failed to create KKS collection version " + name + " type: " + type, e);
    }
    return null;
  }

  public List<KKSCollection> searchKksCollections(List<String> tagNames, String customer, String user) {
    List<KKSCollection> tmp = new ArrayList<KKSCollection>();

    try {
      KksQueryCriteriaType kksQueryCriteria = new KksQueryCriteriaType();
      kksQueryCriteria.setPic(customer);
      KksTagNamesType names = new KksTagNamesType();
      names.getKksTagName().addAll(tagNames);
      kksQueryCriteria.setKksTagNames(names);
      KksCollectionsType collections = kksService.opQueryKks(kksQueryCriteria, getKksAuditInfo(user));

      for (KksCollectionType type : collections.getKksCollection()) {
        tmp.add(converter.fromWsType(type, user));
      }

      for (KKSCollection collection : tmp) {
        List<Entry> entries = new ArrayList<Entry>(collection.getEntryValues());

        if (!isMaster(customer, user, collection)) {
          collection.clearEntries();
          Map<String, Registry> reg = getAuthorizedRegistries(user);
          KksCollectionClassType cc = collection.getCollectionClass();

          Map<String, KksGroupType> groups = new HashMap<String, KksGroupType>();

          for (KksGroupType i : cc.getKksGroups().getKksGroup()) {
            groups.put(i.getId(), i);

            for (KksGroupType ii : i.getSubGroups().getKksGroup()) {
              groups.put(ii.getId(), ii);
            }
          }
          for (Entry e : entries) {
            KksGroupType gt = groups.get(e.getType().getGroupId());

            if (reg.containsKey(gt.getRegister())) {
              collection.addEntry(e);
            }
          }
        }

      }

    } catch (ServiceFault e) {
      LOG.error("Failed to search KKS collections", e);
    }
    Collections.sort(tmp, new CollectionComparator());
    return tmp;
  }

  private boolean isMaster(String customer, String user, KKSCollection collection) {
    return collection.getCreator().equals(user) || isParent(user, customer);
  }

  public String addKksEntry(String collectionId, String customer, String entryId, String entryClassId, String valueId,
      String value, String user) {
    try {
      KksEntryCriteriaType criteria = new KksEntryCriteriaType();
      criteria.setEntryId(entryId);
      criteria.setPic(customer);
      KksEntryValueType evt = new KksEntryValueType();
      evt.setId(valueId);
      evt.setValue(value);
      criteria.setValue(evt);
      criteria.setCollectionId(collectionId);
      criteria.setCreator(user);
      criteria.setEntryClassId(entryClassId);
      criteria.setModified(CalendarUtil.getXmlDate(new Date()));
      return kksService.opAddEntry(criteria, getKksAuditInfo(user)).getId();
    } catch (ServiceFault e) {
      LOG.error("Failed to add KKS entry " + entryId, e);
    }
    return null;
  }

  public boolean removeKksEntry(String collection, String customer, String entryId, String valueId, String value,
      String user) {
    try {
      KksEntryCriteriaType criteria = new KksEntryCriteriaType();
      criteria.setEntryId(entryId);
      criteria.setPic(customer);
      criteria.setCollectionId(collection);
      KksEntryValueType evt = new KksEntryValueType();
      evt.setId(valueId);
      evt.setValue(value);
      criteria.setValue(evt);
      kksService.opDeleteEntry(criteria, getKksAuditInfo(user));
    } catch (ServiceFault e) {
      LOG.error("Failed to remove KKS entry " + entryId, e);
      return false;
    }
    return true;
  }

  private void collectMetadata(String user) {
    try {
      List<KksCollectionClassType> classes = kksService.opGetKksCollectionClasses("all", getKksAuditInfo(user))
          .getKksCollectionClass();

      if (classes != null) {
        for (KksCollectionClassType kcc : classes) {
          collectionClasses.put(kcc.getId(), kcc);

          for (KksGroupType gt : kcc.getKksGroups().getKksGroup()) {
            for (KksEntryClassType ect : gt.getKksEntryClasses().getKksEntryClass()) {
              entryClasses.put(ect.getId(), ect);
            }

            for (KksGroupType sgt : gt.getSubGroups().getKksGroup()) {
              for (KksEntryClassType sect : sgt.getKksEntryClasses().getKksEntryClass()) {
                entryClasses.put(sect.getId(), sect);
              }
            }
          }
        }
      }

    } catch (ServiceFault e) {
      LOG.error("Failed to collect KKS metadata ", e);
    }
  }

  public List<Person> searchChilds(String user) {
    return getChilds(user);
  }

  public List<Person> getChilds(String pic) {
    List<Person> childs = new ArrayList<Person>();
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    communityQueryCriteria.setCommunityType(Constants.COMMUNITY_TYPE_GUARDIAN_COMMUNITY);
    MemberPicsType mpt = new MemberPicsType();
    mpt.getMemberPic().add(pic);
    communityQueryCriteria.setMemberPics(mpt);
    CommunitiesType communitiesType = null;

    try {
      communitiesType = communityService.opQueryCommunities(communityQueryCriteria, getCommynityAuditInfo(pic));
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      LOG.error("Failed to get communities", fault);
    }

    List<String> pics = new ArrayList<String>();
    if (communitiesType != null) {
      List<CommunityType> communities = communitiesType.getCommunity();
      for (CommunityType community : communities) {
        MembersType membersType = community.getMembers();
        List<MemberType> members = membersType.getMember();

        for (MemberType member : members) {
          if (member.getRole().equals(Constants.ROLE_DEPENDANT)) {
            pics.add(member.getPic());
          }
        }
      }
    }

    if (pics.size() > 0) {
      try {
        CustomerQueryCriteriaType criteria = new CustomerQueryCriteriaType();
        PicsType pt = new PicsType();
        pt.getPic().addAll(pics);
        criteria.setPics(pt);
        CustomersType customers = customerService.opQueryCustomers(criteria, getCustomerAuditInfo(pic));
        for (CustomerType customer : customers.getCustomer()) {
          childs.add(Person.fromCustomerType(customer));
        }
      } catch (fi.koku.services.entity.customer.v1.ServiceFault e) {
        LOG.error("Failed to get customers", e);
      }
    }
    return childs;
  }

  public boolean isParent(String user, String child) {
    List<Person> tmp = getChilds(user);

    for (Person p : tmp) {
      if (p.getPic().equals(child)) {
        return true;
      }
    }
    return false;
  }

  public Person searchPerson(Person target, String user) {
    try {
      CustomerType t = customerService.opGetCustomer(target.getPic().trim(), getCustomerAuditInfo(user));
      if (t != null) {
        return Person.fromCustomerType(t);
      }
    } catch (fi.koku.services.entity.customer.v1.ServiceFault e) {
      LOG.error("Failed to fetch customer details", e);
    }
    return null;
  }

  public Person searchCustomer(String socialSecurityNumber, String user) {
    try {
      CustomerType t = customerService.opGetCustomer(socialSecurityNumber.trim(), getCustomerAuditInfo(user));
      if (t != null) {
        return Person.fromCustomerType(t);
      }
    } catch (fi.koku.services.entity.customer.v1.ServiceFault e) {
      LOG.error("Failed to fetch customer details", e);
    }
    return null;
  }

  public List<Creatable> searchPersonCreatableCollections(Person h, String user) {
    if (collectionClasses.isEmpty()) {
      collectMetadata(user);
    }

    List<Creatable> creatables = new ArrayList<Creatable>();

    for (KksCollectionClassType ct : collectionClasses.values()) {
      creatables.add(new Creatable("" + ct.getId(), false, ct.getName()));
    }

    return creatables;
  }

  /**
   * Creates consent request for given customer and consentType
   * 
   * @param customer
   * @param user
   *          that requests the consent
   * @param consentType
   *          that is requested (example
   *          kks.suostumus.4-vuotiaan.neuvolatarkastus)
   * @return true if request is success false if failed
   */
  public boolean sendConsentRequest(String consentType, String customerId, String user) {

    try {

      List<ConsentTemplate> templates = tivaService.queryConsentTemplates(consentType, 1);

      if (templates.size() == 0) {
        LOG.error("No TIVA templates found for constentType " + consentType);
        return false;
      }

      ConsentTemplate consentTemplate = templates.get(0);

      Consent consent = new Consent();
      consent.setTargetPerson(customerId);
      consent.setTemplate(consentTemplate);
      consent.setConsentRequestor(user);
      List<String> guardians = getCustomerGuardians(customerId, user);
      if (guardians == null) {
        LOG.error("No guardians found for customer");
        return false;
      }

      consent.getGivenTo().addAll(getConsentOrganizations(user, consentType));
      consent.getConsentProviders().addAll(guardians);
      tivaService.createConsent(consent);
    } catch (Exception e) {
      LOG.error("Cannot use TIVA service", e);
      return false;
    }
    return true;
  }

  private List<String> getCustomerGuardians(String customer, String user) {
    List<String> names = new ArrayList<String>();
    try {
      List<CustomerType> tmp = familyService.getPersonsParents(customer, user, "KKS");
      for (CustomerType ct : tmp) {
        names.add(ct.getHenkiloTunnus());
      }
    } catch (Exception e) {
      LOG.error("Failed to use familyservice", e);
    }
    return names;
  }

  private List<GivenTo> getConsentOrganizations(String user, String concentType ) {
    String consentOrganizations =  KoKuPropertiesUtil.get(concentType);
    List<GivenTo> orgNames = new ArrayList<GivenTo>();
    
    if (consentOrganizations != null ) {
      String orgs[] = consentOrganizations.split(";");  
      for (String line : orgs ) {
        String tmp[] = line.split(",");
        GivenTo gt = new GivenTo();
        gt.setPartyId(tmp[0]);
        gt.setPartyName(tmp[1]);
        orgNames.add(gt);
      }
    }
    return orgNames;
  }

  private KokuTivaToKksService getTivaService() {
    ConsentServiceFactory csf = new ConsentServiceFactory();
    return csf.getService();
  }

  private AuthorizationInfoService getAuthorizationService() {
    AuthorizationInfoServiceFactory f = new AuthorizationInfoServiceFactory(Constants.AUTH_SERVICE_USER_ID,
        Constants.AUTH_SERVICE_PASSWORD, Constants.AUTH_ENDPOINT);
    AuthorizationInfoService s = f.getAuthorizationInfoService();
    return s;
  }

  private FamilyService getFamilyService() {
    FamilyService service = new FamilyService(Constants.CUSTOMER_SERVICE_USER_ID, Constants.CUSTOMER_SERVICE_PASSWORD,
        Constants.COMMUNITY_SERVICE_USER_ID, Constants.COMMUNITY_SERVICE_PASSWORD);
    return service;
  }
}
