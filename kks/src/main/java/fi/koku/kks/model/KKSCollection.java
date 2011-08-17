package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.koku.kks.ui.common.utils.Constants;

/**
 * Single collection
 * 
 * @author tuomape
 */
public class KKSCollection {

  private String id;

  private String name;
  private String description;
  private String modifier;
  private CollectionState state;
  private Date creationTime;
  private int version;
  private CollectionType type;
  private List<Classification> classifications;
  private Map<String, Entry> entries;
  private Map<String, List<Entry>> multiValueEntries;
  private String nextVersion;
  private String prevVersion;
  private boolean versioned;
  private boolean buildFromExisting;

  public KKSCollection(String id, KKSCollection previous, boolean clear, Date creationTime, CollectionState state,
      int version) {
    this(id, previous.getName(), previous.getDescription(), state, creationTime, version, previous.getType());
    prevVersion = previous.getId();
    buildFromExisting = true;

    for (Entry k : previous.getEntries().values()) {
      addEntry(new Entry(clear ? "" : k.getValue(), new Date(), k.getVersion(), k.getRegister(), k.getRecorder(),
          k.getType()));
    }

    for (List<Entry> tmp : previous.getMultiValueEntries().values()) {
      for (Entry k : tmp) {

        if (!containsClassification(k.getClassifications(), Constants.classification_KOMMENTTI)) {
          addMultivalue(new Entry(clear ? "" : k.getValue(), new Date(), k.getVersion(), k.getRegister(),
              k.getRecorder(), k.getType()));
        }
      }
    }

  }

  public KKSCollection(String id, String name, String description, CollectionState state, Date creationTime,
      int version, CollectionType type) {
    super();
    this.prevVersion = null;
    this.buildFromExisting = false;
    this.nextVersion = null;
    this.versioned = false;
    this.id = id;
    this.name = name;
    this.description = description;
    this.state = state;
    this.creationTime = creationTime;
    this.version = version;
    this.type = type;
    entries = new HashMap<String, Entry>();
    multiValueEntries = new HashMap<String, List<Entry>>();
  }

  private boolean containsClassification(List<Classification> classifications, String... checkedStrings) {
    List<String> tmp = new ArrayList<String>(Arrays.asList(checkedStrings));
    for (Classification l : classifications) {
      if (tmp.contains(l.getName())) {
        return true;
      }
    }
    return false;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CollectionState getState() {
    return state;
  }

  public void setState(CollectionState state) {
    this.state = state;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public List<Classification> getClassifications() {
    return classifications;
  }

  public void setClassifications(List<Classification> classifications) {
    this.classifications = classifications;
  }

  public CollectionType getType() {
    return type;
  }

  public void setType(CollectionType type) {
    this.type = type;
  }

  public String getModifier() {
    return modifier;
  }

  public void setModifier(String modifier) {
    this.modifier = modifier;
  }

  public void addEntry(Entry entry) {
    entries.put("" + entry.getType().getId(), entry);
  }

  public Map<String, Entry> getEntries() {
    return entries;
  }

  public void setEntries(Map<String, Entry> entries) {
    this.entries = entries;
  }

  public String getPrevVersion() {
    return prevVersion;
  }

  public void setPrevVersion(String prevVersion) {
    this.prevVersion = prevVersion;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Map<String, List<Entry>> getMultiValueEntries() {
    return multiValueEntries;
  }

  public void setMultiValueEntries(Map<String, List<Entry>> multiValueEntries) {
    this.multiValueEntries = multiValueEntries;
  }

  public void addMultivalue(Entry entry) {

    String key = "" + entry.getType().getId();
    if (multiValueEntries.containsKey(key)) {
      multiValueEntries.get(key).add(entry);
    } else {
      List<Entry> tmp = new ArrayList<Entry>();
      tmp.add(entry);
      multiValueEntries.put(key, tmp);
    }
  }

  public void removeMultivalue(String id) {
    for (List<Entry> kirjaukset : multiValueEntries.values()) {

      List<Entry> tmp = new ArrayList<Entry>(kirjaukset);
      for (Entry k : tmp) {
        if (k.getId().equals(id)) {
          kirjaukset.remove(k);
        }
      }
    }
  }

  public Entry getEntry(String id) {

    for (List<Entry> tmp : multiValueEntries.values()) {
      for (Entry k : tmp) {
        if (k.getId().equals(id)) {
          return k;
        }
      }
    }

    for (Entry k : entries.values()) {
      if (k.getId().equals(id)) {
        return k;
      }
    }

    return null;
  }

  public boolean isVersioned() {
    return versioned;
  }

  public void setVersioned(boolean versioned) {
    this.versioned = versioned;
  }

  public String getNextVersion() {
    return nextVersion;
  }

  public void setNextVersion(String versioned) {
    this.nextVersion = versioned;
  }

  public boolean isBuildFromExisting() {
    return buildFromExisting;
  }

  public void setBuildFromExisting(boolean buildFromExisting) {
    this.buildFromExisting = buildFromExisting;
  }

}
