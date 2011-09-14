package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.koku.services.entity.kks.v1.KksEntryClassType;
import fi.koku.services.entity.kks.v1.KksTagType;
import fi.koku.services.entity.kks.v1.KksTagsType;

/**
 * Holds single entry
 * 
 * @author tuomape
 * 
 */
public class Entry {

  private String id;
  private String value;
  private String valueId;
  private Date creationTime;
  private String recorder;
  private String version;
  private KksEntryClassType type;
  private List<KksTagType> tags;
  private List<String> values;

  public Entry(String id, String valueId, String value, Date creation, String version, String recorder,
      KksEntryClassType type) {
    super();
    this.id = id;
    this.value = value;
    this.valueId = valueId;
    this.creationTime = creation;
    this.version = version;
    this.type = type;
    this.values = new ArrayList<String>();
    this.recorder = recorder;
    this.values.add(value);
    this.tags = new ArrayList<KksTagType>();
  }

  public Entry(String value, String valueId, Date creation, String version, String recorder, KksEntryClassType type) {
    super();
    this.id = null;
    this.value = value;
    this.creationTime = creation;
    this.valueId = valueId;
    this.version = version;
    this.type = type;
    this.values = new ArrayList<String>();
    this.recorder = recorder;
    this.values.add(value);
    this.tags = new ArrayList<KksTagType>();
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

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public KksEntryClassType getType() {
    return type;
  }

  public void setType(KksEntryClassType type) {
    this.type = type;
  }

  public List<KksTagType> getClassifications() {

    List<KksTagType> tmp = new ArrayList<KksTagType>(tags);
    KksTagsType tags = getType().getKksTags();
    tmp.addAll(tags.getKksTag());
    return tmp;
  }

  public void setClassifications(List<KksTagType> classifications) {
    this.tags = classifications;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;

    if (values.size() > 0) {
      this.value = values.get(0);
    }
  }

  public String getRecorder() {
    return recorder;
  }

  public void setRecorder(String recorder) {
    this.recorder = recorder;
  }

  public boolean hasClassification(String classification) {
    for (KksTagType l : type.getKksTags().getKksTag()) {
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

  public String getValueId() {
    return valueId;
  }

  public void setValueId(String valueId) {
    this.valueId = valueId;
  }

  public String getValuesAsText() {
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < values.size(); i++) {
      sb.append(values.get(i));

      if ((i + 1) < values.size()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

}
