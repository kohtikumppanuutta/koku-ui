package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.List;

public class KKSProperty {

  private String name;

  private List<String> values;

  public KKSProperty(String name, String value) {
    super();
    this.name = name;
    this.values = new ArrayList<String>();
    this.values.add(value);
  }

  public KKSProperty(String name, List<String> values) {
    super();
    this.name = name;
    this.values = values;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getFirstValue() {
    if (values.size() == 0)
      return null;
    return values.get(0);
  }

  public List<String> getValues() {
    return values;
  }

  public void addValue(String value) {
    values.add(value);
  }

  public void setFirstValue(String value) {
    if (values.size() > 0)
      values.set(0, value);
    else
      values.add(value);
  }
}
