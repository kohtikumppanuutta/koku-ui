package com.ixonos.koku.kks.malli;

/**
 * Sisältää luokituksen jollekkin asialle tai asiakokonaisuudelle
 * 
 * @author tuomape
 */
public class Luokitus {

  private int koodi;
  private String nimi;
  private String kuvaus;

  public Luokitus(int koodi, String nimi, String kuvaus) {
    super();
    this.koodi = koodi;
    this.nimi = nimi;
    this.kuvaus = kuvaus;
  }

  public int getKoodi() {
    return koodi;
  }

  public void setKoodi(int koodi) {
    this.koodi = koodi;
  }

  public String getNimi() {
    return nimi;
  }

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

  public String getKuvaus() {
    return kuvaus;
  }

  public void setKuvaus(String kuvaus) {
    this.kuvaus = kuvaus;
  }

}
