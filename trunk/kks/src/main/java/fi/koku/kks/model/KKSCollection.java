package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fi.koku.services.entity.kks.v1.KksCollectionClassType;
import fi.koku.services.utility.authorizationinfo.v1.model.Registry;

/**
 * Single collection
 * 
 * @author tuomape
 */
public class KKSCollection {

  private String id;

  private String name;
  private String customer;
  private String description;
  private String modifier;
  private String modifierFullName;
  private CollectionState state;
  private Date creationTime;
  private int version;
  private Map<String, Entry> entries;
  private String nextVersion;
  private String prevVersion;
  private boolean versioned;
  private boolean buildFromExisting;
  private KksCollectionClassType collectionClass;
  private String creator;
  private Map<String, Registry> authorizedRegistrys;
  private boolean master;
  private boolean consentRequested;
  private String userConsentStatus;

  public KKSCollection() {
    entries = new LinkedHashMap<String, Entry>();
    authorizedRegistrys = new HashMap<String, Registry>();
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
    authorizedRegistrys = new HashMap<String, Registry>();
    consentRequested = false;
    userConsentStatus = "";
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

  public Entry getEntry(String id) {

    for (Entry k : entries.values()) {
      if (k.getId().equals(id)) {
        return k;
      }
    }

    return null;
  }

  public Entry getEntryWithValue(String id) {

    for (Entry k : entries.values()) {
      for (EntryValue v : k.getEntryValues()) {

        if (v.getId().equals(id)) {
          return k;
        }
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

    return tmp;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getModifierFullName() {
    return modifierFullName;
  }

  public void setModifierFullName(String modifierFullName) {
    this.modifierFullName = modifierFullName;
  }

  public void clearEntries() {
    entries.clear();
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public Map<String, Registry> getAuthorizedRegistrys() {
    return authorizedRegistrys;
  }

  public void setAuthorizedRegistrys(Map<String, Registry> authorizedRegistrys) {
    this.authorizedRegistrys = authorizedRegistrys;
  }

  public boolean isMaster() {
    return master;
  }

  public void setMaster(boolean master) {
    this.master = master;
  }

  public boolean isConsentRequested() {
    return consentRequested;
  }

  public void setConsentRequested(boolean consentRequested) {
    this.consentRequested = consentRequested;
  }

  public String getUserConsentStatus() {
    return userConsentStatus;
  }

  public void setUserConsentStatus(String userConsentStatus) {
    this.userConsentStatus = userConsentStatus;
  }
  
  public String toString() {
    return getId() + " " + getName();
  }

}
