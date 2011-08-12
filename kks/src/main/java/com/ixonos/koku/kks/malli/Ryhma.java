package com.ixonos.koku.kks.malli;

import com.ixonos.koku.kks.utils.LinkedMapWrapper;
import com.ixonos.koku.kks.utils.RyhmaKey;

/**
 * Sisältää tiedot, jotka kuuluu johonkin ryhmään
 * 
 * @author tuomape
 * 
 */
public class Ryhma implements Comparable<Ryhma> {

  private RyhmaKey id;
  private String kuvaus;
  private String rekisteri;

  private LinkedMapWrapper<String, LapsiRyhma> lapsiryhmat;

  public Ryhma(RyhmaKey id, String rekisteri) {
    this(id, rekisteri, null);
  }

  public Ryhma(RyhmaKey id, String rekisteri, String kuvaus) {
    this.id = id;
    this.rekisteri = rekisteri;
    this.kuvaus = kuvaus;
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
    return id.getName();
  }

  public void setNimi(String nimi) {
    id.setName(nimi);
  }

  public RyhmaKey getId() {
    return id;
  }

  public void setId(RyhmaKey id) {
    this.id = id;
  }

  public String getKuvaus() {
    return kuvaus;
  }

  public void setKuvaus(String kuvaus) {
    this.kuvaus = kuvaus;
  }

  public String getRekisteri() {
    return rekisteri;
  }

  public void setRekisteri(String rekisteri) {
    this.rekisteri = rekisteri;
  }

  @Override
  public int compareTo(Ryhma o) {
    return getId().compareTo(o.getId());
  }

}
