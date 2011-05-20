package com.ixonos.koku.kks.mock;

import java.util.List;

public class KKSProperty {

  private String name;

  private List<String> values;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<String> getValues() {
    return values;
  }

  public void addValue(String value) {
    values.add(value);
  }
}
