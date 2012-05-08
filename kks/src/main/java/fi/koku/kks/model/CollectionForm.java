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

import java.util.HashMap;
import java.util.Map;

/**
 * Form class for editing collection entries
 * 
 * @author Ixonos / tuomape
 *
 */
public class CollectionForm {
 
  private Map<String, Entry> entries;
  
  public CollectionForm() {
    entries = new HashMap<String, Entry>();
  }

  public Map<String, Entry> getEntries() {
    return entries;
  }

  public void setEntries(Map<String, Entry> entries) {
    this.entries = entries;
  }
  
  
}
