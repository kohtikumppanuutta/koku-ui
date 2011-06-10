package com.ixonos.koku.kks.malli;

import java.util.HashMap;
import java.util.Map;

public class Kirjaukset {

  public Map<String, Kirjaus> kirjaukset;

  public Kirjaukset() {
    kirjaukset = new HashMap<String, Kirjaus>();
  }

  public void lisaaKirjaus(Kirjaus kirjaus) {
    kirjaukset.put("" + kirjaus.getTyyppi().getKoodi(), kirjaus);
  }

  public Map<String, Kirjaus> getKirjaukset() {
    return kirjaukset;
  }

  public void setKirjaukset(Map<String, Kirjaus> kirjaukset) {
    this.kirjaukset = kirjaukset;
  }

}
