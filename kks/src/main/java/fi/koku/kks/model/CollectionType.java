package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.koku.kks.ui.common.utils.GroupComparator;
import fi.koku.kks.ui.common.utils.LinkedMapWrapper;

/**
 * Describes collection type and its information
 * 
 * @author tuomape
 */
public class CollectionType {

  private int id;
  private String name;
  private String description;
  private String concentType;
  private List<EntryType> entryTypes;
  private LinkedMapWrapper<String, Group> entryGroups;
  private GroupComparator groupComparator;

  private static Logger log = LoggerFactory.getLogger(CollectionType.class);

  public CollectionType(int id, String name, String description) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.entryTypes = new ArrayList<EntryType>();
    this.entryGroups = new LinkedMapWrapper<String, Group>();
    this.groupComparator = new GroupComparator();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public List<EntryType> getEntryTypes() {
    return entryTypes;
  }

  public void setEntryTypes(List<EntryType> entryTypes) {
    this.entryTypes = entryTypes;

    for (EntryType tmp : entryTypes) {
      addIntoEntryGroup(tmp);
    }
  }

  public void addEntryType(EntryType type) {
    this.entryTypes.add(type);
    addIntoEntryGroup(type);
  }

  private void addIntoEntryGroup(EntryType type) {
    if (entryGroups.get().containsKey(type.getGroup())) {
      Group tmp = entryGroups.get().get(type.getGroup());
      tmp.add(type);
    } else {
      log.error("Wrong group for entry: " + type.getName() + " " + type.getGroup());
    }
  }

  public void addEntryGroup(Group group) {
    entryGroups.get().put(group.getName(), group);
  }

  public LinkedMapWrapper<String, Group> getEntryGroups() {
    return entryGroups;
  }

  public List<Group> getSortedGroups() {
    List<Group> tmp = new ArrayList<Group>(entryGroups.getValues());
    Collections.sort(tmp, groupComparator);
    return tmp;
  }

  public EntryType getEntryType(String id) {
    for (EntryType k : entryTypes) {
      if (k.getId().equals(id)) {
        return k;
      }
    }
    return null;
  }

  public String getConcentType() {
    return concentType;
  }

  public void setConcentType(String concentType) {
    this.concentType = concentType;
  }

}
