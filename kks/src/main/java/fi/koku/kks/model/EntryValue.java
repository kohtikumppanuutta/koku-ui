/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class for entry value
 * 
 * @author Ixonos / tuomape
 *
 */
public class EntryValue {

  private String id;
  private String value;
  private Date modified;
  private String modifier;
  private String modifierFullName;
  private List<String> values;

  public EntryValue() {
    values = new ArrayList<String>();
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.values.remove(this.value);
    this.value = value;
    this.values.add(value);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  public String getModifier() {
    return modifier;
  }

  public void setModifier(String modifier) {
    this.modifier = modifier;
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

  public String getValuesAsText() {
    StringBuffer sb = new StringBuffer();

    if (values != null) {
      for (int i = 0; i < values.size(); i++) {
        sb.append(values.get(i));

        if ((i + 1) < values.size()) {
          sb.append(", ");
        }
      }
    }
    return sb.toString();
  }

  public String getModifierFullName() {
    return modifierFullName;
  }

  public void setModifierFullName(String modifierFullName) {
    this.modifierFullName = modifierFullName;
  }

}
