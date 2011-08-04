package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
import java.util.List;

import com.ixonos.koku.kks.utils.LinkedMapWrapper;

/**
 * Sisältää ryhmän sisällä olevan ryhmän tiedot
 * 
 * @author tuomape
 * 
 */
public class LapsiRyhma {

  private String nimi;
  private LinkedMapWrapper<String, List<KirjausTyyppi>> tyypit;

  public LapsiRyhma(String nimi) {
    this.nimi = nimi;
    tyypit = new LinkedMapWrapper<String, List<KirjausTyyppi>>();
  }

  public void lisaa(KirjausTyyppi tyyppi) {

    if (tyypit.get().containsKey(tyyppi.getRyhma())) {
      List<KirjausTyyppi> tmp = tyypit.get().get(tyyppi.getRyhma());
      tmp.add(tyyppi);
    } else {
      List<KirjausTyyppi> tmp = new ArrayList<KirjausTyyppi>();
      tmp.add(tyyppi);
      tyypit.get().put(tyyppi.getRyhma(), tmp);
    }

  }

  public LinkedMapWrapper<String, List<KirjausTyyppi>> getTyypit() {
    return tyypit;
  }

  public String getNimi() {
    return nimi;
  }

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

}
