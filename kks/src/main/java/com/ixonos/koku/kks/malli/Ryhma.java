package com.ixonos.koku.kks.malli;

import com.ixonos.koku.kks.utils.LinkedMapWrapper;

/**
 * Sisältää tiedot, jotka kuuluu johonkin ryhmään
 * 
 * @author tuomape
 * 
 */
public class Ryhma {

  private String nimi;
  private LinkedMapWrapper<String, LapsiRyhma> lapsiryhmat;

  public Ryhma(String nimi) {
    this.nimi = nimi;
    lapsiryhmat = new LinkedMapWrapper<String, LapsiRyhma>();
  }

  public void lisaa(KirjausTyyppi tyyppi) {

    if (lapsiryhmat.get().containsKey(tyyppi.getRyhma())) {
      LapsiRyhma tmp = lapsiryhmat.get().get(tyyppi.getRyhma());
      tmp.lisaa(tyyppi);
    } else {
      LapsiRyhma tmp = new LapsiRyhma(tyyppi.getRyhma());
      tmp.lisaa(tyyppi);
      lapsiryhmat.get().put(tyyppi.getRyhma(), tmp);
    }

  }

  public LinkedMapWrapper<String, LapsiRyhma> getLapsiryhmat() {
    return lapsiryhmat;
  }

  public void setLapsiryhmat(LinkedMapWrapper<String, LapsiRyhma> lapsiryhmat) {
    this.lapsiryhmat = lapsiryhmat;
  }

  public String getNimi() {
    return nimi;
  }

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

}
