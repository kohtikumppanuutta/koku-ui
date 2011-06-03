package com.ixonos.koku.kks.malli;

import java.util.Date;
import java.util.List;

import com.ixonos.koku.kks.utils.enums.Tila;

/**
 * Sisältää yhden kasvatuksen ja kehityksen kokoelman tiedot
 * 
 * @author tuomape
 */
public class Kokoelma {

  private String nimi;
  private String kuvaus;
  private Tila tila;
  private Date luontiAika;
  private int versio;
  private KokoelmaTyyppi tyyppi;
  private List<Kirjaus> kirjaukset;
  private List<Luokitus> luokitukset;

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

  public Tila getTila() {
    return tila;
  }

  public void setTila(Tila tila) {
    this.tila = tila;
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

  public List<Kirjaus> getKirjaukset() {
    return kirjaukset;
  }

  public void setKirjaukset(List<Kirjaus> kirjaukset) {
    this.kirjaukset = kirjaukset;
  }

  public List<Luokitus> getLuokitukset() {
    return luokitukset;
  }

  public void setLuokitukset(List<Luokitus> luokitukset) {
    this.luokitukset = luokitukset;
  }

  public KokoelmaTyyppi getTyyppi() {
    return tyyppi;
  }

  public void setTyyppi(KokoelmaTyyppi tyyppi) {
    this.tyyppi = tyyppi;
  }

}
