package com.ixonos.koku.kks.mock;

import java.util.Map;

public class KehitysAsia {

  private String nimi;
  private Map<String, KKSProperty> properties;

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

  public String getNimi() {
    return nimi;
  }

  public void setProperties(Map<String, KKSProperty> properties) {
    this.properties = properties;
  }

  public Map<String, KKSProperty> getProperties() {
    return properties;
  }

  public void addProperty(KKSProperty p) {
    properties.put(p.getName(), p);
  }
}
