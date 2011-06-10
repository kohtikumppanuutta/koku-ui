package com.ixonos.koku.kks.malli;

import java.util.ArrayList;
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
  private String kirjaaja;
  private int versio;
  private String rekisteri;
  private KirjausTyyppi tyyppi;
  private List<Luokitus> luokitukset;
  private List<String> arvot;

  public Kirjaus(String arvo, Date luontiAika, int versio, String rekisteri,
      String kirjaaja, KirjausTyyppi tyyppi) {
    super();
    this.arvo = arvo;
    this.luontiAika = luontiAika;
    this.versio = versio;
    this.rekisteri = rekisteri;
    this.tyyppi = tyyppi;
    this.arvot = new ArrayList<String>();
    this.kirjaaja = kirjaaja;
    this.arvot.add(arvo);
  }

  public String getArvo() {
    return arvo;
  }

  public void setArvo(String arvo) {
    this.arvot.remove(this.arvo);
    this.arvo = arvo;
    this.arvot.add(arvo);
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

  public List<String> getArvot() {
    return arvot;
  }

  public void setArvot(List<String> arvot) {
    this.arvot = arvot;
  }

  public String getKirjaaja() {
    return kirjaaja;
  }

  public void setKirjaaja(String kirjaaja) {
    this.kirjaaja = kirjaaja;
  }

}
