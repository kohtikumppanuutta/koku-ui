package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.koku.kks.ui.common.User;
import fi.koku.kks.ui.common.utils.SearchResult;

/**
 * Service demoa varten
 * 
 * @author tuomape
 * 
 */
@Service(value = "demoKksService")
public class DemoService {

  private User kayttaja;

  private KKSDemoModel malli;

  public DemoService() {

  }

  private static Logger log = LoggerFactory.getLogger(DemoService.class);

  public boolean onkoLuotu() {
    if (malli != null)
      return true;
    return false;
  }

  public void luo(String kayttaja) {
    this.kayttaja = new User();
    this.kayttaja.setRole(kayttaja);
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

  /**
   * Creates a collection for person
   * 
   * @param h
   *          Person
   * @param nimi
   *          Name of the collection
   */
  public KKSCollection luocollection(Person h, String nimi, Creatable creation) {

    KKSCollection k = null;

    if (creation.isNeedsVersioning()) {
      KKSCollection collection = h.getKks().getCollection(creation.getId());
      k = DemoFactory.createNewVersion(collection, nimi, !creation.isCopyContent());
    } else {
      k = DemoFactory.createCollection(nimi, creation.getName());
    }

    h.getKks().addCollection(k);
    return k;
  }

  public List<String> haecreatablescollections(Person h) {
    List<String> nimet = haecollectionNimet();

    List<String> tmp = new ArrayList<String>(nimet);
    for (String nimi : tmp) {
      if (!h.getKks().hasCollection(nimi))
        nimet.remove(nimi);
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
    Set<CollectionType> tyypit = searchCollectionTypes(h);

    List<Creatable> creatables = new ArrayList<Creatable>();

    for (CollectionType kt : tyypit) {
      creatables.add(new Creatable("" + kt.getId(), false, kt.getName()));
    }

    return creatables;
  }

  public Set<CollectionType> searchCollectionTypes(Person h) {
    Set<CollectionType> tmp = new LinkedHashSet<CollectionType>();
    tmp.add(DemoFactory.luoNelivuotisTarkastustype());
    tmp.add(DemoFactory.luoVarhaiskasvatusSuunnitelmantype());
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

    return result;
  }

  public List<CollectionType> haecollectionTypes() {
    List<CollectionType> tmp = new ArrayList<CollectionType>();

    List<KKSCollection> tmp2 = new ArrayList<KKSCollection>();

    tmp2.add(DemoFactory.luo4VuotisTarkastus(""));
    tmp2.add(DemoFactory.luoVarhaiskasvatusSuunnitelma(""));

    for (KKSCollection k : tmp2) {
      tmp.add(k.getType());
    }
    return tmp;
  }
}
