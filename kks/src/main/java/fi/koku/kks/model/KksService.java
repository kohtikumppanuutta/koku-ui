package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.koku.kks.ui.common.KksConverter;
import fi.koku.kks.ui.common.KokuWSFactory;
import fi.koku.kks.ui.common.User;
import fi.koku.kks.ui.common.utils.SearchResult;
import fi.koku.services.entity.kks.v1.AuditInfoType;
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
import fi.koku.services.entity.kks.v1.KksServicePortType;
import fi.koku.services.entity.kks.v1.KksTagIdsType;
import fi.koku.services.entity.kks.v1.KksTagNamesType;
import fi.koku.services.entity.kks.v1.KksTagType;
import fi.koku.services.entity.kks.v1.KksTagsType;
import fi.koku.services.entity.kks.v1.KksType;
import fi.koku.services.entity.kks.v1.ServiceFault;

/**
 * Service demoa varten
 * 
 * @author tuomape
 * 
 */
@Service(value = "kksService")
public class KksService {

  private static final Logger LOG = LoggerFactory.getLogger(KksService.class);

  String endPoint = "http://localhost:8180/";
  private KKSDemoModel malli;
  private KokuWSFactory kf;
  private AuditInfoType audit;
  private Map<String, KksEntryClassType> entryClasses;
  private Map<String, KksCollectionClassType> collectionClasses;
  private KksConverter converter;

  public KksService() {
    kf = new KokuWSFactory("marko", "marko", endPoint);
    audit = new AuditInfoType();
    audit.setComponent("kks");
    audit.setUserId("tuomape");
    converter = new KksConverter(this);

    entryClasses = new HashMap<String, KksEntryClassType>();
    collectionClasses = new HashMap<String, KksCollectionClassType>();
  }

  public KksEntryClassType getEntryClassType(String id) {
    if (entryClasses.isEmpty()) {
      collectMetadata();
    }
    return entryClasses.get(id);
  }

  public KksCollectionClassType getCollectionClassType(String id) {
    if (collectionClasses.isEmpty()) {
      collectMetadata();
    }
    return collectionClasses.get(id);
  }

  public void clearMetadata() {
    entryClasses.clear();
    collectionClasses.clear();
  }

  public List<KKSCollection> getKksCollections(String pic) {
    List<KKSCollection> tmp = new ArrayList<KKSCollection>();
    try {
      KksServicePortType kksService = kf.getKksService();
      KksCollectionsCriteriaType criteria = new KksCollectionsCriteriaType();
      criteria.setPic(pic);
      criteria.setKksScope("minimum");
      KksType kks = kksService.opGetKks(criteria, audit);

      List<KksCollectionType> collections = kks.getKksCollections().getKksCollection();

      if (collections != null) {

        for (KksCollectionType kct : collections) {
          tmp.add(converter.fromWsType(kct, false));
        }
      }

    } catch (ServiceFault e) {
      LOG.error("Failed to get KKS collections", e);
    }
    return tmp;
  }

  public KKSCollection getKksCollection(String collectionId) {
    try {
      KksServicePortType kksService = kf.getKksService();
      KksCollectionType kks = kksService.opGetKksCollection(collectionId, audit);
      return converter.fromWsType(kks, true);
    } catch (ServiceFault e) {
      LOG.error("Failed to get KKS collection " + collectionId, e);
    }
    return null;
  }

  public boolean updateKksCollection(KKSCollection collection, String customer) {
    try {
      KksServicePortType kksService = kf.getKksService();
      kksService.opUpdateKksCollection(converter.toWsType(collection, customer), audit);
    } catch (ServiceFault e) {
      e.printStackTrace();
      LOG.error("Failed to update KKS collection " + collection.getId(), e);
      return false;
    }
    return true;
  }

  public boolean updateKksCollectionStatus(String collectionId, String status) {
    try {
      KksServicePortType kksService = kf.getKksService();
      KksCollectionStateCriteriaType state = new KksCollectionStateCriteriaType();
      state.setCollectionId(collectionId);
      state.setState(status);
      kksService.opUpdateKksCollectionStatus(state, audit);
    } catch (ServiceFault e) {
      LOG.error("Failed to update KKS collection status " + collectionId + " : " + status, e);
    }
    return true;
  }

  public String createKksCollection(String name, String type, String customer) {
    try {
      KksServicePortType kksService = kf.getKksService();
      KksCollectionCreationCriteriaType kksCollectionCreationCriteria = new KksCollectionCreationCriteriaType();
      kksCollectionCreationCriteria.setCollectionName(name);
      kksCollectionCreationCriteria.setPic(customer);
      kksCollectionCreationCriteria.setCollectionTypeId(type);
      kksCollectionCreationCriteria.setKksScope("new");
      return kksService.opAddKksCollection(kksCollectionCreationCriteria, audit).getId();
    } catch (ServiceFault e) {
      LOG.error("Failed to create KKS collection " + name, e);
    }
    return null;
  }

  public String createKksCollectionVersion(String name, String type, String customer, boolean empty) {
    try {
      KksServicePortType kksService = kf.getKksService();
      KksCollectionCreationCriteriaType kksCollectionCreationCriteria = new KksCollectionCreationCriteriaType();
      kksCollectionCreationCriteria.setCollectionName(name);
      kksCollectionCreationCriteria.setPic(customer);
      kksCollectionCreationCriteria.setCollectionTypeId(type);

      if (empty) {
        kksCollectionCreationCriteria.setKksScope("new_version");
      } else {
        kksCollectionCreationCriteria.setKksScope("version");
      }

      return kksService.opAddKksCollection(kksCollectionCreationCriteria, audit).getId();
    } catch (ServiceFault e) {
      LOG.error("Failed to create KKS collection version " + name + " type: " + type, e);
    }
    return null;
  }

  public List<KKSCollection> searchKksCollections(List<String> tagNames, String customer) {
    List<KKSCollection> tmp = new ArrayList<KKSCollection>();

    try {
      KksServicePortType kksService = kf.getKksService();
      KksQueryCriteriaType kksQueryCriteria = new KksQueryCriteriaType();
      KksTagNamesType names = new KksTagNamesType();
      names.getKksTagName().addAll(tagNames);
      kksQueryCriteria.setKksTagNames(names);

      KksCollectionsType collections = kksService.opQueryKks(kksQueryCriteria, audit);

      for (KksCollectionType type : collections.getKksCollection()) {
        tmp.add(converter.fromWsType(type, false));
      }

    } catch (ServiceFault e) {
      LOG.error("Failed to search KKS collections", e);
    }
    return tmp;
  }

  public String addKksEntry(String customer, String entryId, String valueId, String value) {
    try {
      KksServicePortType kksService = kf.getKksService();

      KksEntryCriteriaType criteria = new KksEntryCriteriaType();
      criteria.setEntryId(entryId);
      criteria.setPic(customer);
      KksEntryValueType evt = new KksEntryValueType();
      evt.setId(valueId);
      evt.setValue(value);
      criteria.setValue(evt);

      return kksService.opAddEntry(criteria, audit).getId();
    } catch (ServiceFault e) {
      LOG.error("Failed to add KKS entry " + entryId, e);
    }
    return null;
  }

  public boolean removeKksEntry(String customer, String entryId, String valueId, String value) {
    try {
      KksServicePortType kksService = kf.getKksService();

      KksEntryCriteriaType criteria = new KksEntryCriteriaType();
      criteria.setEntryId(entryId);
      criteria.setPic(customer);
      KksEntryValueType evt = new KksEntryValueType();
      evt.setId(valueId);
      evt.setValue(value);
      criteria.setValue(evt);

      kksService.opDeleteEntry(criteria, audit);
    } catch (ServiceFault e) {
      LOG.error("Failed to remove KKS entry " + entryId, e);
      return false;
    }
    return true;
  }

  private void collectMetadata() {
    try {
      KksServicePortType kksService = kf.getKksService();
      List<KksCollectionClassType> classes = kksService.opGetKksCollectionClasses("all", audit).getKksCollectionClass();

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

  public void test() {

    KksServicePortType kksService = kf.getKksService();

    KksTagIdsType tmp = new KksTagIdsType();
    tmp.getKksTagId().add("1");
    KksTagsType tags;
    try {
      tags = kksService.opGetKksTags(tmp, audit);

      for (KksTagType kt : tags.getKksTag()) {
        System.out.println("tags" + kt.getName());
      }
    } catch (ServiceFault e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
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

  public List<Person> searchChilds(User user) {
    return malli.getPersons();
  }

  public List<Person> haeHenkilo(Person target) {
    List<Person> list = new ArrayList<Person>();
    Person tmp = searchChild(target.getPic().trim());

    if (tmp != null) {
      list.add(tmp);
    }
    return list;
  }

  public Person searchChild(String socialSecurityNumber) {
    for (Person tmp : malli.getPersons()) {
      if (tmp.getPic().equals(socialSecurityNumber.trim())) {
        return tmp;
      }
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
  public List<Creatable> searchPersonCreatableCollections(Person h) {
    if (collectionClasses.isEmpty()) {
      collectMetadata();
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
