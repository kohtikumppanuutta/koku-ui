package com.ixonos.koku.kks.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Wrapper luokka mapille (helpottaa käyttöä JSP puolella)
 * 
 * @author tuomape
 * 
 * @param <K>
 * @param <V>
 */
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
