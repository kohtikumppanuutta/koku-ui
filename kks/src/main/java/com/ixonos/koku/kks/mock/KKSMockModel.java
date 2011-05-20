package com.ixonos.koku.kks.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock model for KKS
 * 
 * @author tuomape
 * 
 */
public class KKSMockModel {

  private List<Henkilo> childs;
  private List<Kehitystieto> entries;

  public KKSMockModel() {
    childs = new ArrayList<Henkilo>();
    entries = new ArrayList<Kehitystieto>();
  }

  public void addChild(Henkilo c) {
    childs.add(c);
  }

  public void addEntry(Kehitystieto e) {
    entries.add(e);
  }

  public List<Henkilo> getChilds() {
    return childs;
  }

  public void setChilds(List<Henkilo> childs) {
    this.childs = childs;
  }

  public List<Kehitystieto> getEntries() {
    return entries;
  }

  public void setEntries(List<Kehitystieto> entries) {
    this.entries = entries;
  }

}
