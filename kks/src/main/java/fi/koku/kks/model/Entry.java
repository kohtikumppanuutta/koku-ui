/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
 *
 */
package fi.koku.kks.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.koku.services.entity.kks.v1.KksEntryClassType;
import fi.koku.services.entity.kks.v1.KksTagType;

/**
 * Holds single entry
 * 
 * @author tuomape
 * 
 */
public class Entry {

  private String id;
  private Date creationTime;
  private String recorder;
  private String modifierFullName;
  private String version;
  private KksEntryClassType type;
  private List<EntryValue> entryValues;

  public Entry(String id, Date creation, String version, String recorder, KksEntryClassType type) {
    super();
    this.id = id;
    this.creationTime = creation;
    this.version = version;
    this.type = type;
    this.recorder = recorder;
    this.entryValues = new ArrayList<EntryValue>();
  }

  public Entry(Date creation, String version, String recorder, KksEntryClassType type) {
    super();
    this.id = null;
    this.creationTime = creation;
    this.version = version;
    this.type = type;
    this.recorder = recorder;
    this.entryValues = new ArrayList<EntryValue>();
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

  public String getModifierFullName() {
    return modifierFullName;
  }

  public void setModifierFullName(String modifierFullName) {
    this.modifierFullName = modifierFullName;
  }

  public List<EntryValue> getEntryValues() {
    return entryValues;
  }

  public void setEntryValues(List<EntryValue> entryValues) {
    this.entryValues = entryValues;
  }

  public void addEntryValue(EntryValue e) {
    entryValues.add(e);
  }

  public EntryValue getEntryValue(String id) {
    for (EntryValue v : entryValues) {
      if (v.getId().equals(id)) {
        return v;
      }
    }
    return null;
  }

  public List<String> getValueChoices() {
    List<String> tmp = new ArrayList<String>();

    List<String> valuaSpaces = type.getValueSpaces().getValueSpace();

    if (valuaSpaces != null) {
      for (String s : valuaSpaces) {
        tmp.add(s.trim());
      }
    }
    return tmp;
  }

  public EntryValue getFirstValue() {
    if (entryValues.size() == 0) {
      EntryValue v = new EntryValue();
      addEntryValue(v);
    }

    return entryValues.get(0);
  }
}
