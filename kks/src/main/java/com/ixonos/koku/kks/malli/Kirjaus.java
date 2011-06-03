package com.ixonos.koku.kks.malli;

import java.util.Date;
import java.util.List;

/**
 * Sisältää yhden kirjauksen tiedot
 * 
 * @author tuomape
 * 
 */
public class Kirjaus {

  private String arvo;
  private Date luontiAika;
  private int versio;
  private String rekisteri;
  private KirjausTyyppi tyyppi;
  private List<Luokitus> luokitukset;

  public String getArvo() {
    return arvo;
  }

  public void setArvo(String arvo) {
    this.arvo = arvo;
  }

  public Date getLuontiAika() {
    return luontiAika;
  }

  public void setLuontiAika(Date luontiAika) {
    this.luontiAika = luontiAika;
  }

  public int getVersio() {
    return versio;
  }

  public void setVersio(int versio) {
    this.versio = versio;
  }

  public String getRekisteri() {
    return rekisteri;
  }

  public void setRekisteri(String rekisteri) {
    this.rekisteri = rekisteri;
  }

  public KirjausTyyppi getTyyppi() {
    return tyyppi;
  }

  public void setTyyppi(KirjausTyyppi tyyppi) {
    this.tyyppi = tyyppi;
  }

  public List<Luokitus> getLuokitukset() {
    return luokitukset;
  }

  public void setLuokitukset(List<Luokitus> luokitukset) {
    this.luokitukset = luokitukset;
  }

}
