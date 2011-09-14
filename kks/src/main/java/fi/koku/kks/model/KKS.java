package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Persons agreements and plans
 * 
 * @author tuomape
 */
public class KKS {

  private Map<String, KKSCollection> collections;

  public KKS() {
    collections = new LinkedHashMap<String, KKSCollection>();
  }

  public void addCollection(KKSCollection k) {
    collections.put(k.getId(), k);
  }

  public void removeCollection(KKSCollection k) {
    collections.remove(k.getId());
  }

  public KKSCollection getCollection(String id) {
    return collections.get(id);
  }

  public List<KKSCollection> getCollections() {
    return new ArrayList<KKSCollection>(collections.values());
  }

  public boolean hasCollection(String id) {
    return collections.containsKey(id);
  }
}
