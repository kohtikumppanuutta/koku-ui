package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo malli KKS:lle
 * 
 * @author tuomape
 * 
 */
public class KKSDemoMalli {

  private List<Henkilo> henkilot;

  public KKSDemoMalli() {
    henkilot = new ArrayList<Henkilo>();
  }

  public void lisaaHenkilo(Henkilo h) {
    henkilot.add(h);
  }

  public List<Henkilo> getHenkilot() {
    return henkilot;
  }

  public void setHenkilot(List<Henkilo> henkilot) {
    this.henkilot = henkilot;
  }

}
