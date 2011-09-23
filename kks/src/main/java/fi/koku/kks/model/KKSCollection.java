package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fi.koku.services.entity.kks.v1.KksCollectionClassType;
import fi.koku.services.entity.kks.v1.KksEntryClassType;
import fi.koku.services.entity.kks.v1.KksGroupType;
import fi.koku.services.entity.kks.v1.KksTagType;

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
  private List<Classification> classifications;
  private Map<String, Entry> entries;
  private Map<String, List<Entry>> multiValueEntries;
  private String nextVersion;
  private String prevVersion;
  private boolean versioned;
  private boolean buildFromExisting;
  private KksCollectionClassType collectionClass;
  private String creator;

  public KKSCollection(String id, KKSCollection previous, boolean clear, Date creationTime, CollectionState state,
      int version) {
    this(id, previous.getName(), previous.getDescription(), state, creationTime, version, previous.getCollectionClass());
    prevVersion = previous.getId();
    buildFromExisting = true;

    for (Entry k : previous.getEntries().values()) {
      // addEntry(new Entry(clear ? "" : k.getValue(), new Date(),
      // k.getVersion(), k.getRecorder(), k.getType()));
    }

    for (List<Entry> tmp : previous.getMultiValueEntries().values()) {
      for (Entry k : tmp) {

        /*
         * if (!containsClassification(k.getClassifications(),
         * Constants.LUOKITUS_KOMMENTTI)) { addMultivalue(new Entry(clear ? "" :
         * k.getValue(), new Date(), k.getVersion(), k.getRegister(),
         * k.getRecorder(), k.getType())); }
         */
      }
    }

  }

  public KKSCollection(String id, String name, String description, CollectionState state, Date creationTime,
      int version, KksCollectionClassType type) {
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
    this.collectionClass = type;
    entries = new LinkedHashMap<String, Entry>();
    multiValueEntries = new LinkedHashMap<String, List<Entry>>();
  }

  private boolean containsClassification(List<KksTagType> classifications, String... checkedStrings) {
    List<String> tmp = new ArrayList<String>(Arrays.asList(checkedStrings));
    for (KksTagType l : classifications) {
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

  public KksCollectionClassType getCollectionClass() {
    return collectionClass;
  }

  public void setCollectionClass(KksCollectionClassType collectionClass) {
    this.collectionClass = collectionClass;
  }

  public String getModifier() {
    return modifier;
  }

  public void setModifier(String modifier) {
    this.modifier = modifier;
  }

  public final void addEntry(Entry entry) {
    if (entry == null) {
      return;
    }
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

  public final void addMultivalue(Entry entry) {

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

  public Entry getEntryWithValue(String id) {

    for (List<Entry> tmp : multiValueEntries.values()) {
      for (Entry k : tmp) {

        if (k.getValueId().equals(id)) {
          return k;
        }
      }
    }

    for (Entry k : entries.values()) {
      if (k.getValueId().equals(id)) {
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

  public List<Entry> getEntryValues() {
    List<Entry> tmp = new ArrayList<Entry>();
    tmp.addAll(entries.values());
    for (List<Entry> l : multiValueEntries.values()) {
      tmp.addAll(l);
    }
    return tmp;
  }

  public void generateEmptyEntries() {
    for (KksGroupType group : collectionClass.getKksGroups().getKksGroup()) {

      checkAndInsertEntry(group);

      for (KksGroupType subGroup : group.getSubGroups().getKksGroup()) {
        checkAndInsertEntry(subGroup);
      }

    }
  }

  private void checkAndInsertEntry(KksGroupType group) {
    for (KksEntryClassType ect : group.getKksEntryClasses().getKksEntryClass()) {

      if (!ect.isMultiValue() && !entries.containsKey("" + ect.getId())) {
        addEntry(new Entry("", "", new Date(), "1", "", ect));
      }
    }
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

}
