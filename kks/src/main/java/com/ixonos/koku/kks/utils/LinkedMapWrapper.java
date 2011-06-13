package com.ixonos.koku.kks.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedMapWrapper<K, V> {

  private Map<K, V> map;

  public LinkedMapWrapper() {
    map = new LinkedHashMap<K, V>();
  }

  public Collection<V> getValues() {
    return map.values();
  }

  public Map<K, V> get() {
    return map;
  }
}
