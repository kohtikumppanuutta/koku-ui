package fi.koku.kks.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Form class for editing collection entries
 * 
 * @author Ixonos / tuomape
 *
 */
public class CollectionForm {
 
  private Map<String, Entry> entries;
  
  public CollectionForm() {
    entries = new HashMap<String, Entry>();
  }

  public Map<String, Entry> getEntries() {
    return entries;
  }

  public void setEntries(Map<String, Entry> entries) {
    this.entries = entries;
  }
  
  
}
