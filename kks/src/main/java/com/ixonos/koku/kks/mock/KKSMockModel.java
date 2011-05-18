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
  private List<KehitystietoOLD> entries;

  public KKSMockModel() {
    childs = new ArrayList<Henkilo>();
    entries = new ArrayList<KehitystietoOLD>();
  }

  public void addChild(Henkilo c) {
    childs.add(c);
  }

  public void addEntry(KehitystietoOLD e) {
    entries.add(e);
  }

  public List<Henkilo> getChilds() {
    return childs;
  }

  public void setChilds(List<Henkilo> childs) {
    this.childs = childs;
  }

  public List<KehitystietoOLD> getEntries() {
    return entries;
  }

  public void setEntries(List<KehitystietoOLD> entries) {
    this.entries = entries;
  }

}
