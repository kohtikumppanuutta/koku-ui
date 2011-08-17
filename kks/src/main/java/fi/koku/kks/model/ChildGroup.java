package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.List;

import fi.koku.kks.ui.common.utils.LinkedMapWrapper;

/**
 * Holds child group information
 * 
 * @author tuomape
 * 
 */
public class ChildGroup {

  private String name;
  private LinkedMapWrapper<String, List<EntryType>> types;

  public ChildGroup(String name) {
    this.name = name;
    types = new LinkedMapWrapper<String, List<EntryType>>();
  }

  public void add(EntryType type) {

    if (types.get().containsKey(type.getSubGroup())) {
      List<EntryType> tmp = types.get().get(type.getSubGroup());
      tmp.add(type);
    } else {
      List<EntryType> tmp = new ArrayList<EntryType>();
      tmp.add(type);
      types.get().put(type.getSubGroup(), tmp);
    }

  }

  public LinkedMapWrapper<String, List<EntryType>> getTypes() {
    return types;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
