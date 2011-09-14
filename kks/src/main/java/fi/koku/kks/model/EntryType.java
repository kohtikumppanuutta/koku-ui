package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.List;

import fi.koku.kks.ui.common.DataType;

/**
 * Kuvaa collectionn yhden tiedon sisällön, edellytykset ja mahdollisuudet
 * 
 * @author tuomape
 */
public class EntryType implements Comparable<EntryType> {

  private String id;
  private String name;
  private String description;
  private String creationDesc;
  private boolean multiValue;
  private DataType dataType;
  private List<String> values;
  private String accountable;
  private String group;
  private String register;
  private String subGroup;
  private List<Classification> classifications;
  private List<Classification> developmentTypes;

  public EntryType(String id, String name, String description, boolean multiValue, DataType dataType,
      List<String> values, String accountable, String group, String register, String subGroup) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.multiValue = multiValue;
    this.dataType = dataType;
    this.values = values;
    this.accountable = accountable;
    this.group = group;
    this.register = register;
    this.subGroup = subGroup;
    this.classifications = new ArrayList<Classification>();
    this.developmentTypes = new ArrayList<Classification>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

  public boolean isMultiValue() {
    return multiValue;
  }

  public void setMultiValue(boolean multiValue) {
    this.multiValue = multiValue;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public String getAccountable() {
    return accountable;
  }

  public void setAccountable(String accountable) {
    this.accountable = accountable;
  }

  public String getRegister() {
    return register;
  }

  public void setRegister(String register) {
    this.register = register;
  }

  public List<Classification> getClassifications() {
    return classifications;
  }

  public void setClassifications(List<Classification> classifications) {
    this.classifications = classifications;
  }

  public List<Classification> getDevelopmentTypes() {
    return developmentTypes;
  }

  public void setDevelopmentTypes(List<Classification> developmentTypes) {
    this.developmentTypes = developmentTypes;
  }

  public String getSubGroup() {
    return subGroup;
  }

  public void setSubGroub(String subGroup) {
    this.subGroup = subGroup;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getDevelopmentTypesAsText() {
    StringBuffer sb = new StringBuffer();
    List<Classification> tmp = getDevelopmentTypes();
    for (int i = 0; i < tmp.size(); i++) {
      Classification l = tmp.get(i);
      sb.append(l.getName());

      if ((i + 1) < tmp.size()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  public String getClassificationsAsText() {
    StringBuffer sb = new StringBuffer();
    List<Classification> tmp = getClassifications();
    for (int i = 0; i < tmp.size(); i++) {
      Classification l = tmp.get(i);
      sb.append(l.getName());

      if ((i + 1) < tmp.size()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  public String getValuesAsText() {
    StringBuffer sb = new StringBuffer();
    List<String> tmp = getValues();

    if (tmp == null) {
      return "";
    }

    for (int i = 0; i < tmp.size(); i++) {
      String l = tmp.get(i);
      sb.append(l);

      if ((i + 1) < tmp.size()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  public String getCreationDesc() {
    return creationDesc;
  }

  public void setCreationDesc(String creationDesc) {
    this.creationDesc = creationDesc;
  }

  @Override
  public int compareTo(EntryType o) {
    return id.compareTo(o.getId());
  }

}
