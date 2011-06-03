package com.ixonos.koku.kks.malli;

import java.util.List;

/**
 * Kuvaa kokoelman sisällön, edellytykset ja mahdollisuudet
 * 
 * @author tuomape
 */
public class KokoelmaTyyppi {

  private int koodi;
  private String nimi;
  private String kuvaus;
  private List<KirjausTyyppi> kirjausTyypit;

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

  public List<KirjausTyyppi> getKirjausTyypit() {
    return kirjausTyypit;
  }

  public void setKirjausTyypit(List<KirjausTyyppi> kirjausTyypit) {
    this.kirjausTyypit = kirjausTyypit;
  }

}
