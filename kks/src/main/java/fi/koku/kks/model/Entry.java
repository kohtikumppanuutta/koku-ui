package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Holds single entry
 * 
 * @author tuomape
 * 
 */
public class Entry {

  private static int idGenerator = 0;
  private String id;
  private String value;
  private Date creationTime;
  private String recorder;
  private int version;
  private String register;
  private EntryType type;
  private List<Classification> classifications;
  private List<String> values;

  public Entry(String value, Date creation, int version, String register, String recorder, EntryType type) {
    super();
    this.id = "" + idGenerator++;
    this.value = value;
    this.creationTime = creation;
    this.version = version;
    this.register = register;
    this.type = type;
    this.values = new ArrayList<String>();
    this.recorder = recorder;
    this.values.add(value);
    this.classifications = new ArrayList<Classification>();
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.values.remove(this.value);
    this.value = value;
    this.values.add(value);
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creation) {
    this.creationTime = creation;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public String getRegister() {
    return register;
  }

  public void setRegister(String register) {
    this.register = register;
  }

  public EntryType getType() {
    return type;
  }

  public void setType(EntryType type) {
    this.type = type;
  }

  public List<Classification> getClassifications() {

    List<Classification> tmp = new ArrayList<Classification>(classifications);

    tmp.addAll(getType().getClassifications());
    return tmp;
  }

  public void setClassifications(List<Classification> classifications) {
    this.classifications = classifications;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public String getRecorder() {
    return recorder;
  }

  public void setRecorder(String recorder) {
    this.recorder = recorder;
  }

  public boolean hasClassification(String classification) {

    for (Classification l : type.getClassifications()) {
      if (l.getName().startsWith(classification)) {
        return true;
      }
    }

    for (Classification l : type.getDevelopmentTypes()) {
      if (l.getName().startsWith(classification)) {
        return true;
      }
    }
    return false;

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
