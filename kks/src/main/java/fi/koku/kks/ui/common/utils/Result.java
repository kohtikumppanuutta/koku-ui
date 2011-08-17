package fi.koku.kks.ui.common.utils;

import java.util.ArrayList;
import java.util.List;

import fi.koku.kks.model.Entry;

/**
 * Single search result
 * 
 * @author tuomape
 * 
 */
public class Result {

  private String collectionId;
  private boolean collectionActive;

  private String name;
  private List<Entry> entries;

  public Result(String id) {
    this.collectionId = id;
    entries = new ArrayList<Entry>();
  }

  public String getName() {
    return name;
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public void add(Entry k) {
    entries.add(k);
  }

  public String getCollectionId() {
    return collectionId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isCollectionActive() {
    return collectionActive;
  }

  public void setCollectionActive(boolean collectionActive) {
    this.collectionActive = collectionActive;
  }

}
