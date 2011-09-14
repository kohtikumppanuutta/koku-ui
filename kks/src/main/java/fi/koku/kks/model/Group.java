package fi.koku.kks.model;

import fi.koku.kks.ui.common.utils.LinkedMapWrapper;

/**
 * Contains information that is mapped to certain group
 * 
 * @author tuomape
 * 
 */
public class Group implements Comparable<Group> {

  private GroupKey id;
  private String description;
  private String register;

  private LinkedMapWrapper<String, ChildGroup> childGroups;

  public Group(GroupKey id, String rekisteri) {
    this(id, rekisteri, null);
  }

  public Group(GroupKey id, String register, String description) {
    this.id = id;
    this.register = register;
    this.description = description;
    childGroups = new LinkedMapWrapper<String, ChildGroup>();
  }

  public void add(EntryType type) {

    if (childGroups.get().containsKey(type.getSubGroup())) {
      ChildGroup tmp = childGroups.get().get(type.getSubGroup());
      tmp.add(type);
    } else {
      ChildGroup tmp = new ChildGroup(type.getSubGroup());
      tmp.add(type);
      childGroups.get().put(type.getSubGroup(), tmp);
    }

  }

  public LinkedMapWrapper<String, ChildGroup> getChildGroups() {
    return childGroups;
  }

  public void setChildGroups(LinkedMapWrapper<String, ChildGroup> childGroups) {
    this.childGroups = childGroups;
  }

  public String getName() {
    return id.getName();
  }

  public void setName(String name) {
    id.setName(name);
  }

  public GroupKey getId() {
    return id;
  }

  public void setId(GroupKey id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRegister() {
    return register;
  }

  public void setRegister(String register) {
    this.register = register;
  }

  @Override
  public int compareTo(Group o) {
    return getId().compareTo(o.getId());
  }

}
