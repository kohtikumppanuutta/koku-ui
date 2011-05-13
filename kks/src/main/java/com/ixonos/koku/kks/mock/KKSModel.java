package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock model for KKS
 * 
 * @author tuomape
 * 
 */
public class KKSModel {

  private List<Lapsi> childs;
  private List<Kehitystieto> entries;

  public KKSModel() {
    childs = new ArrayList<Lapsi>();
    entries = new ArrayList<Kehitystieto>();
  }

  public void addChild(Lapsi c) {
    childs.add(c);
  }

  public void addEntry(Kehitystieto e) {
    entries.add(e);
  }

  public List<Lapsi> getChilds() {
    return childs;
  }

  public void setChilds(List<Lapsi> childs) {
    this.childs = childs;
  }

  public List<Kehitystieto> getEntries() {
    return entries;
  }

  public void setEntries(List<Kehitystieto> entries) {
    this.entries = entries;
  }

}
