package fi.koku.kks.ui.common.utils;

import java.util.Collection;

import fi.koku.kks.model.Entry;
import fi.koku.kks.model.KKSCollection;

/**
 * Search results
 * 
 * @author tuomape
 * 
 */
public class SearchResult {

  private LinkedMapWrapper<String, Result> results;

  public SearchResult() {
    results = new LinkedMapWrapper<String, Result>();
  }

  public void lisaaresult(KKSCollection collection, Entry entry) {
    if (results.get().containsKey(collection.getId())) {
      Result tmp = results.get().get(collection.getId());
      tmp.add(entry);
    } else {
      Result tmp = new Result(collection.getId());
      tmp.setName(collection.getName());
      tmp.setCollectionActive(collection.getState().isActive());
      tmp.add(entry);
      results.get().put(collection.getId(), tmp);
    }
  }

  public Collection<Result> getResults() {
    return results.getValues();
  }

}
