package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.koku.kks.ui.common.KksConverter;
import fi.koku.kks.ui.common.utils.CollectionComparator;
import fi.koku.kks.ui.common.utils.Constants;
import fi.koku.kks.ui.common.utils.SearchResult;
import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.portlet.filter.userinfo.service.impl.UserInfoServiceLocalDummyImpl;
import fi.koku.services.entity.community.v1.CommunitiesType;
import fi.koku.services.entity.community.v1.CommunityQueryCriteriaType;
import fi.koku.services.entity.community.v1.CommunityServiceFactory;
import fi.koku.services.entity.community.v1.CommunityServicePortType;
import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.community.v1.MemberType;
import fi.koku.services.entity.community.v1.MembersType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;
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
import fi.koku.services.entity.userinfo.v1.UserInfoService;
import fi.koku.services.entity.userinfo.v1.impl.UserInfoServiceDummyImpl;
import fi.koku.services.entity.userinfo.v1.model.Registry;

/**
 * Service demoa varten
 * 
 * @author tuomape
 * 
 */
@Service(value = "kksService")
public class KksService {

  private static final Logger LOG = LoggerFactory.getLogger(KksService.class);

  private KKSDemoModel malli;
  private Map<String, KksEntryClassType> entryClasses;
  private Map<String, KksCollectionClassType> collectionClasses;
  private KksConverter converter;
  private Map<String, List<ConsentRequest>> consents;
  UserInfoServiceLocalDummyImpl userInfo;

  public KksService() {
    entryClasses = new HashMap<String, KksEntryClassType>();
    collectionClasses = new HashMap<String, KksCollectionClassType>();
    converter = new KksConverter(this);
    userInfo = new UserInfoServiceLocalDummyImpl();
    initConsentReq();
  }

  private void initConsentReq() {

    if (consents == null) {
      consents = new HashMap<String, List<ConsentRequest>>();
    }
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
    UserInfoService uis = new UserInfoServiceDummyImpl();
    Map<String, Registry> tmp = new HashMap<String, Registry>();
    List<Registry> register = uis.getUsersAuthorizedRegistries(user);
    for (Registry r : register) {
      tmp.put(r.getId(), r);
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
        Constants.COMMUNITY_SERVICE_PASSWORD, Constants.ENDPOINT);
    return csf.getCommunityService();
  }

  private CustomerServicePortType getCustomerService() {
    CustomerServiceFactory customerServiceFactory = new CustomerServiceFactory(Constants.CUSTOMER_SERVICE_USER_ID,
        Constants.CUSTOMER_SERVICE_PASSWORD, Constants.ENDPOINT);
    return customerServiceFactory.getCustomerService();
  }

  public boolean hasAuthorizedData(KksCollectionClassType c, String user) {
    Map<String, Registry> tmp = getAuthorizedRegistries(user);

    for (KksGroupType g : c.getKksGroups().getKksGroup()) {
      if (tmp.containsKey(g.getRegister())) {
        return true;
      }

      for (KksGroupType sg : g.getSubGroups().getKksGroup()) {
        if (tmp.containsKey(sg.getRegister())) {
          return true;
        }
      }
    }

    return false;
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
    List<KKSCollection> authorized = new ArrayList<KKSCollection>();
    try {

      KksCollectionsCriteriaType criteria = new KksCollectionsCriteriaType();
      criteria.setPic(pic);
      criteria.setKksScope("minimum");
      KksServicePortType kksService = getKksService();
      KksType kks = kksService.opGetKks(criteria, getKksAuditInfo(user));

      List<KksCollectionType> collections = kks.getKksCollections().getKksCollection();

      if (collections != null) {

        for (KksCollectionType kct : collections) {
          tmp.add(converter.fromWsType(kct, false, user));
        }
      }

      for (KKSCollection c : tmp) {
        if (isMaster(pic, user, c) || hasAuthorizedData(c.getCollectionClass(), user)) {
          authorized.add(c);

          UserInfo u = userInfo.getUserInfoByPic(c.getCreator());

          if (u != null) {
            c.setModifierFullName(u.getFname() + " " + u.getSname());
          }
        }
      }
      Collections.sort(authorized, new CollectionComparator());

    } catch (ServiceFault e) {
      LOG.error("Failed to get KKS collections", e);
    }

    return authorized;
  }

  public KKSCollection getKksCollection(String collectionId, UserInfo info) {
    try {
      KksServicePortType kksService = getKksService();
      KksCollectionType kks = kksService.opGetKksCollection(collectionId, getKksAuditInfo(info.getPic()));

      KKSCollection col = converter.fromWsType(kks, true, info.getPic());
      for (List<Entry> lst : col.getMultiValueEntries().values()) {

        for (Entry e : lst) {
          UserInfo u = userInfo.getUserInfoByPic(e.getRecorder());
          if (u != null) {
            e.setModifierFullName(u.getFname() + " " + u.getSname());
          }
        }
      }
      return col;
    } catch (ServiceFault e) {
      LOG.error("Failed to get KKS collection " + collectionId, e);
    }
    return null;
  }

  public boolean updateKksCollection(KKSCollection collection, String customer, String user) {
    try {
      KksServicePortType kksService = getKksService();
      collection.setCreator(user);
      kksService.opUpdateKksCollection(converter.toWsType(collection, customer), getKksAuditInfo(user));
    } catch (ServiceFault e) {
      LOG.error("Failed to update KKS collection " + collection.getId(), e);
      return false;
    }
    return true;
  }

  public boolean updateKksCollectionStatus(String customer, String collectionId, String status, String user) {
    try {
      KksServicePortType kksService = getKksService();
      KksCollectionStateCriteriaType state = new KksCollectionStateCriteriaType();
      state.setCollectionId(collectionId);
      state.setState(status);
      state.setCustomer(customer);
      kksService.opUpdateKksCollectionStatus(state, getKksAuditInfo(user));
    } catch (ServiceFault e) {
      LOG.error("Failed to update KKS collection status " + collectionId + " : " + status, e);
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
      KksServicePortType kksService = getKksService();
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
      KksServicePortType kksService = getKksService();
      return kksService.opAddKksCollection(kksCollectionCreationCriteria, getKksAuditInfo(user)).getId();
    } catch (ServiceFault e) {
      LOG.error("Failed to create KKS collection version " + name + " type: " + type, e);
    }
    return null;
  }

  public List<KKSCollection> searchKksCollections(List<String> tagNames, String customer, String user) {
    List<KKSCollection> tmp = new ArrayList<KKSCollection>();
    List<KKSCollection> authorized = new ArrayList<KKSCollection>();
    try {
      KksQueryCriteriaType kksQueryCriteria = new KksQueryCriteriaType();
      kksQueryCriteria.setPic(customer);
      KksTagNamesType names = new KksTagNamesType();
      names.getKksTagName().addAll(tagNames);
      kksQueryCriteria.setKksTagNames(names);
      KksServicePortType kksService = getKksService();
      KksCollectionsType collections = kksService.opQueryKks(kksQueryCriteria, getKksAuditInfo(user));

      for (KksCollectionType type : collections.getKksCollection()) {
        tmp.add(converter.fromWsType(type, false, user));
      }

      for (KKSCollection collection : tmp) {
        List<Entry> entries = new ArrayList<Entry>(collection.getEntryValues());

        if (isMaster(customer, user, collection) || hasAuthorizedData(collection.getCollectionClass(), user)) {
          authorized.add(collection);

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
      }

    } catch (ServiceFault e) {
      LOG.error("Failed to search KKS collections", e);
    }
    return authorized;
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
      Calendar c = new GregorianCalendar();
      c.setTime(new Date());
      criteria.setModified(c);
      KksServicePortType kksService = getKksService();
      return kksService.opAddEntry(criteria, getKksAuditInfo(user)).getId();
    } catch (ServiceFault e) {
      LOG.error("Failed to add KKS entry " + entryId, e);
    }
    return null;
  }

  public boolean removeKksEntry(String customer, String entryId, String valueId, String value, String user) {
    try {
      KksEntryCriteriaType criteria = new KksEntryCriteriaType();
      criteria.setEntryId(entryId);
      criteria.setPic(customer);
      KksEntryValueType evt = new KksEntryValueType();
      evt.setId(valueId);
      evt.setValue(value);
      criteria.setValue(evt);
      KksServicePortType kksService = getKksService();
      kksService.opDeleteEntry(criteria, getKksAuditInfo(user));
    } catch (ServiceFault e) {
      LOG.error("Failed to remove KKS entry " + entryId, e);
      return false;
    }
    return true;
  }

  private void collectMetadata(String user) {
    try {
      KksServicePortType kksService = getKksService();
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

  public boolean onkoLuotu() {
    if (malli != null) {
      return true;
    }
    return false;
  }

  public void luo(String kayttaja) {
    malli = DemoFactory.luo();
  }

  public List<Person> searchChilds(String user) {
    return getChilds(user);
  }

  public List<Person> getChilds(String pic) {
    List<Person> childs = new ArrayList<Person>();
    CommunityQueryCriteriaType communityQueryCriteria = new CommunityQueryCriteriaType();
    communityQueryCriteria.setCommunityType(Constants.COMMUNITY_TYPE_GUARDIAN_COMMUNITY);
    communityQueryCriteria.setMemberPic(pic);
    CommunitiesType communitiesType = null;

    try {
      communitiesType = getCommunityService().opQueryCommunities(communityQueryCriteria, getCommynityAuditInfo(pic));
    } catch (fi.koku.services.entity.community.v1.ServiceFault fault) {
      LOG.error("Failed to get communities", fault);
    }

    if (communitiesType != null) {
      List<CommunityType> communities = communitiesType.getCommunity();
      for (CommunityType community : communities) {
        MembersType membersType = community.getMembers();
        List<MemberType> members = membersType.getMember();
        Iterator<MemberType> mi = members.iterator();
        while (mi.hasNext()) {
          MemberType member = mi.next();
          if (member.getRole().equals(Constants.ROLE_DEPENDANT)) {
            try {
              CustomerType customer = getCustomerService().opGetCustomer(member.getPic(), getCustomerAuditInfo(pic));
              childs.add(Person.fromCustomerType(customer));
            } catch (fi.koku.services.entity.customer.v1.ServiceFault fault) {
              LOG.error("Failed to get community person details", fault);
            }
          }
        }
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

  public List<Person> searchPerson(Person target, String user) {
    List<Person> list = new ArrayList<Person>();
    try {
      CustomerType t = getCustomerService().opGetCustomer(target.getPic().trim(), getCustomerAuditInfo(user));
      if (t != null) {
        Person p = Person.fromCustomerType(t);
        list.add(p);
      }
    } catch (fi.koku.services.entity.customer.v1.ServiceFault e) {
      LOG.error("Failed to fetch customer details", e);
    }
    return list;
  }

  public Person searchCustomer(String socialSecurityNumber, String user) {
    try {
      CustomerType t = getCustomerService().opGetCustomer(socialSecurityNumber.trim(), getCustomerAuditInfo(user));
      if (t != null) {
        return Person.fromCustomerType(t);
      }
    } catch (fi.koku.services.entity.customer.v1.ServiceFault e) {
      LOG.error("Failed to fetch customer details", e);
    }
    return null;
  }

  public List<String> haecreatablescollections(Person h) {
    List<String> nimet = haecollectionNimet();

    List<String> tmp = new ArrayList<String>(nimet);
    for (String nimi : tmp) {
      if (!h.getKks().hasCollection(nimi)) {
        nimet.remove(nimi);
      }
    }

    return nimet;
  }

  /**
   * This method lists all the possible collections for this person
   * 
   * @param h
   * @return
   */
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

  public Set<CollectionType> searchCollectionTypes(Person h) {
    Set<CollectionType> tmp = new LinkedHashSet<CollectionType>();
    // tmp.add(DemoFactory.luoNelivuotisTarkastustype());
    // tmp.add(DemoFactory.luoVarhaiskasvatusSuunnitelmantype());
    return tmp;
  }

  public List<String> haecollectionNimet() {
    List<String> tmp = new ArrayList<String>();
    tmp.add(DemoFactory.NELI_VUOTIS_TARKASTUS);
    tmp.add(DemoFactory.VASU);
    return tmp;
  }

  public SearchResult searchEntries(Person h, String... classification) {
    SearchResult result = new SearchResult();
    List<KKSCollection> collections = h.getKks().getCollections();

    for (KKSCollection k : collections) {
      searchEntries(result, k, classification);

      searchMultivalueEntries(result, k, classification);
    }

    return result;
  }

  private void searchMultivalueEntries(SearchResult result, KKSCollection k, String... classification) {
    for (List<Entry> tmp : k.getMultiValueEntries().values()) {
      for (Entry ki : tmp) {
        boolean lisatty = false;
        for (int i = 0; i < classification.length && !lisatty; i++) {
          String str = classification[i];
          if (ki.hasClassification(str) && !ki.getValue().equals("")) {
            result.lisaaresult(k, ki);
            lisatty = true;
          }
        }
      }
    }
  }

  public void sendConsentRequest(String collectionId, String consentType, String customerId) {

    initConsentReq();
    ConsentRequest req = new ConsentRequest();
    req.setConsentType(consentType);
    req.setCustomerId(customerId);

    if (consents.containsKey(customerId)) {
      List<ConsentRequest> tmp = consents.get(customerId);
      tmp.add(req);

    } else {
      List<ConsentRequest> tmp = new ArrayList<ConsentRequest>();
      tmp.add(req);
      consents.put(customerId, tmp);
    }
  }

  public Map<String, ConsentRequest> getConsentRequests(String customerId) {

    initConsentReq();

    if (!consents.containsKey(customerId)) {
      return new HashMap<String, ConsentRequest>();
    }

    List<ConsentRequest> tmp = consents.get(customerId);

    Map<String, ConsentRequest> res = new HashMap<String, ConsentRequest>();

    for (ConsentRequest r : tmp) {
      res.put(r.getConsentType(), r);
    }
    return res;
  }

  private void searchEntries(SearchResult result, KKSCollection k, String... classification) {
    for (Entry ki : k.getEntries().values()) {
      boolean lisatty = false;
      for (int i = 0; i < classification.length && !lisatty; i++) {
        String tmp = classification[i];
        if (ki.hasClassification(tmp) && !ki.getValue().equals("")) {
          result.lisaaresult(k, ki);
          lisatty = true;
        }
      }
    }
  }

  public List<CollectionType> haecollectionTypes() {
    List<CollectionType> tmp = new ArrayList<CollectionType>();

    List<KKSCollection> tmp2 = new ArrayList<KKSCollection>();

    // tmp2.add(DemoFactory.luo4VuotisTarkastus(""));
    // tmp2.add(DemoFactory.luoVarhaiskasvatusSuunnitelma(""));

    for (KKSCollection k : tmp2) {
      // tmp.add(k.getType());
    }
    return tmp;
  }
}
